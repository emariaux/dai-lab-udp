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

    public static void main(String[] args) {
        Orchestra orchestra = new Orchestra();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            executor.execute(new AuditorTCPServer(orchestra));
            new AuditorMulticastReceiver(orchestra).run();
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}