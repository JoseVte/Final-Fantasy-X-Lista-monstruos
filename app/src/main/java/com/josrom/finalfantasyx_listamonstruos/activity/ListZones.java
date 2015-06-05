package com.josrom.finalfantasyx_listamonstruos.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.josrom.finalfantasyx_listamonstruos.Interface;
import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.adapter.ListZonesAdapter;
import com.josrom.finalfantasyx_listamonstruos.database.DBAdapter;
import com.josrom.finalfantasyx_listamonstruos.fragment.NavigationDrawerFragment;
import com.josrom.finalfantasyx_listamonstruos.model.Zone;

import java.util.List;

public class ListZones extends ActionBarActivity {

    private RecyclerView listZones;
    private DBAdapter mDBHelper;
    private static ListZones mApp;
    private ListZonesAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_zones);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        listZones = (RecyclerView) findViewById(R.id.list_zones);
        listZones.setLayoutManager(new LinearLayoutManager(this));
        listZones.setHasFixedSize(true);

        mDBHelper = MainActivity.getDataBase();
        listAllZones();

        mApp = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_zones, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView mySearch = new SearchView(this);

        // Assumes current activity is the searchable activity
        mySearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Actions for search
        mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mySearch.clearFocus();
                listQueryZones(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listQueryZones(s);
                return false;
            }
        });

        menu.findItem(R.id.action_search).setActionView(mySearch);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listAllZones(){
        final List<Zone> zones = mDBHelper.fetchAllZones();

        dataAdapter = new ListZonesAdapter(this, zones, mDBHelper);
        dataAdapter.setClickListener(new Interface.ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Zone zone = zones.get(position);
                Toast.makeText(getApplicationContext(), zone.getName(), Toast.LENGTH_SHORT).show();

                openActivityMonstersFromZone(view, zone);
            }
        });
        if(listZones.getAdapter() != null){
            listZones.swapAdapter(dataAdapter, false);
        } else {
            listZones.setAdapter(dataAdapter);
        }
    }

    public void listQueryZones(String query){
        final List<Zone> zones = mDBHelper.fetchQueryZones(query);

        dataAdapter = new ListZonesAdapter(this, zones, mDBHelper);
        dataAdapter.setClickListener(new Interface.ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Zone zone = zones.get(position);
                Toast.makeText(getApplicationContext(), zone.getName(), Toast.LENGTH_SHORT).show();

                openActivityMonstersFromZone(view, zone);
            }
        });
        listZones.swapAdapter(dataAdapter, false);
    }

    public static Context getContext(){ return mApp.getApplicationContext(); }

    private void openActivityMonstersFromZone(View view, Zone zone) {
        Intent i = new Intent(this, ListMonsterZone.class);
        Bundle extras = new Bundle();
        extras.putString(Zone.ZONE.COLUMN_NAME, zone.getName());
        i.putExtras(extras);
        startActivity(i);
    }
}
