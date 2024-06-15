package org.unibl.etf.mr.assetledger.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;

import java.util.List;

public class EmployeesRecyclerViewAdapter extends RecyclerView.Adapter<EmployeesRecyclerViewAdapter.ViewHolder> {

    private List<AssetInfo> assetsInfo;

    public interface OnEmployeeClickListener {
        void onEmployeeClick(View view, AssetInfo assetInfo);
    }

    private OnEmployeeClickListener listener;

    public EmployeesRecyclerViewAdapter(List<AssetInfo> assetsInfo, OnEmployeeClickListener listener) {
        this.assetsInfo = assetsInfo;
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
        final AssetInfo assetInfo = assetsInfo.get(position);

        holder.employeeName.setText(assetInfo.getEmployeeName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEmployeeClick(v, assetInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView employeeName;


        public ViewHolder(View view) {
            super(view);

            employeeName = view.findViewById(R.id.employeeName);

        }


    }
}
