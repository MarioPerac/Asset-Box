package org.unibl.etf.mr.assetbox.ui.assets;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.recyclerview.AssetInfoRecyclerViewAdapter;
import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.SearchCategories;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssetsFragment extends Fragment {

    private List<AssetInfo> assetInfoList;
    private List<AssetInfo> filteredAssetInfoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssetDAO assetDAO;
    private AssetInfoRecyclerViewAdapter adapter;
    private Spinner searchCategorySpinner;
    private SearchView searchView;
    private String currentSearchCategory = SearchCategories.Name.toString();

    public AssetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetInfoList = (List<AssetInfo>) getArguments().getSerializable("assets");
        } else
            assetInfoList = AssetInfoListManager.getInstance().getAll();

        assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();
        filteredAssetInfoList.addAll(assetInfoList);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assets_list, container, false);

        recyclerView = view.findViewById(R.id.assets_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AssetInfoRecyclerViewAdapter(filteredAssetInfoList, this::onAssetClick);
        recyclerView.setAdapter(adapter);

        searchCategorySpinner = view.findViewById(R.id.search_category_spinner);
        searchView = view.findViewById(R.id.search_view);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchCategorySpinner.setAdapter(spinnerAdapter);
        searchCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSearchCategory = parent.getItemAtPosition(position).toString();
                filterAssets(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAssets(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAssets(newText);
                return false;
            }
        });

        TextView emptyListMessage = view.findViewById(R.id.emptyListMessage);
        if (filteredAssetInfoList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }

        return view;
    }


    private void onAssetClick(View view, AssetInfo assetInfo) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Asset asset = assetDAO.getById(assetInfo.getId());
            getActivity().runOnUiThread(() -> {
                Bundle bundle = new Bundle();

                bundle.putSerializable("asset", asset);
                Navigation.findNavController(view).navigate(R.id.action_navigation_assets_to_assetDetailsFragment, bundle);
            });
        });
    }

    private void filterAssets(String query) {
        filteredAssetInfoList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredAssetInfoList.addAll(assetInfoList);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            for (AssetInfo assetInfo : assetInfoList) {
                if (currentSearchCategory.equals(SearchCategories.Name.toString()) && assetInfo.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredAssetInfoList.add(assetInfo);
                } else if (currentSearchCategory.equals(SearchCategories.Date.toString())) {
                    String formattedDate = assetInfo.getCreationDate().format(formatter);
                    if (formattedDate.contains(query)) {
                        filteredAssetInfoList.add(assetInfo);
                    }

                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
