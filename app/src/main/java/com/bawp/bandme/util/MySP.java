package com.bawp.bandme.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MySP {

    public interface KEYS{
        String ELECTRIC_GUITAR = "Guitar";
        String BASS_GUITAR = "Bass";
        String DJ = "DJ";
        String DRUMS = "Drums";
        String KEYBOARD = "Keyboard";
        String MICROPHONE = "Singer";
        String FLUTE = "Flute";
        String MANDOLIN = "Mandolin";
        String VIOLIN = "Violin";
        String PERCUSSION = "Percussion";
    }

    private static MySP instance;
    private SharedPreferences prefs;

    public static MySP getInstance(){
        return instance;
    }

    private MySP(Context context){
        prefs = context.getApplicationContext().getSharedPreferences("APP_DB", Context.MODE_PRIVATE);
    }

    public static MySP initHelper(Context context){
        if (instance == null)
            instance = new MySP(context);
        return instance;
    }

    //***************String*************************
    public void putString(String key, String value){
        prefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String def){
        return prefs.getString(key, def);
    }
}
