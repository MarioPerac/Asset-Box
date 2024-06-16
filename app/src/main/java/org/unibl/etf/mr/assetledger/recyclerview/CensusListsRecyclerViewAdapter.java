package org.unibl.etf.mr.assetledger.recyclerview;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.CensusList;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CensusListsRecyclerViewAdapter extends RecyclerView.Adapter<CensusListsRecyclerViewAdapter.ViewHolder> {

    List<CensusList> lists;

    private CensusListsRecyclerViewAdapter.OnListClickListener listener;

    public interface OnListClickListener {
        void onListClick(View view, CensusList censusList);
    }

    public CensusListsRecyclerViewAdapter(List<CensusList> lists, OnListClickListener listener) {
        this.lists = lists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.census_list, parent, false);
        return new CensusListsRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CensusList censusList = lists.get(position);
        holder.name.setText(censusList.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        holder.creationDateTime.setText(censusList.getCreationDate().format(formatter));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onListClick(v, censusList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView creationDateTime;


        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            creationDateTime = view.findViewById(R.id.date);
        }


    }
}
