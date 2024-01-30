package ch.heig.dai.udp.musician;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Musician {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    private final static HashMap<String, String> instrumentSounds = new HashMap<>();
    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    @Expose
    private UUID uuid;

    private String instrument;
    @Expose
    private String sound;

    @Expose
    private long lastActivity;


    public Musician(String instrument){
        uuid = UUID.randomUUID();
        sound = instrumentSounds.get(instrument);
        this.instrument = instrument;
        lastActivity = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static void checkInstrumentFromArgs(String ... args){
        if(args.length < 1){
            System.out.println("Please provide the instrument as a command-line argument.");
            System.exit(1);
        } else if (!instrumentSounds.containsKey(args[0])) {
            System.out.println("Invalid Instrument.");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        // Fill in the instrument list with their sound.
        instrumentSounds.put("PIANO", "ti-ta-ti");
        instrumentSounds.put("TRUMPET", "pouet");
        instrumentSounds.put("FLUTE", "trulu");
        instrumentSounds.put("VIOLIN", "gzi-gzi");
        instrumentSounds.put("DRUM", "boum-boum");

        checkInstrumentFromArgs(args);

        Musician musician = new Musician(args[0]);
        musician.play();
    }

    public void play() throws IOException, InterruptedException {
        try (DatagramSocket socket = new DatagramSocket()) {

            byte[] payload = toString().getBytes(UTF_8);
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);
            var packet = new DatagramPacket(payload, payload.length, dest_address);
            while (true) {
                socket.send(packet);
                System.out.println("Sound...");
                Thread.sleep(Duration.ofSeconds(1));
            }
        }
    }
}