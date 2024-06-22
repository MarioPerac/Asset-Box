package org.unibl.etf.mr.assetbox.ui.censuslists;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
import org.unibl.etf.mr.assetbox.model.CensusList;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.recyclerview.ItemsRecyclerViewAdapter;
import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.SearchCategories;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    private View root;

    private List<Item> items;

    private List<Item> filteredItems = new ArrayList<>();

    private ItemsRecyclerViewAdapter adapter;

    private SearchView searchView;
    private String currentSearchCategory = SearchCategories.Name.toString();

    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (List<Item>) getArguments().getSerializable("items");
            filteredItems.addAll(items);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_items, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemsRecyclerViewAdapter(items, this::onItemClick);
        recyclerView.setAdapter(adapter);

        Spinner searchCategorySpinner = root.findViewById(R.id.search_category_spinner);
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

    private void onItemClick(View view, Item item) {

    }

    private void filterAssets(String query) {
        filteredItems.clear();
        if (TextUtils.isEmpty(query)) {
            filteredItems.addAll(items);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            Context context = getContext();


            SearchCategories searchCategory = SearchCategories.fromString(currentSearchCategory, context);

            for (Item item : items) {
                switch (searchCategory) {
                    case Name:
                        if (item.getAsset().getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredItems.add(item);
                        }
                        break;
                    case Date:
                        String formattedDate = item.getAsset().getCreationDate().format(formatter);
                        if (formattedDate.contains(query)) {
                            filteredItems.add(item);
                        }
                        break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}