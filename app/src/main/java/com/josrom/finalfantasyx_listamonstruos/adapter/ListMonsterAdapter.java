package com.josrom.finalfantasyx_listamonstruos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.activity.ListMonsterZone;
import com.josrom.finalfantasyx_listamonstruos.activity.MainActivity;
import com.josrom.finalfantasyx_listamonstruos.model.Monster;

import java.util.List;

import at.markushi.ui.CircleButton;

/**
 * Created by Josrom on 05/06/2015.
 */
public class ListMonsterAdapter extends ArrayAdapter<Monster>{
    private final Context context;
    private final int layoutResourceId;
    private final List<Monster> data;

    public ListMonsterAdapter(Context context,  int layoutResourceId, List<Monster> listMonsters) {
        super(context,layoutResourceId,listMonsters);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = listMonsters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MonsterHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
            holder = new MonsterHolder();
            holder.textName = (TextView)row.findViewById(R.id.monster_name);
            holder.textCount = (TextView)row.findViewById(R.id.monster_count);
            holder.btnIncrease = (CircleButton)row.findViewById(R.id.buttonIncrease);
            holder.btnDecrease = (CircleButton)row.findViewById(R.id.buttonDecrease);
            row.setTag(holder);
        } else {
            holder = (MonsterHolder) row.getTag();
        }
        final Monster monster = data.get(position);
        holder.textName.setText(monster.getName());
        holder.textCount.setText(monster.getCount() + "/10");
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monster.getCount() < 10) {
                    monster.setCount(monster.getCount()+1);
                    if (MainActivity.getDataBase().setCountMonster(monster)) {
                        holder.textCount.setText(monster.getCount() + "/10");
                    }

                }
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monster.getCount() > 0) {
                    monster.setCount(monster.getCount()-1);
                    if (MainActivity.getDataBase().setCountMonster(monster)) {
                        holder.textCount.setText(monster.getCount() + "/10");
                    }
                }
            }
        });

        return row;
    }

    static class MonsterHolder{
        TextView textName;
        TextView textCount;
        CircleButton btnIncrease;
        CircleButton btnDecrease;
    }
}
