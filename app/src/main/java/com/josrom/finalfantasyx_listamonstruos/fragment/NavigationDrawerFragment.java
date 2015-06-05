package com.josrom.finalfantasyx_listamonstruos.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.josrom.finalfantasyx_listamonstruos.Interface;
import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.activity.ListZones;
import com.josrom.finalfantasyx_listamonstruos.activity.MainActivity;
import com.josrom.finalfantasyx_listamonstruos.adapter.ListNavigationAdapter;
import com.josrom.finalfantasyx_listamonstruos.model.Information;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements Interface.ClickListener {
    public static final String PREF_FILE_NAME = "preferences";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View contentView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.drawer_list);
        ListNavigationAdapter adapter = new ListNavigationAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_home_grey600_48dp,R.drawable.ic_country};
        String[] names = {"Home","Zones"};

        for (int i = 0; i < names.length && i < icons.length; i++){
            data.add(new Information(icons[i],names[i]));
        }

        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        contentView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                if (mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            //TODO Mirar esto porque no va bien
            //mDrawerLayout.openDrawer(contentView);
        }
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readToPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    public void itemClicked(View view, int position) {
        switch (position){
            case 0:
                if (view.getContext().getClass().getName().equals(MainActivity.class.getName())){
                    mDrawerLayout.closeDrawer(contentView);
                }else{
                    mDrawerLayout.closeDrawer(contentView);
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }
                break;
            case 1:
                if (view.getContext().getClass().getName().equals(ListZones.class.getName())){
                    mDrawerLayout.closeDrawer(contentView);
                }else{
                    mDrawerLayout.closeDrawer(contentView);
                    startActivity(new Intent(getActivity(),ListZones.class));
                }
                break;
            default:
                Toast.makeText(getActivity(), "No implementado", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}