package com.justandrej.study.bullsandcows;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scoresDb";
    public static final String TABLE_SCORES = "scores";
    public static final String TABLE_TEMP = "scores_temp";
    public static final String TABLE_ACCESS = "table_access";

    public static final String KEY_ID = "_id";
    public static final String KEY_SCORE = "score";
    public static final String KEY_BOOLEAN = "istrue";

    private static final String TAG = "DbHelper";

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LAST_ACCESS = "last_access";


    private static DBHelper instance;



    public static DBHelper getInstance(Context context){
        if (null == instance){
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table " + TABLE_SCORES + "(" + KEY_ID + " integer primary key," + KEY_NAME + " text," + KEY_MAIL + " text" + ")");

        db.execSQL("create table " + TABLE_SCORES + "(" + KEY_ID + " integer primary key, " + KEY_SCORE + " integer" + ")");

    }


//    public static String getTableAsString(SQLiteDatabase db) {
//        Log.d(TAG, "getTableAsString called");
//        String tableString = String.format("Table %s:\n", TABLE_SCORES);
//        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_SCORES, null);
//        if (allRows.moveToFirst() ){
//            String[] columnNames = allRows.getColumnNames();
//            do {
//                for (String name: columnNames) {
//                    tableString += String.format("%s: %s\n", name,
//                            allRows.getString(allRows.getColumnIndex(name)));
//                }
//                tableString += "\n";
//
//            } while (allRows.moveToNext());
//        }
//
//        return tableString;
//    }

    public static void reset(Context context){
//        DBHelper nInstance = getInstance(context);
        SQLiteDatabase mdb = instance.getWritableDatabase();
        mdb.execSQL("DROP TABLE " + TABLE_SCORES);
        mdb.execSQL("create table " + TABLE_SCORES + "(" + KEY_ID + " integer primary key," + KEY_SCORE + " integer" + ")");

        SPHelper.setUpdate(context, true);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SCORES);

        onCreate(db);

    }
}