package org.unibl.etf.mr.assetbox.ui.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.recyclerview.StringListRecyclerViewAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class LocationsFragment extends Fragment {

    View root;
    List<AssetInfo> assetInfoList;

    RecyclerView recyclerView;

    StringListRecyclerViewAdapter adapter;
    TextView emptyListMessage;

    public LocationsFragment() {
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
        root = inflater.inflate(R.layout.fragment_locations, container, false);

        Context context = root.getContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.locations_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StringListRecyclerViewAdapter(assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList()), this::onLocationClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (assetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    private void onDeleteClick(View view, String name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this location?\nNote: All associated assets will be deleted.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLocation(name);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void onLocationClick(View view, String location) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assets", (Serializable) assetInfoList.stream().filter(a -> a.getLocation().equals(location)).collect(Collectors.toList()));
        Navigation.findNavController(view).navigate(R.id.action_locationsFragment_to_navigation_assets, bundle);
    }

    private void deleteLocation(String name) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AssetDAO assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();
                deleteImages(assetDAO.getAllAssetsImagePathsByLocation(name));
                assetDAO.deleteAllAssetsByLocation(name);

                getActivity().runOnUiThread(() -> {
                    AssetInfoListManager.getInstance().deleteByLocation(name);
                    assetInfoList = AssetInfoListManager.getInstance().getAll();
                    setEmptyListMessage();
                    adapter.updateData(assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList()));
                });
            }
        });
    }

    private void deleteImages(List<String> imagePathList) {

        for (String imagePath : imagePathList) {
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
            }
        }
    }

    private void setEmptyListMessage() {
        emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (assetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
    }
}