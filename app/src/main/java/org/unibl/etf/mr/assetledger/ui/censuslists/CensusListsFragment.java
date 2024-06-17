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
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.model.CensusList;
import org.unibl.etf.mr.assetledger.model.CensusListsManager;
import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.recyclerview.CensusListsRecyclerViewAdapter;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CensusListsFragment extends Fragment {

    View root;

    List<CensusList> lists;
    List<CensusList> filteredLists = new ArrayList<>();

    private RecyclerView recyclerView;

    private CensusListsRecyclerViewAdapter adapter;

    private Spinner searchCategorySpinner;
    private SearchView searchView;
    private String currentSearchCategory = "Name";

    public CensusListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = CensusListsManager.getInstance().getCensusLists();
        filteredLists.addAll(lists);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_census_lists, container, false);
        recyclerView = root.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CensusListsRecyclerViewAdapter(filteredLists, this::onListClick);
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
        if (filteredLists.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    private void onListClick(View view, CensusList censusList) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            List<Item> items = AssetDatabase.getInstance(getContext()).getItemDAO().getAllCensusListItems(censusList.getId());
            for (Item item : items)
                item.setAsset(AssetDatabase.getInstance(getContext()).getAssetDAO().getById(item.getAssetId()));
            getActivity().runOnUiThread(() -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("items", (Serializable) items);
                Navigation.findNavController(view).navigate(R.id.action_censusListsFragment_to_itemsFragment, bundle);
            });
        });
    }

    private void filterAssets(String query) {
        filteredLists.clear();
        if (TextUtils.isEmpty(query)) {
            filteredLists.addAll(lists);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            for (CensusList censusList : lists) {
                if (currentSearchCategory.equals("Name") && censusList.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredLists.add(censusList);
                } else if (currentSearchCategory.equals("Date")) {
                    String formattedDate = censusList.getCreationDate().format(formatter);
                    if (formattedDate.contains(query)) {
                        filteredLists.add(censusList);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}