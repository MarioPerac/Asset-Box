package org.unibl.etf.mr.assetledger.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;

import java.util.List;

public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder> {

    private List<AssetInfo> assetsInfo;

    public interface OnLocationClickListener {
        void onLocationClick(View view, AssetInfo assetInfo);
    }

    private LocationsRecyclerViewAdapter.OnLocationClickListener listener;

    public LocationsRecyclerViewAdapter(List<AssetInfo> assetsInfo, LocationsRecyclerViewAdapter.OnLocationClickListener listener) {
        this.assetsInfo = assetsInfo;
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
        final AssetInfo assetInfo = assetsInfo.get(position);

        holder.assetLocation.setText(assetInfo.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLocationClick(v, assetInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView assetLocation;


        public ViewHolder(View view) {
            super(view);

            assetLocation = view.findViewById(R.id.location);

        }


    }
}