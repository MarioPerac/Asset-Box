package org.unibl.etf.mr.assetledger.recyclerview;


import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AssetInfoRecyclerViewAdapter extends RecyclerView.Adapter<AssetInfoRecyclerViewAdapter.ViewHolder> {

    private List<AssetInfo> assetsInfo;
    public interface OnAssetClickListener {
        void onAssetClick(AssetInfo assetInfo);
    }
    private OnAssetClickListener listener;

    public AssetInfoRecyclerViewAdapter(List<AssetInfo> assetsInfo, OnAssetClickListener listener) {
        this.assetsInfo = assetsInfo;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_asset, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AssetInfo assetInfo = assetsInfo.get(position);
        holder.assetName.setText(assetInfo.getName());
//        holder.assetIcon.setImageResource(); // potrebno prikazati i sliku

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        holder.assetCreationDateTime.setText(assetInfo.getCreationDate().format(formatter));

        holder.assetEmployeeName.setText(assetInfo.getEmployeeName());
        holder.assetLocation.setText(assetInfo.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onAssetClick(assetInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView assetIcon;
        TextView assetName;
        TextView assetLocation;
        TextView assetCreationDateTime;

        TextView assetEmployeeName;

        public ViewHolder(View view) {
            super(view);

            assetIcon = view.findViewById(R.id.assetImageView);
            assetName = view.findViewById(R.id.assetName);
            assetLocation = view.findViewById(R.id.assetLocation);
            assetCreationDateTime = view.findViewById(R.id.assetCreationDateTime);
            assetEmployeeName = view.findViewById(R.id.assetEmployeeName);
        }


    }
}