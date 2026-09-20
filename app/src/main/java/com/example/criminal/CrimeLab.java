package com.example.criminal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.criminal.database.CrimeBaseHelper;
import com.example.criminal.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private final String TAG = "CrimeLab";
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDataBase;
    private Cursor cursor;

    public static CrimeLab get(Context context) {
        if(sCrimeLab==null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c) {
        //mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDataBase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);
        mDataBase.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",
                new String[] {uuidString});
    }

    private Cursor queryCrimes(String whereClause,
                               String[] whereArgs) {
        cursor = mDataBase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //mCrimes = new ArrayList<>();

//        for(int i=0; i<100; i++) {
//            Crime crime = new Crime();
//            crime.setmTitle("Crime #" + i);
//            crime.setmSolved(i%3 == 0);
//            mCrimes.add(crime);
//        }
    }

    public Crime getCrime() {
        String uuidString = cursor.getString(
                cursor.getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = cursor.getString(
                cursor.getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = cursor.getLong(
                cursor.getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = cursor.getInt(
                cursor.getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setmTitle(title);
        crime.setmDate(new Date(date));
        crime.setmSolved(isSolved!=0);
        return crime;
    }

    public List<Crime> getmCrimes() {
        //return mCrimes;

        List<Crime> crimes = new ArrayList<>();
        cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return getCrime();
        } finally {
            cursor.close();
        }

//        for(Crime crime : mCrimes) {
//            if(crime.getmId().equals(id)) {
//                Log.d(TAG, "Crime was found");
//                return crime;
//            }
//        }
//        Log.e(TAG, "It is fuck up!");
//        return null;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getmId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getmDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);
        return values;
    }
}
