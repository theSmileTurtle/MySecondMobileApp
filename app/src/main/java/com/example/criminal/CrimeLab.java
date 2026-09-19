package com.example.criminal;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private final String TAG = "CrimeLab";
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if(sCrimeLab==null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();

        for(int i=0; i<100; i++) {
            Crime crime = new Crime();
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i%3 == 0);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getmCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime crime : mCrimes) {
            if(crime.getmId().equals(id)) {
                Log.d(TAG, "Crime was found");
                return crime;
            }
        }
        Log.e(TAG, "It is fuck up!");
        return null;
    }
}
