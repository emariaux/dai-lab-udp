package ch.heig.dai.udp.musician;

public enum Instrument {
    PIANO("ti-ta-ti"),
    TRUMPET("pouet"),
    FLUTE("trulu"),
    VIOLIN("gzi-gzi"),
    DRUM("boum-boum");

    private final String sound;
    Instrument(String sound) {
        this.sound = sound;
    }
    public String getSound() {
        return sound;
    }

}
