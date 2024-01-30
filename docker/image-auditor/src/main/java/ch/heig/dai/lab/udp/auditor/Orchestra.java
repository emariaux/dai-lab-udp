package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orchestra {

    private final List<Musician> activeMusicians = new ArrayList<>();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final static HashMap<String, String> instrumentSounds = new HashMap<>();

    static {
        // Fill in the instrument list with their sound.
        instrumentSounds.put("PIANO", "ti-ta-ti");
        instrumentSounds.put("TRUMPET", "pouet");
        instrumentSounds.put("FLUTE", "trulu");
        instrumentSounds.put("VIOLIN", "gzi-gzi");
        instrumentSounds.put("DRUM", "boum-boum");
    }

    public void addMusician(String message){
        Sound sound = gson.fromJson(message, Sound.class);

        Musician musician = new Musician(sound.getUuid(), instrumentSounds.get(sound.getSound()));

        if(!activeMusicians.contains(musician)){
            // Adds the musician
            activeMusicians.add(musician);
        }else {
            for(var m : activeMusicians){
                if(m.equals(musician)){
                    m.setLastActivity(System.currentTimeMillis());
                }
            }
        }
    }

    public String getActiveMusicians(){
        // Removes the musicians who are inactive for 5 seconds.
        activeMusicians.removeIf(musician -> musician.getLastActivity() < System.currentTimeMillis() - 5000);

        return gson.toJson(activeMusicians);
    }
}
