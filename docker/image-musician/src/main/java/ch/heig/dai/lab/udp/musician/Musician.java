package ch.heig.dai.lab.udp.musician;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class represents a musician producing sounds.
 */
@Slf4j
@Getter
@Setter
public class Musician {

    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    private final static HashMap<String, String> instrumentSounds = new HashMap<>();
    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    @Expose
    private UUID uuid;

    private String instrument;

    @Expose
    private String sound;

    /**
     * Static block to initialize the mapping of instruments to sounds.
     */
    static {
        instrumentSounds.put("PIANO", "ti-ta-ti");
        instrumentSounds.put("TRUMPET", "pouet");
        instrumentSounds.put("FLUTE", "trulu");
        instrumentSounds.put("VIOLIN", "gzi-gzi");
        instrumentSounds.put("DRUM", "boum-boum");
    }

    public Musician(String instrument){
        uuid = UUID.randomUUID();
        sound = instrumentSounds.get(instrument.toUpperCase());
        this.instrument = instrument;
    }

    /**
     * Converts the musician object to its JSON representation.
     *
     * @return The JSON representation of the musician.
     */
    @Override
    public String toString() {
        return gson.toJson(this);
    }

    /**
     * Checks if the instrument is provided as a command-line argument and if the instrument exists.
     *
     * @param args The command-line arguments.
     */
    public static void checkInstrumentFromArgs(String ... args){
        if(args.length < 1){
            log.error("Please provide an instrument as a command-line argument.");
            System.exit(1);
        } else if (!instrumentSounds.containsKey(args[0].toUpperCase())) {
            log.error("Invalid Instrument.");
            System.exit(1);
        }
    }

    /**
     * Sends the sound produced by the musician over UDP multicast.
     *
     * @param musician The musician producing the sound.
     */
    private static void sendSound(Musician musician) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);

            byte[] payload = musician.toString().getBytes(UTF_8);
            var packet = new DatagramPacket(payload, payload.length, dest_address);

            socket.send(packet);
            log.info("Sound...");

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        checkInstrumentFromArgs(args);

        Musician musician = new Musician(args[0]);

        // Sends sound every second.
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> sendSound(musician), 0, 1, TimeUnit.SECONDS);
    }
}