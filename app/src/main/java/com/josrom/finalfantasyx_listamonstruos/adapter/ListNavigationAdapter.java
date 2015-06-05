package com.josrom.finalfantasyx_listamonstruos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josrom.finalfantasyx_listamonstruos.Interface;
import com.josrom.finalfantasyx_listamonstruos.R;
import com.josrom.finalfantasyx_listamonstruos.model.Information;

import java.util.List;

public class ListNavigationAdapter extends RecyclerView.Adapter<ListNavigationAdapter.RowHolder>{
    private List<Information> data;
    private LayoutInflater inflater;
    private Interface.ClickListener clickListener;

    public ListNavigationAdapter(Context context, List<Information> array){
        inflater = LayoutInflater.from(context);
        data = array;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.nav_row, parent, false);
        return new RowHolder(v);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.icon.setImageResource(data.get(position).iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(Interface.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView icon;
        TextView name;
        public RowHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.list_nav_icon);
            name = (TextView) itemView.findViewById(R.id.list_nav_name);
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