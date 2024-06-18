package org.unibl.etf.mr.assetbox.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetbox.R;

import java.util.List;

public class StringListRecyclerViewAdapter extends RecyclerView.Adapter<StringListRecyclerViewAdapter.ViewHolder> {

    private List<String> strings;

    public interface OnStringClickListener {
        void onStringClick(View view, String list);
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(View view, String string);
    }

    private OnStringClickListener onStringClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public StringListRecyclerViewAdapter(List<String> strings, OnStringClickListener listener) {
        this.strings = strings;
        this.onStringClickListener = listener;
    }

    public StringListRecyclerViewAdapter(List<String> strings, OnStringClickListener onStringClickListener, OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.strings = strings;
        this.onStringClickListener = onStringClickListener;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
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
                onStringClickListener.onStringClick(v, string);
            }
        });

        if (onDeleteButtonClickListener != null) {
            holder.buttonDelete.setOnClickListener((View v) -> onDeleteButtonClickListener.onDeleteButtonClick(v, string));
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public void updateData(List<String> newData) {
        this.strings = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView string;

        Button buttonDelete;
        Button buttonEdit;

        public ViewHolder(View view) {
            super(view);

            string = view.findViewById(R.id.name);
            buttonDelete = view.findViewById(R.id.buttonDelete);
            buttonEdit = view.findViewById(R.id.buttonEdit);

        }


    }
}