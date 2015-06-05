package com.josrom.finalfantasyx_listamonstruos.model;

/**
 * Created by Josrom on 04/06/2015.
 */
public class Zone {
    private String name;

    public Zone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface ZONE {
        String TABLE = "zones";
        String COLUMN_NAME = "name";
        String[] COLUMNS = {COLUMN_NAME};
    }
}
