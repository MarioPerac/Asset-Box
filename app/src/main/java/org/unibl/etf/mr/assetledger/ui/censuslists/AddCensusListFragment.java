package org.unibl.etf.mr.assetledger.ui.censuslists;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.AssetInfoListManager;
import org.unibl.etf.mr.assetledger.model.CensusList;
import org.unibl.etf.mr.assetledger.model.CensusListsManager;
import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.model.ItemListManager;
import org.unibl.etf.mr.assetledger.recyclerview.ItemsRecyclerViewAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCensusListFragment extends Fragment {


    View root;

    private CensusListsManager censusListsManager;

    private ItemListManager itemListManager;
    FloatingActionButton buttonAddItem;
    Button buttonSave;

    Button buttonCancel;

    EditText editTextCensusListName;

    private RecyclerView recyclerView;

    private ItemsRecyclerViewAdapter adapter;

    TextView emptyListMessage;


    public AddCensusListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
        censusListsManager = CensusListsManager.getInstance();
        itemListManager = ItemListManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_census_list, container, false);
        editTextCensusListName = root.findViewById(R.id.editTextListName);
        recyclerView = root.findViewById(R.id.list);
        buttonAddItem = root.findViewById(R.id.addItemButton);
        buttonSave = root.findViewById(R.id.buttonSave);
        buttonCancel = root.findViewById(R.id.buttonCancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemsRecyclerViewAdapter(itemListManager.getItems(), (view, item) -> {
        });
        recyclerView.setAdapter(adapter);


        buttonAddItem.setOnClickListener(this::onAddItemButtonClick);
        buttonSave.setOnClickListener(this::onSaveButtonClick);
        buttonCancel.setOnClickListener(this::onCancelButtonClick);
        emptyListMessage = root.findViewById(R.id.emptyListMessage);
        if (itemListManager.getItems().isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
        return root;
    }

    public void onAddItemButtonClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_addCensusListFragment_to_scanItemFragment);
    }

    public void onSaveButtonClick(View view) {

        String listName = editTextCensusListName.getText().toString();
        if (!listName.isEmpty()) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                CensusList censusList = new CensusList(0, listName, LocalDateTime.now());
                long censusId = AssetDatabase.getInstance(getContext()).getCensuslistDAO().insert(censusList);
                censusList.setId(censusId);
                List<Asset> changedAssets = new ArrayList<>();
                for (Item item : itemListManager.getItems()) {
                    item.setCensusId(censusId);
                    Asset asset = item.getAsset();

                    boolean locationChanged = !asset.getLocation().equals(item.getNewLocation());
                    boolean employeeNameChanged = !asset.getEmployeeName().equals(item.getNewEmployeeName());

                    if (locationChanged || employeeNameChanged) {
                        if (locationChanged) {
                            asset.setLocation(item.getNewLocation());
                        }

                        if (employeeNameChanged) {
                            asset.setEmployeeName(item.getNewEmployeeName());
                        }

                        changedAssets.add(asset);
                    }
                }
                if (!changedAssets.isEmpty()) {
                    AssetDatabase.getInstance(getContext()).getAssetDAO().update(changedAssets.toArray(new Asset[0]));
                    AssetInfoListManager.getInstance().updateAssetInfoList(changedAssets);
                }


                AssetDatabase.getInstance(getContext()).getItemDAO().insertAll(itemListManager.getItems().toArray(new Item[0]));
                itemListManager.clear();
                censusListsManager.addCensusList(censusList);
                getActivity().runOnUiThread(() -> {
                    Navigation.findNavController(view).navigateUp();
                });
            });
        } else {
            Toast.makeText(getContext(), "Missing list name", Toast.LENGTH_LONG).show();
        }
    }

    public void onCancelButtonClick(View view) {
        editTextCensusListName.setText("");
        itemListManager.clear();
        adapter.notifyDataSetChanged();
        if (itemListManager.getItems().isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }
    }

}