package ch.heig.dai.lab.udp.auditor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Represents a UDP multicast receiver for the auditor.
 * It listens for multicast messages on a specified IP address and port,
 * receives musician data, and adds them to the orchestra.
 */
@Slf4j
public class AuditorMulticastReceiver implements Runnable{

    private final static String IPADDRESS = "239.255.22.5";
    private final static int UDP_PORT = 9904;
    private final Orchestra orchestra;

    public AuditorMulticastReceiver(Orchestra orchestra) {
        this.orchestra = orchestra;
    }

    /**
     * Executes the main logic of the UDP multicast server.
     *
     * @throws IOException If there is an I/O error during multicast communication.
     */
    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(UDP_PORT)) {
            InetSocketAddress group_address =  new InetSocketAddress(IPADDRESS, UDP_PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group_address, netif);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);

                orchestra.addMusician(message);

                packet.setLength(buffer.length);

            }
        }
        catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
