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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Musician {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Instrument.class, (JsonSerializer<Instrument>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getSound()))
            .create();
    @Expose
    private UUID uuid;

    @SerializedName("sound")
    @Expose
    private Instrument instrument;

    public Musician(Instrument instrument){
        uuid = UUID.randomUUID();
        this.instrument = instrument;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static Instrument getInstrumentFromArgs(String ... args){
        if(args.length < 1){
            System.out.println("Please provide the instrument as a command-line argument.");
            System.exit(1);
        }

        try{
            return Instrument.valueOf(args[0].toUpperCase());
        }catch (IllegalArgumentException e){
            System.out.println("Invalid Instrument.");
            System.exit(1);
        }

        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Instrument instrument = getInstrumentFromArgs(args);
        Musician musician = new Musician(instrument);
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