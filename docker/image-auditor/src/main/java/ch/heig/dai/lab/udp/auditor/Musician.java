package ch.heig.dai.lab.udp.auditor;

import java.time.Instant;
import java.util.Objects;

public class Musician {
    private final String uuid;
    private final Instrument instrument;
    private Instant firstActivity;
    private Instant lastActivity;

    public Musician(String uuid, Instrument instrument, Instant firstActivity, Instant lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.firstActivity = firstActivity;
        this.lastActivity = lastActivity;
    }

    public String getUuid() {
        return uuid;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Instant getFirstActivity() {
        return firstActivity;
    }

    public void setFirstActivity(Instant firstActivity) {
        this.firstActivity = firstActivity;
    }

    public Instant getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Instant lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return Objects.equals(uuid, musician.uuid) && instrument == musician.instrument;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, instrument);
    }
}
