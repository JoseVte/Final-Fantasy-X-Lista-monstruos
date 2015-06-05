package com.josrom.finalfantasyx_listamonstruos.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.adapter.ListMonsterAdapter;
import com.josrom.finalfantasyx_listamonstruos.database.DBAdapter;
import com.josrom.finalfantasyx_listamonstruos.model.Monster;
import com.josrom.finalfantasyx_listamonstruos.model.Zone;

import java.util.List;

public class ListMonsterZone extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_monster_zone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(Zone.ZONE.COLUMN_NAME) != null) {
            DBAdapter mDBHelper = MainActivity.getDataBase();
            Zone zone = new Zone(extras.getString(Zone.ZONE.COLUMN_NAME));
            getSupportActionBar().setTitle("Monsters of " + zone.getName());
            List<Monster> listMonsters = mDBHelper.fetchMonstersByZone(zone);
            ListMonsterAdapter monsterAdapter = new ListMonsterAdapter(this, R.layout.monster_row, listMonsters);
            ListView mListView = (ListView) findViewById(R.id.list_monsters);
            mListView.setItemsCanFocus(false);
            mListView.setAdapter(monsterAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_monster_zone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
