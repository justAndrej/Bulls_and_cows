package com.justandrej.study.bullsandcows;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import java.util.Arrays;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    private static final String HARD_LEVEL = "Hard Level";

    private int mHardLevel = 0, mAttempts = 0, mAttemptsUsed = 0, mBulls = 0, mCows = 0, mUserscore = 0;
    private boolean mIsInfinity = false;
    private int[] mRandomValue, mUserValue = new int[4];
    private TextView gameHint, mAttemptsLeft;
    private DBHelper mDBHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameHint = findViewById(R.id.game_hint);
        mAttemptsLeft = findViewById(R.id.attempts_left);
        mRandomValue = GetRandomValue();
        mDBHelper = DBHelper.getInstance(this);

        mHardLevel = getIntent().getIntExtra(HARD_LEVEL, 0);

        switch (mHardLevel){
            case 0: mIsInfinity = true;
            break;
            case 1: mAttempts = 30;
            break;
            case 2: mAttempts = 20;
            break;
            case 3: mAttempts = 10;
            break;
        }

        if(!mIsInfinity) {
            mAttemptsLeft.setText((getString(R.string.attempts_left) + " " + String.valueOf(mAttempts) + "."));
        }

        final PinEntryEditText pinEntry = findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
//                    if (str.toString().equals("1234")) {
//                        Toast.makeText(GameActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(GameActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
//                        pinEntry.setText(null);
//                    }
//                    String stval = String.valueOf(value);
//                    Toast.makeText(GameActivity.this, String.valueOf(mRandomValue), Toast.LENGTH_SHORT).show();

                    char[] mUserValueChar = str.toString().toCharArray();


                    for(int j = 0; j < mUserValueChar.length; j++){
                        mUserValue[j] = mUserValueChar[j] - '0';
                    }

                    if(!(mUserValue[0]==0||HasSameDigit(mUserValue))) {

                        if (!Arrays.equals(mUserValue, mRandomValue)) {

                            for (int i = 0; i < mRandomValue.length; i++) {
                                if (mUserValue[i] == mRandomValue[i]) {
                                    mBulls++;
                                } else {

                                    for (int aMRandomValue : mRandomValue) {
                                        if (mUserValue[i] == aMRandomValue) {
                                            mCows++;
                                        }
                                    }

                                }
                            }

                            gameHint.setText((str + " " + getString(R.string.contains) + " " + String.valueOf(mBulls) + " " + getString(R.string.bulls) + " " + String.valueOf(mCows) + " " + getString(R.string.cows)));
                            pinEntry.setText(null);
                            mBulls = mCows = 0;
                            if(!mIsInfinity){
                                mAttempts--;
                                mAttemptsUsed++;

                                if(mAttempts <= 0){

                                    Intent intent = MainActivity.newIntent(GameActivity.this, false, 0);
                                    startActivity(intent);
                                } else {
                                    mAttemptsLeft.setText((getString(R.string.attempts_left) + " " + String.valueOf(mAttempts) + "."));
                                }

                            }

                        } else {
                            if(!mIsInfinity) {
                                mUserscore = 30 - mAttemptsUsed;

                                //save score
                                SQLiteDatabase database = mDBHelper.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBHelper.KEY_SCORE, mUserscore);
                                database.insert(DBHelper.TABLE_SCORES, null, contentValues);
                                SPHelper.setUpdate(GameActivity.this, false);

                                //go to main activity
                                Intent intent = MainActivity.newIntent(GameActivity.this, true, mUserscore);
                                startActivity(intent);
                            } else {
                                Intent intent = MainActivity.newIntent(GameActivity.this, true, 0);
                                startActivity(intent);
                            }

                        }

                    } else if(mUserValue[0]==0){

                        gameHint.setText(R.string.start_null);
                        pinEntry.setText(null);

                    } else {
                        gameHint.setText(R.string.has_same_digit);
                        pinEntry.setText(null);
                    }

                }
            });
        }
    }


    public static Intent newIntent(Context packageContext, int hardlevel) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        intent.putExtra(HARD_LEVEL, hardlevel);
        return intent;
    }

    private static boolean HasSameDigit(int[] userInput){
        for(int i = 0; i < userInput.length; i++){
            for(int j = 0; j < userInput.length; j++){
                if(i != j){
                    if(userInput[i] == userInput[j]){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static int[] GetRandomValue(){
        int a; //from 1 to 9
        int b; // from 0 to 9
        int c; //...
        int d;


        Random rnd = new Random(System.currentTimeMillis());
        a = 1 + rnd.nextInt(9);
        do{
            b = rnd.nextInt(10);
        } while(b == a);

        do{
            c = rnd.nextInt(10);
        } while(c == a || c == b);

        do{
            d = rnd.nextInt(10);
        } while(d == a || d == b || d == c);

        return new int[]{a, b, c, d};
    }
}
