package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
public class Musician {
    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();
    @Expose
    private UUID uuid;

    @Expose
    private String instrument;

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
}
