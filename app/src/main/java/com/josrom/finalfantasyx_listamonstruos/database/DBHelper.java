package com.josrom.finalfantasyx_listamonstruos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.model.Monster;
import com.josrom.finalfantasyx_listamonstruos.model.Zone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Josrom on 04/06/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String TAG = DBHelper.class.getSimpleName();
    final static int DB_VERSION = 1;
    final static String DB_NAME = "ffx.s3db";
    Context ctx;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream is = ctx.getResources().openRawResource(R.raw.create);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            while(br.ready()) {
                String createStmt = br.readLine();
                db.execSQL(createStmt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        is = ctx.getResources().openRawResource(R.raw.inserts);
        br = new BufferedReader(new InputStreamReader(is));

        try {
            while(br.ready()) {
                String insertStmt = br.readLine();
                db.execSQL(insertStmt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + " , which will destroy all old data");
        db.execSQL("DROP TABLE if exists " + Zone.ZONE.TABLE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Zone.ZONE.TABLE+ "';");
        db.execSQL("DROP TABLE if exists " + Monster.MONSTER.TABLE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Monster.MONSTER.TABLE+ "';");
        db.execSQL("DROP TABLE if exists android_metadata");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'android_metadata';");
    }
}
