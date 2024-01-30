package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
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

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    // Removes the musicians who are inactive for 5 seconds.
                    activeMusicians.removeIf(musician -> musician.getLastActivity() < System.currentTimeMillis() - 5000);

                    String message = gson.toJson(activeMusicians);

                    out.write(message);
                    out.flush();

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

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);


            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);

                Sound sound = gson.fromJson(message, Sound.class);

                Musician musician = new Musician(sound.getUuid(), instrumentSounds.get(sound.getSound()));

                if(!activeMusicians.contains(musician)){
                    // Adds the musician
                    activeMusicians.add(musician);
                }else {
                    for(var m : activeMusicians){
                        if(m.equals(musician)){
                            m.setLastActivity(System.currentTimeMillis());
                        }
                    }
                }

                System.out.println("Received message: " + message);
                packet.setLength(buffer.length);
            }

        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Auditor auditor = new Auditor();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            executor.submit(auditor::sendMusiciansList);
            executor.submit(auditor::receiveMusicianSounds);
        }
    }
}