package org.unibl.etf.mr.assetbox.recyclerview;


import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.AssetInfo;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AssetInfoRecyclerViewAdapter extends RecyclerView.Adapter<AssetInfoRecyclerViewAdapter.ViewHolder> {

    private List<AssetInfo> assetsInfo;

    private OnAssetClickListener listener;

    public interface OnAssetClickListener {
        void onAssetClick(View view, AssetInfo assetInfo);
    }


    public AssetInfoRecyclerViewAdapter(List<AssetInfo> assetsInfo, OnAssetClickListener listener) {
        this.assetsInfo = assetsInfo;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.asset, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final AssetInfoRecyclerViewAdapter.ViewHolder holder, int position) {
        final AssetInfo assetInfo = assetsInfo.get(position);
        holder.assetName.setText(assetInfo.getName());
        if (assetInfo.getImagePath() != null && !assetInfo.getImagePath().isEmpty())
            holder.assetIcon.setImageURI(Uri.parse(assetInfo.getImagePath()));
        else
            holder.assetIcon.setImageResource(R.drawable.box_add_asset_icon);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        holder.assetCreationDateTime.setText(assetInfo.getCreationDate().format(formatter));

        holder.assetEmployeeName.setText(assetInfo.getEmployeeName());
        holder.assetLocation.setText(assetInfo.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAssetClick(v, assetInfo);
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