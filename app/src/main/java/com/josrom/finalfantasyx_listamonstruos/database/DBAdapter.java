package com.josrom.finalfantasyx_listamonstruos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josrom on 04/06/2015.
 */
public class DBAdapter {
    public static final String TAG = DBAdapter.class.getSimpleName();
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBAdapter(Context ctx) {
        mCtx = ctx;
    }

    public DBAdapter open() {
        mDBHelper = new DBHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public boolean isOpen() {
        return mDB.isOpen();
    }

    public void close() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }

    public List<Zone> fetchAllZones(){
        Cursor mCursor = mDB.query(Zone.ZONE.TABLE, Zone.ZONE.COLUMNS, null, null, null, null, null);
        List<Zone> list = new ArrayList<>();
        if (mCursor.moveToFirst()){
            do {
                list.add(new Zone(mCursor.getString(mCursor.getColumnIndex(Zone.ZONE.COLUMN_NAME))));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }

    public List<Zone> fetchQueryZones(String query){
        Cursor mCursor = mDB.query(Zone.ZONE.TABLE, Zone.ZONE.COLUMNS, Zone.ZONE.COLUMN_NAME + " like '%" + query + "%'", null, null, null, null);
        List<Zone> list = new ArrayList<>();
        if (mCursor.moveToFirst()){
            do {
                list.add(new Zone(mCursor.getString(mCursor.getColumnIndex(Zone.ZONE.COLUMN_NAME))));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }

    public List<Monster> fetchMonstersByZone(Zone zone) {
        Cursor mCursor = mDB.query(Monster.MONSTER.TABLE, Monster.MONSTER.COLUMNS, Monster.MONSTER.COLUMN_ZONE + " = ?",new String[]{zone.getName()},null,null,"ROWID DESC");
        List<Monster> list = new ArrayList<>();
        if(mCursor.moveToFirst()){
            do{
                list.add(new Monster(mCursor.getLong(mCursor.getColumnIndex(Monster.MONSTER.COLUMN_ID)),
                        mCursor.getString(mCursor.getColumnIndex(Monster.MONSTER.COLUMN_NAME)),
                        new Zone(mCursor.getString(mCursor.getColumnIndex(Monster.MONSTER.COLUMN_ZONE))),
                        mCursor.getInt(mCursor.getColumnIndex(Monster.MONSTER.COLUMN_COUNT))));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }

    public void existsTables() {
        boolean exist1,exist2,exist3;
        exist1 = exist2 = exist3 = true;
        Cursor cursor1 = mDB.rawQuery("SELECT * FROM " + Zone.ZONE.TABLE + ";", null);
        if (cursor1 == null) exist1 = false;
        else cursor1.close();

        Cursor cursor2 = mDB.rawQuery("SELECT * FROM " + Monster.MONSTER.TABLE + ";", null);
        if (cursor2 == null) exist2 = false;
        else cursor2.close();

        Cursor cursor3 = mDB.rawQuery("SELECT * FROM android_metadata;", null);
        if (cursor3 == null) exist3 = false;
        else cursor3.close();

        //Si no existe se crea
        if (!exist1 && !exist2 && !exist3) mDBHelper.onCreate(mDB);
            //Si alguna no existe se actualiza
        else if (!(exist1 && exist2 && exist3)) mDBHelper.onUpgrade(mDB,0,1);
    }

    public void existsData() {
        boolean exist1,exist2,exist3;
        exist1 = exist2 = exist3 = false;
        Cursor cursor1 = mDB.query(Zone.ZONE.TABLE, Zone.ZONE.COLUMNS, null, null, null, null, null);
        if (cursor1.getCount() > 0) exist1 = true;
        else cursor1.close();

        Cursor cursor2 = mDB.query(Monster.MONSTER.TABLE, Monster.MONSTER.COLUMNS, null, null, null, null, null);
        if (cursor2.getCount() > 0) exist2 = true;
        else cursor2.close();

        Cursor cursor3 = mDB.rawQuery("SELECT * FROM android_metadata;", null);
        if (cursor3.getCount() > 0) exist3 = true;
        else cursor3.close();

        if (!(exist1 && exist2 && exist3)) {
            deleteAllData();
            insertDefaultValues();
        }
    }

    private void insertDefaultValues() {
        InputStream is = mCtx.getResources().openRawResource(R.raw.inserts);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            while(br.ready()) {
                String insertStmt = br.readLine();
                mDB.execSQL(insertStmt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteAllData(){
        int rowsZones, rowsMonsters, rowsMetadata;
        mDB.delete("SQLITE_SEQUENCE","NAME = ?", new String[]{Zone.ZONE.TABLE});
        mDB.delete("SQLITE_SEQUENCE","NAME = ?", new String[]{Monster.MONSTER.TABLE});
        mDB.delete("SQLITE_SEQUENCE","NAME = ?", new String[]{"android_metadata"});
        Log.d(TAG, "Sequence of database deleted");

        rowsMonsters = mDB.delete(Monster.MONSTER.TABLE, null, null);
        Log.d(TAG, "Number of rows in " + Monster.MONSTER.TABLE + " deleted: " + rowsMonsters);
        rowsZones = mDB.delete(Zone.ZONE.TABLE, null, null);
        Log.d(TAG, "Number of rows in " + Zone.ZONE.TABLE + " deleted: " + rowsZones);
        rowsMetadata = mDB.delete("android_metadata", null, null);
        Log.d(TAG, "Number of rows in android_metadata deleted: " + rowsMetadata);

        return (rowsZones + rowsMetadata + rowsMonsters)> 0;
    }

    public int countOfMonster(Zone zone) {
        List<Monster> monsters = fetchMonstersByZone(zone);
        int sum = 0;
        for (Monster monster : monsters) {
            sum += monster.getCount();
        }
        return sum;
    }

    public int countOfTotalMonster(Zone zone){
        List<Monster> monsters = fetchMonstersByZone(zone);
        return monsters.size()*10;
    }

    public boolean setCountMonster(Monster monster) {
        ContentValues cv = new ContentValues();
        cv.put(Monster.MONSTER.COLUMN_ID,monster.getId());
        cv.put(Monster.MONSTER.COLUMN_NAME,monster.getName());
        cv.put(Monster.MONSTER.COLUMN_ZONE,monster.getZone().getName());
        cv.put(Monster.MONSTER.COLUMN_COUNT,monster.getCount());

        int rs = mDB.update(Monster.MONSTER.TABLE, cv, "_id = " + monster.getId(), null);
        return rs==1;
    }
}
