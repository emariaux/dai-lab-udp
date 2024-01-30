package ch.heig.dai.lab.udp.auditor;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
public class Musician {
    private UUID uuid;
    private String instrument;
    private long lastActivity;

    public Musician(UUID uuid, String instrument, long lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = lastActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return Objects.equals(uuid, musician.uuid);
    }

    public int hashCode() {
        return Objects.hash(uuid);
    }
}
