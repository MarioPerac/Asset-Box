package org.unibl.etf.mr.assetledger.ui.assets;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.recyclerview.AssetInfoRecyclerViewAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssetsFragment extends Fragment {


    List<AssetInfo> assetInfoList;
    RecyclerView recyclerView;
    AssetDAO assetDAO;

    TextView emptyListMessage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AssetsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();
        if (getArguments() != null) {
            assetInfoList = (List<AssetInfo>) getArguments().getSerializable("assets");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assets_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.assets_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new AssetInfoRecyclerViewAdapter(assetInfoList, this::onAssetClick));

        emptyListMessage = view.findViewById(R.id.emptyListMessage);
        FloatingActionButton addAssetButton = view.findViewById(R.id.addAssetButton);
        addAssetButton.setOnClickListener(this::onAddAssetClick);

        if (assetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return view;
    }


    public void onAddAssetClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_navigation_assets_to_addAssetFragment);
    }

    public void onAssetClick(View view, AssetInfo assetInfo) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Asset asset = assetDAO.getById(assetInfo.getId());


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("asset", asset);
                        Navigation.findNavController(view).navigate(R.id.action_navigation_assets_to_assetDetailsFragment, bundle);
                    }
                });
            }
        });

    }
}

