package org.unibl.etf.mr.assetbox.ui.censuslists;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.unibl.etf.mr.assetbox.BuildConfig;
import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.model.ItemListManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AddItemFragment extends Fragment {
    private View root;
    private Asset asset;

    private ImageView image;

    private TextView name;
    private TextView creationDate;
    private TextView description;
    private TextView price;
    private TextView textViewLocation;

    private TextView textViewEmployeeName;

    private TextView barcode;

    private AutoCompleteTextView autoCompleteTextViewNewEmployeeName;
    private AutoCompleteTextView autoCompleteTextViewNewLocation;

    private Button buttonEdit;

    private ItemListManager itemListManager;
    private PlacesClient placesClient;


    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset = (Asset) getArguments().getSerializable("asset");
            itemListManager = ItemListManager.getInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_item, container, false);
        image = root.findViewById(R.id.imageViewAsset);
        name = root.findViewById(R.id.textViewName);
        creationDate = root.findViewById(R.id.textViewCreationDateTime);
        description = root.findViewById(R.id.textViewDescription);
        price = root.findViewById(R.id.textViewPrice);
        textViewLocation = root.findViewById(R.id.textViewLocation);
        textViewEmployeeName = root.findViewById(R.id.textViewEmployeeName);
        barcode = root.findViewById(R.id.textViewBarcode);
        autoCompleteTextViewNewEmployeeName = root.findViewById(R.id.autoCompleteTextViewNewEmployeeName);
        autoCompleteTextViewNewLocation = root.findViewById(R.id.autoCompleteTextViewNewLocation);
        buttonEdit = root.findViewById(R.id.buttonEdit);
        if (asset.getImagePath() != null && !asset.getImagePath().isEmpty())
            image.setImageURI(Uri.parse(asset.getImagePath()));
        else
            image.setImageResource(R.drawable.box_add_asset_icon);
        name.setText(asset.getName());
        creationDate.setText(asset.getCreationDate().toString());
        description.setText(asset.getDescription());
        price.setText(String.valueOf(asset.getPrice()));
        textViewLocation.setText(asset.getLocation());
        barcode.setText(String.valueOf(asset.getBarcode()));
        textViewEmployeeName.setText(asset.getEmployeeName());


        ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextViewNewLocation.setAdapter(adapterLocations);

        ArrayAdapter<String> adapterEmployeeNames = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextViewNewEmployeeName.setAdapter(adapterEmployeeNames);
        adapterEmployeeNames.addAll(AssetInfoListManager.getInstance().getAll().stream().map(AssetInfo::getEmployeeName).distinct().collect(Collectors.toList()));

        autoCompleteTextViewNewLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    getAutocompletePredictions(s.toString(), adapterLocations);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Places.initialize(getContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(getContext());

        buttonEdit.setOnClickListener(this::onEditButtonClick);

        return root;
    }

    public void onEditButtonClick(View view) {
        String newEmployeeName = autoCompleteTextViewNewEmployeeName.getText().toString().trim();
        String newLocation = autoCompleteTextViewNewLocation.getText().toString().trim();

        if (newEmployeeName.isEmpty()) {
            newEmployeeName = textViewEmployeeName.getText().toString();
        }
        if (newLocation.isEmpty()) {
            newLocation = textViewLocation.getText().toString();
        }

        Item item = new Item(0, asset.getId(), asset.getEmployeeName(), newEmployeeName, asset.getLocation(), newLocation, 0);
        item.setAsset(asset);

        ItemListManager.getInstance().addItem(item);
        Navigation.findNavController(view).navigateUp();
        Navigation.findNavController(view).navigateUp();
    }

    private void getAutocompletePredictions(String query, ArrayAdapter<String> adapter) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<String> predictionList = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                predictionList.add(prediction.getFullText(null).toString());
            }
            adapter.clear();
            adapter.addAll(predictionList);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
        });
    }

}