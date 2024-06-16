package org.unibl.etf.mr.assetledger.ui.censuslists;

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

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.CensusList;
import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.recyclerview.AssetInfoRecyclerViewAdapter;
import org.unibl.etf.mr.assetledger.recyclerview.StringListRecyclerViewAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CensusListsFragment extends Fragment {

    View root;

    List<Item> items;
    List<Item> filteredItems;

    private RecyclerView recyclerView;

    private StringListRecyclerViewAdapter adapter;

    private Spinner searchCategorySpinner;
    private SearchView searchView;
    private String currentSearchCategory = "Name";

    public CensusListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        items = CensusList.getInstance().getItems();
        filteredItems.addAll(items);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_census_lists, container, false);
        recyclerView = root.findViewById(R.id.census_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new StringListRecyclerViewAdapter(filteredItems, this::onListClick);
        recyclerView.setAdapter(adapter);

        searchCategorySpinner = root.findViewById(R.id.search_category_spinner);
        searchView = root.findViewById(R.id.search_view);

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

        TextView emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (filteredItems.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    private void onListClick(View view, AssetInfo assetInfo) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
//            Asset asset = assetDAO.getById(assetInfo.getId());
//            getActivity().runOnUiThread(() -> {
//                Bundle bundle = new Bundle();
//
//                bundle.putSerializable("asset", asset);
//                Navigation.findNavController(view).navigate(R.id.action_navigation_assets_to_assetDetailsFragment, bundle);
//            });
        });
    }

    private void filterAssets(String query) {
        filteredItems.clear();
        if (TextUtils.isEmpty(query)) {
            filteredItems.addAll(items);
//        } else {
//            for (Item item : items) {
//                if (currentSearchCategory.equals("Name") && assetInfo.getName().toLowerCase().contains(query.toLowerCase())) {
//                    filteredAssetInfoList.add(assetInfo);
//                } else if (currentSearchCategory.equals("Location") && assetInfo.getLocation().toLowerCase().contains(query.toLowerCase())) {
//                    filteredAssetInfoList.add(assetInfo);
//                }
//            }
//        }
            adapter.notifyDataSetChanged();
        }
    }
}