package com.example.vaxtrax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {

    private ArrayList<CountriesModel> data;
    private ArrayList<CountriesModel> filteredData;
    private LayoutInflater inflater;
    private Context context;

    /**
     * constructor
     * @param data list of countries to be rendered on recycler view
     * @param context parent activity context
     */
    public CountriesAdapter(ArrayList<CountriesModel> data, Context context) {
        this.data = data;
        this.filteredData = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stats_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesAdapter.ViewHolder holder, int position) {
            final CountriesModel dataObj = (filteredData != null && !filteredData.isEmpty()) ? filteredData.get(position) : data.get(position);
            holder.country.setText(dataObj.getCountry());
            holder.nctv.setText(String.valueOf(dataObj.getStatsModel().getNewConfirmed()));
            holder.tctv.setText(String.valueOf(dataObj.getStatsModel().getTotalConfirmed()));
            holder.ndtv.setText(String.valueOf(dataObj.getStatsModel().getNewDeaths()));
            holder.tdtv.setText(String.valueOf(dataObj.getStatsModel().getTotalDeaths()));
            holder.nrtv.setText(String.valueOf(dataObj.getStatsModel().getNewRecovered()));
            holder.trtv.setText(String.valueOf(dataObj.getStatsModel().getTotalRecovered()));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    /**
     * implements filterable functions for search view
     * performfiltering get query to check against data
     */

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    ArrayList<CountriesModel> countriesFilteredData = new ArrayList<>();
                    for (CountriesModel country : data) {
                        // make sure cases are not an issue and chars are compared not strings
                        if (country.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                            countriesFilteredData.add(country);
                        }
                    }
                    filteredData = countriesFilteredData;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<CountriesModel>) results.values;
                notifyDataSetChanged();
                // notify adapter of data set change
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView country;
        TextView nctv;
        TextView tctv;
        TextView ndtv;
        TextView tdtv;
        TextView nrtv;
        TextView trtv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country);
            nctv = itemView.findViewById(R.id.nctv);
            tctv = itemView.findViewById(R.id.tctv);
            ndtv = itemView.findViewById(R.id.ndtv);
            tdtv = itemView.findViewById(R.id.tdtv);
            nrtv = itemView.findViewById(R.id.nrtv);
            trtv = itemView.findViewById(R.id.trtv);
        }
    }
}
