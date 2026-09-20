package com.example.criminal.database;

// класс показывающий как выглядит структура базы данных
public class CrimeDbSchema {
    // класс показывающий структуру таблицы
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        // класс описывающий столбцы таблицы
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
