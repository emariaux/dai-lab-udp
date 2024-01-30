package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;


public class Musician {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableInnerClassSerialization()
            .create();
    @Expose
    private UUID uuid;

    @Expose
    private String instrument;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Expose
    private long lastActivity;

    public Musician(UUID uuid, String instrument) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = System.currentTimeMillis();
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

    public String toString() {
        return gson.toJson(this);
    }
}
