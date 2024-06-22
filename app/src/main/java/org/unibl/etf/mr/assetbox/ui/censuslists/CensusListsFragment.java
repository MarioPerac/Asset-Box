package org.unibl.etf.mr.assetbox.ui.censuslists;

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
import org.unibl.etf.mr.assetbox.assetsdb.dao.CensusListDAO;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.CensusList;
import org.unibl.etf.mr.assetbox.model.CensusListsManager;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.recyclerview.CensusListsRecyclerViewAdapter;
import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.SearchCategories;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CensusListsFragment extends Fragment {

    private View root;

    private List<CensusList> lists;
    private List<CensusList> filteredLists = new ArrayList<>();

    private CensusListsRecyclerViewAdapter adapter;

    private SearchView searchView;
    private String currentSearchCategory = SearchCategories.Name.toString();

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
        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CensusListsRecyclerViewAdapter(filteredLists, this::onListClick, this::onDeleteButtonClick);
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            Context context = getContext();


            SearchCategories searchCategory = SearchCategories.fromString(currentSearchCategory, context);

            for (CensusList censusList : lists) {
                switch (searchCategory) {
                    case Name:
                        if (censusList.getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredLists.add(censusList);
                        }
                        break;
                    case Date:
                        String formattedDate = censusList.getCreationDate().format(formatter);
                        if (formattedDate.contains(query)) {
                            filteredLists.add(censusList);
                        }
                        break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onDeleteButtonClick(View view, CensusList censusList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.confirm_deletion);
        builder.setMessage(R.string.are_you_sure_delete_census_list);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCensusList(censusList);
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

    private void deleteCensusList(CensusList censusList) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                CensusListDAO censusListDAO = AssetDatabase.getInstance(getContext()).getCensuslistDAO();
                censusListDAO.delete(censusList);

                getActivity().runOnUiThread(() -> {
                    CensusListsManager.getInstance().removeCensusList(censusList);
                    filteredLists.remove(censusList);
                    adapter.notifyDataSetChanged();
                    setEmptyListMessage();
                });
            }
        });
    }

    private void setEmptyListMessage() {
        TextView emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (filteredLists.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
    }
}