package org.unibl.etf.mr.assetbox.ui.employees;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.recyclerview.StringListRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


public class EmployeesFragment extends Fragment {

    View root;
    List<AssetInfo> assetInfoList;

    RecyclerView recyclerView;

    TextView emptyListMessage;

    public EmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetInfoList = AssetInfoListManager.getInstance().getAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_employees, container, false);
        Context context = root.getContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.employees_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new StringListRecyclerViewAdapter(assetInfoList.stream().map(AssetInfo::getEmployeeName).distinct().collect(Collectors.toList()), this::onEmployeeClick));

        emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (assetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    private void onEmployeeClick(View view, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assets", (Serializable) assetInfoList.stream().filter(a -> a.getEmployeeName().equals(name)).collect(Collectors.toList()));
        Navigation.findNavController(view).navigate(R.id.action_employeesFragment_to_navigation_assets, bundle);
    }
}