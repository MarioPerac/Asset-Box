package org.unibl.etf.mr.assetledger.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;

import java.util.List;

public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder> {

    private List<String> locations;

    public interface OnLocationClickListener {
        void onLocationClick(View view, String location);
    }

    private LocationsRecyclerViewAdapter.OnLocationClickListener listener;

    public LocationsRecyclerViewAdapter(List<String> locations, LocationsRecyclerViewAdapter.OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @Override
    public LocationsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_location, parent, false);
        return new LocationsRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final LocationsRecyclerViewAdapter.ViewHolder holder, int position) {
        final String location = locations.get(position);

        holder.location.setText(location);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLocationClick(v, location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView location;


        public ViewHolder(View view) {
            super(view);

            location = view.findViewById(R.id.location);

        }


    }
}