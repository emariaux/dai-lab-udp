package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuditorTCPServer implements Runnable{

    private final static int TCP_PORT = 2205;

    private final Orchestra orchestra;

    public AuditorTCPServer(Orchestra orchestra) {
        this.orchestra = orchestra;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    System.out.println("TCP Server request received");

                    out.write( orchestra.getActiveMusicians() + "\n");
                    out.flush();

                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }
}
