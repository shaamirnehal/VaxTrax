package com.example.vaxtrax;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RVadapter.ViewHolder> implements Filterable {

    private ArrayList<Vaccines> data;
    private ArrayList<Vaccines> filteredData;
    private Context context;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    void setClickListener (ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                Log.i("TAG", "performFiltering: " + charString);
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    ArrayList<Vaccines> vacFilteredData = new ArrayList<>();
                    for (Vaccines vac : data) {
                        if (vac.getStage().toLowerCase().contains(charString.toLowerCase())) {
                            Log.i("TAG", "performFiltering: result found :)");
                            vacFilteredData.add(vac);
                        }
                    }
                    filteredData = vacFilteredData;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                Log.i("TAG", "FILTER RESULTS: " + filterResults);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<Vaccines>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public RVadapter(ArrayList<Vaccines> data, Context context) {
        this.data = data;
        this.filteredData = data;
        this.context = context;
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
        final Vaccines dataObj = (filteredData != null && !filteredData.isEmpty()) ? filteredData.get(position) : data.get(position);
        Log.i("onBindViewHolder", "onBindViewHolder: " + dataObj.getInfo());
        holder.nametv.setText(dataObj.getName());
        holder.stagetv.setText(dataObj.getStage());
        holder.typetv.setText(dataObj.getType());
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
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
