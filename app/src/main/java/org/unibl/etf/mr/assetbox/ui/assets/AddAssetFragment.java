package org.unibl.etf.mr.assetbox.ui.assets;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.unibl.etf.mr.assetbox.BuildConfig;
import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import android.widget.Toast;


public class AddAssetFragment extends Fragment {

    ImageView assetImage;

    Button buttonAdd;

    AssetDAO assetDAO;
    Button buttonScan;

    EditText editTextName;
    EditText editTextDescription;

    AutoCompleteTextView autoCompleteTextViewEmployeeName;

    EditText editTextBarcode;
    EditText editTextPrice;

    AutoCompleteTextView autoCompleteTextViewLocation;

    String assetPhotoUri;

    private ActivityResultLauncher<Intent> startForProfileImageResult;

    private PlacesClient placesClient;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    editTextBarcode.setText(result.getContents());
                }
            });

    public AddAssetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();

        startForProfileImageResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == Activity.RESULT_OK) {
                            if (data != null && data.getData() != null) {
                                Uri fileUri = data.getData();
                                assetPhotoUri = fileUri.toString();
                                assetImage.setImageURI(fileUri);
                            }
                        } else if (resultCode == ImagePicker.RESULT_ERROR) {
                            Toast.makeText(getActivity(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_asset, container, false);


        assetImage = root.findViewById(R.id.imageViewAddAsset);
        FloatingActionButton addImage = root.findViewById(R.id.addAssetImageButton);

        buttonAdd = root.findViewById(R.id.buttonAdd);
        buttonScan = root.findViewById(R.id.buttonScan);
        editTextName = root.findViewById(R.id.editTextName);
        editTextDescription = root.findViewById(R.id.editTextDescription);
        editTextBarcode = root.findViewById(R.id.editTextBarcode);
        editTextPrice = root.findViewById(R.id.editTextPrice);
        autoCompleteTextViewEmployeeName = root.findViewById(R.id.autoCompleteTextViewEmployeeName);
        autoCompleteTextViewLocation = root.findViewById(R.id.autoCompleteTextViewLocation);


        ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextViewLocation.setAdapter(adapterLocations);

        ArrayAdapter<String> adapterEmployeeNames = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextViewEmployeeName.setAdapter(adapterEmployeeNames);
        adapterEmployeeNames.addAll(AssetInfoListManager.getInstance().getAll().stream().map(AssetInfo::getEmployeeName).distinct().collect(Collectors.toList()));

        autoCompleteTextViewLocation.addTextChangedListener(new TextWatcher() {
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

        buttonScan.setOnClickListener(this::onScanButtonClick);
        buttonAdd.setOnClickListener(this::onAddButtonClick);
        addImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity())
                        .saveDir(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(500, 500)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(intent -> {
                            startForProfileImageResult.launch(intent);
                            return null;
                        });


            }
        });


        return root;
    }


    public void onAddButtonClick(View view) {

        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String barcodeString = editTextBarcode.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String employeeName = autoCompleteTextViewEmployeeName.getText().toString().trim();
        String location = autoCompleteTextViewLocation.getText().toString().trim();

        if (name.isEmpty() || barcodeString.isEmpty() || priceString.isEmpty() || employeeName.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), R.string.add_button_click_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }
        
        long barcode = Long.parseLong(barcodeString);
        double price = Double.parseDouble(priceString);

        if (assetPhotoUri == null || assetPhotoUri.isEmpty())
            assetPhotoUri = null;

        Asset asset = new Asset(0, name, description, barcode, price, LocalDateTime.now(), employeeName, location, assetPhotoUri);


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = assetDAO.insert(asset);
                asset.setId(id);
                List<AssetInfo> assetInfos = AssetInfoListManager.getInstance().getAll();
                assetInfos.add(AssetInfoListManager.createAssetInfo(asset));
                getActivity().runOnUiThread(() -> {
                    clearFields();
                    Toast.makeText(getContext(), R.string.asset_added, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void clearFields() {
        editTextName.getText().clear();
        editTextDescription.getText().clear();
        editTextBarcode.getText().clear();
        editTextPrice.getText().clear();
        autoCompleteTextViewEmployeeName.getText().clear();
        autoCompleteTextViewLocation.getText().clear();
        assetPhotoUri = null;
        assetImage.setImageResource(R.drawable.box_add_asset_icon);
    }


    public void onScanButtonClick(View view) {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
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