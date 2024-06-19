package org.unibl.etf.mr.assetbox.recyclerview;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.util.Constants;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {
    List<Item> items;

    private ItemsRecyclerViewAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, Item item);
    }

    public ItemsRecyclerViewAdapter(List<Item> items) {
        this.items = items;
    }

    public ItemsRecyclerViewAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item, parent, false);
        return new ItemsRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemsRecyclerViewAdapter.ViewHolder holder, int position) {
        final Item item = items.get(position);
        if (item.getAsset().getImagePath() != null && !item.getAsset().getImagePath().isEmpty())
            holder.assetImageView.setImageURI(Uri.parse(item.getAsset().getImagePath()));
        else
            holder.assetImageView.setImageResource(R.drawable.box_add_asset_icon);
        holder.assetName.setText(item.getAsset().getName());
        holder.oldAssetEmployeeName.setText(item.getCurrentEmployeeName());
        holder.newAssetEmployeeName.setText(item.getNewEmployeeName());
        holder.oldAssetLocation.setText(item.getCurrentLocation());
        holder.newAssetLocation.setText(item.getNewLocation());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        holder.assetCreationDateTime.setText(item.getAsset().getCreationDate().format(formatter));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView assetImageView;
        TextView assetName;
        TextView oldAssetEmployeeName;
        TextView newAssetEmployeeName;
        TextView oldAssetLocation;
        TextView newAssetLocation;
        TextView assetCreationDateTime;


        public ViewHolder(View view) {
            super(view);
            assetImageView = itemView.findViewById(R.id.assetImageView);
            assetName = itemView.findViewById(R.id.assetName);
            oldAssetEmployeeName = itemView.findViewById(R.id.oldAssetEmployeeName);
            newAssetEmployeeName = itemView.findViewById(R.id.newAssetEmployeeName);
            oldAssetLocation = itemView.findViewById(R.id.oldAssetLocation);
            newAssetLocation = itemView.findViewById(R.id.newAssetLocation);
            assetCreationDateTime = itemView.findViewById(R.id.assetCreationDateTime);
        }


    }
}
