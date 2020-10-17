package com.bawp.bandme.util;


import android.annotation.SuppressLint;

import com.bawp.bandme.model.BandMeContact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Objects;

public class StringDateComparator implements Comparator<BandMeContact>
{
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public int compare(BandMeContact contact1, BandMeContact contact2) {
        try {
            return Objects.requireNonNull(dateFormat.parse(contact1.getLastUpdate())).compareTo(dateFormat.parse(contact2.getLastMessage()));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
