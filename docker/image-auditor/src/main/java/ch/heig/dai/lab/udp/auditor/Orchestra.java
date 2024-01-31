package ch.heig.dai.lab.udp.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class Orchestra {

    private final List<Musician> musicians = new ArrayList<>();
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


    private String getInstrumentFromSound(String sound){
        // Iterates each entry of hashmap
        for(Map.Entry<String, String> instrument : instrumentSounds.entrySet()){
            // Checks if the entry value is equal to the sound.
            if(Objects.equals(instrument.getValue(), sound)){
                // Returns the key of the sound value.
                return instrument.getKey().toLowerCase();
            }
        }

        return null;
    }

    public void addMusician(String message){
        Sound sound = gson.fromJson(message, Sound.class);

        Musician musician = new Musician(sound.getUuid(), getInstrumentFromSound(sound.getSound()));

        if(!musicians.contains(musician)){
            // Adds the musician
            musicians.add(musician);
        }else {
            for(var m : musicians){
                if(m.equals(musician)){
                    m.setLastActivity(System.currentTimeMillis());
                }
            }
        }
    }

    public String getActiveMusicians(){
        // Removes the musicians who are inactive for 5 seconds.
        musicians.removeIf(musician -> musician.getLastActivity() < System.currentTimeMillis() - 5000);

        return gson.toJson(musicians);
    }
}
