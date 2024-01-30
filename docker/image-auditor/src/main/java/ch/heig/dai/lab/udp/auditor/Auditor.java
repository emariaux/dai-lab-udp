package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Auditor {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int UDP_PORT = 9904;
    private final static int TCP_PORT = 2205;
    private static List<Musician> activeMusicians = new ArrayList<>();
    private final static HashMap<String, String> instrumentSounds = new HashMap<>();

    static {
        // Fill in the instrument list with their sound.
        instrumentSounds.put("PIANO", "ti-ta-ti");
        instrumentSounds.put("TRUMPET", "pouet");
        instrumentSounds.put("FLUTE", "trulu");
        instrumentSounds.put("VIOLIN", "gzi-gzi");
        instrumentSounds.put("DRUM", "boum-boum");
    }


    private void sendMusiciansList(){
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                     var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    // Removes the musicians who are inactive for 5 seconds.
                    activeMusicians.removeIf(musician -> musician.getLastActivity() < System.currentTimeMillis() - 5000);

                    Gson gson = new Gson();
                    String message = gson.toJson(activeMusicians);

                    out.write(message);

                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }

    private void receiveMusicianSounds(){
        try (MulticastSocket socket = new MulticastSocket(UDP_PORT)) {
            InetSocketAddress group_address =  new InetSocketAddress(IPADDRESS, UDP_PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group_address, netif);

            try{
                while (true){
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);

                    Gson gson = new Gson();
                    Sound sound = gson.fromJson(message, Sound.class);

                    Musician musician = new Musician(sound.getUuid(), instrumentSounds.get(sound.getSound()), sound.getLastActivity());

                    // Removes the old musician.
                    activeMusicians.removeIf(existingMusician -> existingMusician.equals(musician));

                    // Adds the same musician with his lastActivity time updated.
                    activeMusicians.add(musician);

                    System.out.println("Received message: " + message);
                }
            } finally {
                socket.leaveGroup(group_address, netif);
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Auditor auditor = new Auditor();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            executor.execute(auditor::sendMusiciansList);
            executor.execute(auditor::receiveMusicianSounds);
        }
    }
}