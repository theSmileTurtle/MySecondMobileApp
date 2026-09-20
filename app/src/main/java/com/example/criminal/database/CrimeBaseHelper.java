package com.example.criminal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// вспомогательный класс для работы с бд
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // версия базы
    private static final String DATABASE_NAME = "crimeBase.db"; // имя файла с базой

    // тут как я понял она и создается в базовом конструкторе
    // то есть вызывается onCreate если она не создана или загружается в противном случае
    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // sql запрос для создания таблицы в бд
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME + " ("
        + " _id integer primary key autoincrement, "
        + CrimeDbSchema.CrimeTable.Cols.UUID + ", "
        + CrimeDbSchema.CrimeTable.Cols.TITLE + ", "
        + CrimeDbSchema.CrimeTable.Cols.DATE + ", "
        + CrimeDbSchema.CrimeTable.Cols.SOLVED + ")");
    }

    // это если понадобится обновлять структуру бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
