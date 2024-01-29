package ch.heig.dai.udp.musician;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Musician {
    final static String IPADDRESS = "239.255.22.5";
    final static int PORT = 9904;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {

            String message = "Hello group members!";
            byte[] payload = message.getBytes(UTF_8);
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);
            var packet = new DatagramPacket(payload, payload.length, dest_address);

            socket.send(packet);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}