package com.example.vaxtrax;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RVadapter.ViewHolder> {

    private ArrayList<Vaccines> data;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    void setClickListener (ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public RVadapter(ArrayList<Vaccines> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RVadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVadapter.ViewHolder holder, int position) {
        Vaccines dataObj = data.get(position);
        Log.i("onBindViewHolder", "onBindViewHolder: " + dataObj.getInfo());
        holder.nametv.setText(dataObj.getName());
        holder.stagetv.setText(dataObj.getStage());
        holder.typetv.setText(dataObj.getType());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nametv;
        TextView stagetv;
        TextView typetv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.nametv);
            stagetv = itemView.findViewById(R.id.stagetv);
            typetv = itemView.findViewById(R.id.typetv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
