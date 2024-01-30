package ch.heig.dai.lab.udp.auditor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Sound {
    private UUID uuid;
    private String sound;
    private long lastActivity;

    public Sound(UUID uuid, String sound, long lastActivity) {
        this.uuid = uuid;
        this.sound = sound;
        this.lastActivity = lastActivity;
    }
}
