package ch.heig.dai.lab.udp.auditor;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class represents a TCP server for the auditor.
 * It listens for incoming connections, handles requests for the list of active musicians,
 * and responds with the current list of active musicians from the orchestra.
 */
@Slf4j
public class AuditorTCPServer implements Runnable{

    private final static int TCP_PORT = 2205;

    private final Orchestra orchestra;

    public AuditorTCPServer(Orchestra orchestra) {
        this.orchestra = orchestra;
    }

    /**
     * Executes the main logic of the TCP server.
     *
     * @throws IOException If there is an I/O error during TCP communication.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    log.info("TCP Server request received");

                    out.write( orchestra.getActiveMusicians() + "\n");
                    out.flush();

                } catch (IOException e) {
                    log.error("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            log.error("Server: server socket ex.: " + e);
        }
    }
}
