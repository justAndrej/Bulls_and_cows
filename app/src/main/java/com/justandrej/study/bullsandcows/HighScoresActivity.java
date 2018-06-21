package com.justandrej.study.bullsandcows;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {
//    private ListView mScoreTable;
    private TextView mStringScores;
    private DBHelper mDBHelper;
    private Button mResetButton;
//    private String[] mRowsArray;
    private String IS_UPDATED_TAG = "Updated_database!";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores_activity);
//        mScoreTable = findViewById(R.id.scores_list);
        mStringScores = findViewById(R.id.string_scores);
        mStringScores.setMovementMethod(new ScrollingMovementMethod());
        mResetButton = findViewById(R.id.reset_button);
        mDBHelper = DBHelper.getInstance(this);


        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (!SPHelper.isUpdated(HighScoresActivity.this)) {

            Cursor cursor = db.rawQuery("SELECT DISTINCT " + DBHelper.KEY_SCORE + " FROM " + DBHelper.TABLE_SCORES + " ORDER BY " + DBHelper.KEY_SCORE + " DESC;", null);

            if (cursor.getCount() != 0) {

                cursor.moveToFirst();

                boolean cCheck;
                int winPlaces = 1;


//        mRowsArray = new String[cursor.getCount()];
//        int rowsIndex = 0;


                do {
                    if (winPlaces <= 3) {
                        Spannable spText = new SpannableString(String.valueOf(winPlaces) + ". " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SCORE)) + "\n");
                        spText.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mStringScores.append(spText);
                        winPlaces++;
                    } else {
                        mStringScores.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SCORE)) + "\n");

                    }
                    cCheck = cursor.moveToNext();
                } while (cCheck);

                //update the table
                db.execSQL("CREATE TABLE " + DBHelper.TABLE_TEMP + "(" + DBHelper.KEY_ID + " integer primary key, " + DBHelper.KEY_SCORE + " integer" + ")");
                ContentValues contentValues = new ContentValues();
                boolean cCheck2;
                cursor.moveToLast();

                do{
                    contentValues.put(DBHelper.KEY_SCORE, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_SCORE)));
                    db.insert(DBHelper.TABLE_TEMP, null, contentValues);
                    cCheck2 = cursor.moveToPrevious();
                } while(cCheck2);

                db.execSQL("DROP TABLE " + DBHelper.TABLE_SCORES);
                db.execSQL("ALTER TABLE " + DBHelper.TABLE_TEMP + " RENAME TO " + DBHelper.TABLE_SCORES + ";");
                SPHelper.setUpdate(HighScoresActivity.this , true);
                Log.d(IS_UPDATED_TAG, "database successfully updated");

            } else {
                mStringScores.setText(R.string.no_saved_scores);
            }

//            DBHelper.reset(HighScoresActivity.this);
//            db = mDBHelper.getWritableDatabase();

            cursor.close();
        } else {
            db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + DBHelper.KEY_SCORE + " FROM " + DBHelper.TABLE_SCORES + ";", null);
            if(cursor.getCount() != 0) {
                cursor.moveToLast();
                boolean cCheck;
                int winPlaces = 1;

                do {
                    if (winPlaces <= 3) {
                        Spannable spText = new SpannableString(String.valueOf(winPlaces) + ". " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SCORE)) + "\n");
                        spText.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mStringScores.append(spText);
                        winPlaces++;
                    } else {
                        mStringScores.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SCORE)) + "\n");

                    }
                    cCheck = cursor.moveToPrevious();
                } while (cCheck);
            } else {
                mStringScores.setText(R.string.no_saved_scores);
            }

            cursor.close();

        }

//        ArrayAdapter<String> adapter = new ArrayAdapter <>(HighScoresActivity.this, android.R.layout.simple_list_item_1, mRowsArray);
//        mScoreTable.setAdapter(adapter);

//        mStringScores.setText(DBHelper.getTableAsString(db));

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.reset(HighScoresActivity.this);
                mStringScores.setText(R.string.no_saved_scores);
            }
        });

    }


    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, HighScoresActivity.class);
    }
}
