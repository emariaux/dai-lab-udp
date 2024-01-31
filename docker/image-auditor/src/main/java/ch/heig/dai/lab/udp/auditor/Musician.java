package ch.heig.dai.lab.udp.auditor;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * This class represents a musician in the orchestra.
 */
@Getter
@Setter
public class Musician {
    private UUID uuid;

    private String instrument;
    private long lastActivity;

    public Musician(UUID uuid, String instrument) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = System.currentTimeMillis();
    }

    /**
     * Checks if two musicians are equal based on their UUIDs.
     *
     * @param o The object to compare with.
     * @return True if the musicians have the same UUID, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return Objects.equals(uuid, musician.uuid);
    }

    /**
     * Generates a hash code based on the musician's UUID.
     *
     * @return The hash code.
     */
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
