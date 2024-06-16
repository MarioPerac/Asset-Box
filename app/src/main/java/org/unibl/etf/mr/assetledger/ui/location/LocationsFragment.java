package org.unibl.etf.mr.assetledger.ui.location;

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

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.AssetInfos;
import org.unibl.etf.mr.assetledger.recyclerview.StringListRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


public class LocationsFragment extends Fragment {

    View root;
    List<AssetInfo> assetInfoList;

    RecyclerView recyclerView;

    TextView emptyListMessage;

    public LocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetInfoList = AssetInfos.getInstance().getAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_locations, container, false);

        Context context = root.getContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.locations_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new StringListRecyclerViewAdapter(assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList()), this::onLocationClick));

        emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (assetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    private void onLocationClick(View view, String location) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assets", (Serializable) assetInfoList.stream().filter(a -> a.getLocation().equals(location)).collect(Collectors.toList()));
        Navigation.findNavController(view).navigate(R.id.action_locationsFragment_to_navigation_assets, bundle);
    }
}