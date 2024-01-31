package ch.heig.dai.lab.udp.auditor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * This class represents a sound produced by a musician.
 */
@Getter
@Setter
public class Sound {
    private UUID uuid;
    private String sound;

    public Sound(UUID uuid, String sound) {
        this.uuid = uuid;
        this.sound = sound;
    }
}
