package com.josrom.finalfantasyx_listamonstruos.models;

/**
 * Created by Josrom on 04/06/2015.
 */
public class Monster {
    private long id;
    private String name;
    private Zone zone;
    private int count;

    public Monster(long id, String name, Zone zone, int count) {
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public interface MONSTER {
        String TABLE = "monsters";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "name";
        String COLUMN_ZONE = "zone";
        String COLUMN_COUNT = "count";
        String[] COLUMNS = {COLUMN_ID,COLUMN_NAME,COLUMN_ZONE,COLUMN_COUNT};
    }
}
