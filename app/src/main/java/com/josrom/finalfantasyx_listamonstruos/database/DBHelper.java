package com.josrom.finalfantasyx_listamonstruos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.josrom.finalfantasyx_listamonstruos.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Josrom on 04/06/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

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

    }
}
