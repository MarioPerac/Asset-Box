package org.unibl.etf.mr.assetbox.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetbox.R;

import java.util.List;

public class StringListRecyclerViewAdapter extends RecyclerView.Adapter<StringListRecyclerViewAdapter.ViewHolder> {

    private List<String> strings;

    public interface OnStringClickListener {
        void onStringClick(View view, String list);
    }

    private OnStringClickListener listener;

    public StringListRecyclerViewAdapter(List<String> strings, OnStringClickListener listener) {
        this.strings = strings;
        this.listener = listener;
    }

    @Override
    public StringListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.string_item, parent, false);
        return new StringListRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final StringListRecyclerViewAdapter.ViewHolder holder, int position) {
        final String string = strings.get(position);

        holder.string.setText(string);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStringClick(v, string);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView string;


        public ViewHolder(View view) {
            super(view);

            string = view.findViewById(R.id.name);

        }


    }
}