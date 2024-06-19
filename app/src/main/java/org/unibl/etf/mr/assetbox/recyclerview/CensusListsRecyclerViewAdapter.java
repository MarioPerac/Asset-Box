package org.unibl.etf.mr.assetbox.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.CensusList;
import org.unibl.etf.mr.assetbox.util.Constants;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CensusListsRecyclerViewAdapter extends RecyclerView.Adapter<CensusListsRecyclerViewAdapter.ViewHolder> {

    List<CensusList> lists;

    private CensusListsRecyclerViewAdapter.OnListClickListener onListClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public interface OnListClickListener {
        void onListClick(View view, CensusList censusList);
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(View view, CensusList censusList);
    }

    public CensusListsRecyclerViewAdapter(List<CensusList> lists, OnListClickListener onListClickListener) {
        this.lists = lists;
        this.onListClickListener = onListClickListener;
    }

    public CensusListsRecyclerViewAdapter(List<CensusList> lists, OnListClickListener onListClickListener, OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.lists = lists;
        this.onListClickListener = onListClickListener;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.census_list, parent, false);
        return new CensusListsRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CensusListsRecyclerViewAdapter.ViewHolder holder, int position) {
        final CensusList censusList = lists.get(position);
        holder.name.setText(censusList.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        holder.creationDateTime.setText(censusList.getCreationDate().format(formatter));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListClickListener.onListClick(v, censusList);
            }
        });

        if (onDeleteButtonClickListener != null)
            holder.buttonDelete.setOnClickListener((View v) -> onDeleteButtonClickListener.onDeleteButtonClick(v, censusList));

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView creationDateTime;

        Button buttonDelete;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            creationDateTime = view.findViewById(R.id.date);
            buttonDelete = view.findViewById(R.id.buttonDelete);
        }


    }
}
