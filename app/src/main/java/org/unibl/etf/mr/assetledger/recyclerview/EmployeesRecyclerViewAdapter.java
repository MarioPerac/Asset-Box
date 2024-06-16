package org.unibl.etf.mr.assetledger.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;

import java.util.List;

public class EmployeesRecyclerViewAdapter extends RecyclerView.Adapter<EmployeesRecyclerViewAdapter.ViewHolder> {

    private List<String> names;

    public interface OnEmployeeClickListener {
        void onEmployeeClick(View view, String name);
    }

    private OnEmployeeClickListener listener;

    public EmployeesRecyclerViewAdapter(List<String> names, OnEmployeeClickListener listener) {
        this.names = names;
        this.listener = listener;
    }

    @Override
    public EmployeesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.employee, parent, false);
        return new EmployeesRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final EmployeesRecyclerViewAdapter.ViewHolder holder, int position) {
        final String name = names.get(position);

        holder.employeeName.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEmployeeClick(v, name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView employeeName;


        public ViewHolder(View view) {
            super(view);

            employeeName = view.findViewById(R.id.employeeName);

        }


    }
}
