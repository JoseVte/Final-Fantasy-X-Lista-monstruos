package com.josrom.finalfantasyx_listamonstruos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josrom.finalfantasyx_listamonstruos.Interface;
import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.database.DBAdapter;
import com.josrom.finalfantasyx_listamonstruos.model.Zone;

import java.util.List;

/**
 * Created by Josrom on 04/06/2015.
 */
public class ListZonesAdapter extends RecyclerView.Adapter<ListZonesAdapter.RowHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private final List<Zone> zones;
    private final DBAdapter mDBHelper;
    private Interface.ClickListener clickListener;

    public ListZonesAdapter(Context context, List<Zone> zones, DBAdapter mDBHelper) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.zones = zones;
        this.mDBHelper = mDBHelper;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.zone_row, parent, false);
        return new RowHolder(v);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        holder.name.setText(zones.get(position).getName());
        holder.count.setText(String.valueOf(mDBHelper.countOfMonster(zones.get(position))) + "/" + String.valueOf(mDBHelper.countOfTotalMonster(zones.get(position))));
        holder.zone = zones.get(position);
    }

    @Override
    public int getItemCount() {
        return zones.size();
    }

    public void setClickListener(Interface.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public Interface.ClickListener getClickListener() {
        return clickListener;
    }

    public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView count;
        Zone zone;
        public RowHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_zone_name);
            count = (TextView) itemView.findViewById(R.id.list_zone_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }
}
