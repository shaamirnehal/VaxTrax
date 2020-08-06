package com.example.vaxtrax;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {

    private ArrayList<CountriesModel> data;
    private ArrayList<CountriesModel> filteredData;
    private LayoutInflater inflater;
    private Context context;

    public CountriesAdapter(ArrayList<CountriesModel> data, Context context) {
        this.data = data;
        this.filteredData = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

//    public void setCountriesList (Context context, final ArrayList<CountriesModel> data) {
//        this.context = context;
//        if (this.data == null) {
//            this.data = data;
//            this.filteredData = data;
//            notifyItemChanged(0, filteredData.size());
//        } else {
//            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//                @Override
//                public int getOldListSize() {
//                    return CountriesAdapter.this.data.size();
//                }
//
//                @Override
//                public int getNewListSize() {
//                    return data.size();
//                }
//
//                @Override
//                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                    return CountriesAdapter.this.data.get(oldItemPosition).getCountry() == data.get(newItemPosition).getCountry();
//                }
//
//                @Override
//                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                    CountriesModel newCountries = CountriesAdapter.this.data.get(oldItemPosition);
//                    CountriesModel oldCountries = data.get(newItemPosition);
//                    return newCountries.getCountry() == oldCountries.getCountry();
//                }
//            });
//            this.data = data;
//            this.filteredData = data;
//            result.dispatchUpdatesTo(this);
//        }
//    }

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
                    ArrayList<CountriesModel> countriesFilteredData = new ArrayList<>();
                    for (CountriesModel country : data) {
                        if (country.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                            Log.i("TAG", "performFiltering: result found :)");
                            countriesFilteredData.add(country);
                        }
                    }
                    filteredData = countriesFilteredData;
                }
                Log.i("TAG", "FILTERED DATA: " + filteredData.get(0).getCountry());

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                Log.i("TAG", "FILTER RESULTS: " + filterResults);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<CountriesModel>) results.values;
                Log.i("TAG", "publishResults: " + filteredData);
                Log.i("TAG", "publishResults: " + filteredData.get(0).getCountry());
                notifyDataSetChanged();
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
