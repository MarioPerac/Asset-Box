package org.unibl.etf.mr.assetbox.ui.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.recyclerview.StringListRecyclerViewAdapter;
import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.SearchCategories;

import java.io.File;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class LocationsFragment extends Fragment {

    View root;
    List<AssetInfo> assetInfoList;

    RecyclerView recyclerView;

    List<String> filteredList = new ArrayList<>();

    StringListRecyclerViewAdapter adapter;
    TextView emptyListMessage;
    private SearchView searchView;
    private String currentSearchCategory = SearchCategories.Name.toString();


    public LocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetInfoList = AssetInfoListManager.getInstance().getAll();
        filteredList.addAll(assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_locations, container, false);

        Context context = root.getContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.locations_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StringListRecyclerViewAdapter(filteredList, this::onLocationClick, this::onDeleteClick);

        Spinner searchCategorySpinner = root.findViewById(R.id.search_category_spinner);
        searchView = root.findViewById(R.id.search_view);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_category_name, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchCategorySpinner.setAdapter(spinnerAdapter);
        searchCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSearchCategory = parent.getItemAtPosition(position).toString();
                filterLocations(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterLocations(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLocations(newText);
                return false;
            }
        });
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
        builder.setTitle(R.string.confirm_deletion);
        builder.setMessage(R.string.confirm_delete_location);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLocation(name);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    private void filterLocations(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList()));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            Context context = getContext();


            SearchCategories searchCategory = SearchCategories.fromString(currentSearchCategory, context);

            for (String name : assetInfoList.stream().map(AssetInfo::getLocation).distinct().collect(Collectors.toList())) {
                if (searchCategory == SearchCategories.Name) {
                    if (name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(name);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}