package com.bawp.bandme.util;

public class MyUtil {

    public interface KEYS{
        String ELECTRIC_GUITAR = "Guitar";
        String BASS_GUITAR = "Bass";
        String DJ = "DJ";
        String DRUMS = "Drums";
        String KEYBOARD = "Keyboard";
        String MICROPHONE = "Vocals";
        String FLUTE = "Flute";
        String MANDOLIN = "Mandolin";
        String VIOLIN = "Violin";
        String PERCUSSION = "Percussion";
        String PIANO = "Piano";
        String SAXOPHONE = "Saxophone";
        String BAND_ME_PROFILE = "BandMeProfile";

        //Chat related
        String SENDER = "sender";
        String RECEIVER = "receiver";
        String MESSAGE = "message";
    }

    public interface Arrays{

        String [] districtsForSpinner = {"Jerusalem", "Northern District", "Haifa District", "Central District", "Tel Aviv District",
                "Southern District", "Judea and Samaria Area", "Eilat"};

        String [] districts = {"All", "Jerusalem", "Northern District", "Haifa District", "Central District", "Tel Aviv District",
                "Southern District", "Judea and Samaria Area", "Eilat"};

        String [] instruments = {"All" ,"Guitar", "Bass", "DJ", "Drums", "Flute", "Keyboard", "Mandolin", "Vocals", "Percussion", "Piano",
                "Saxophone", "Violin", "Other"};
    }
}
