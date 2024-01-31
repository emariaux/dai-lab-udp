package ch.heig.dai.lab.udp.auditor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents an auditor.
 */
@Slf4j
public class Auditor {

    public static void main(String[] args) {
        Orchestra orchestra = new Orchestra();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            executor.execute(new AuditorTCPServer(orchestra));
            executor.execute(new AuditorMulticastReceiver(orchestra));
        }catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }
}