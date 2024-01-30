package ch.heig.dai.lab.udp.musician;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

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

    //@Expose
    //private long lastActivity;

    static {
        // Fill in the instrument list with their sound.
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
        //lastActivity = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static void checkInstrumentFromArgs(String ... args){
        if(args.length < 1){
            System.out.println("Please provide the instrument as a command-line argument.");
            System.exit(1);
        } else if (!instrumentSounds.containsKey(args[0].toUpperCase())) {
            System.out.println("Invalid Instrument.");
            System.exit(1);
        }
    }

    private static void sendSound(Musician musician) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);

            // Updates the last activity time before sending the new packet.
           // musician.setLastActivity(System.currentTimeMillis());

            byte[] payload = musician.toString().getBytes(UTF_8);
            var packet = new DatagramPacket(payload, payload.length, dest_address);

            socket.send(packet);
            System.out.println("Sound...");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        checkInstrumentFromArgs(args);

        Musician musician = new Musician(args[0]);

        // Sends sound every second.
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> sendSound(musician), 0, 1, TimeUnit.SECONDS);

    }
}