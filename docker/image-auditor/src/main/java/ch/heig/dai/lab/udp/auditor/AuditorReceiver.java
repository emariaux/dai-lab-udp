package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.HashSet;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuditorReceiver {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    private Gson gson = new Gson();
    private HashSet<Musician> activeMusicians;

    public void read(){
        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            InetSocketAddress group_address =  new InetSocketAddress(IPADDRESS, PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group_address, netif);

            try{
                while (true){
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);

                    Instrument instrument = gson.fromJson(message, Instrument.class);

                    System.out.println("Received message: " + message + " from " + packet.getAddress() + ", port " + packet.getPort());
                }
            } finally {
                socket.leaveGroup(group_address, netif);
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
