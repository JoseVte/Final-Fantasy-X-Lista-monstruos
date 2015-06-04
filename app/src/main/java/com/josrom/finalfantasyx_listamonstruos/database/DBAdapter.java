package com.josrom.finalfantasyx_listamonstruos.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.josrom.finalfantasyx_listamonstruos.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josrom on 04/06/2015.
 */
public class DBAdapter {
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

}
