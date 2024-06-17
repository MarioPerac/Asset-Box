package org.unibl.etf.mr.assetledger.ui.assets;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfoListManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditAssetFragment extends Fragment {

    View root;
    Asset asset;
    AssetInfoListManager assetInfoManager;

    ImageView assetImage;

    Button buttonSave;

    AssetDAO assetDAO;
    Button buttonScan;

    EditText editTextName;
    EditText editTextDescription;

    EditText editTextEmployeeName;

    EditText editTextBarcode;
    EditText editTextPrice;

    EditText editTextLocation;

    String assetPhotoUri;

    private ActivityResultLauncher<Intent> startForProfileImageResult;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    editTextBarcode.setText(result.getContents());
                }
            });

    public EditAssetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset = (Asset) getArguments().getSerializable("asset");
            assetInfoManager = AssetInfoListManager.getInstance();
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
                                    Log.d("tag", fileUri.toString());
                                }
                            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                                Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_asset, container, false);


        assetImage = root.findViewById(R.id.imageViewAddAsset);
        FloatingActionButton addImage = root.findViewById(R.id.addAssetImageButton);

        buttonSave = root.findViewById(R.id.buttonSave);
        buttonScan = root.findViewById(R.id.buttonScan);
        editTextName = root.findViewById(R.id.editTextName);
        editTextDescription = root.findViewById(R.id.editTextDescription);
        editTextEmployeeName = root.findViewById(R.id.editTextEmployeeName);
        editTextBarcode = root.findViewById(R.id.editTextBarcode);
        editTextPrice = root.findViewById(R.id.editTextPrice);
        editTextLocation = root.findViewById(R.id.editTextLocation);

        editTextName.setText(asset.getName());
        editTextDescription.setText(asset.getDescription());
        editTextEmployeeName.setText(asset.getEmployeeName());
        editTextBarcode.setText(String.valueOf(asset.getBarcode()));
        editTextPrice.setText(String.valueOf(asset.getPrice()));
        editTextLocation.setText(asset.getLocation());
        assetImage.setImageURI(Uri.parse(asset.getImagePath()));
        assetPhotoUri = asset.getImagePath();


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanButtonClick(v);
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick(v);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Log.d("tag", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ImagePicker").toString());

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


    public void onSaveButtonClick(View view) {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        long barcode = Long.parseLong(editTextBarcode.getText().toString());
        double price = Double.parseDouble(editTextPrice.getText().toString());
        String employeeName = editTextEmployeeName.getText().toString();
        String location = editTextLocation.getText().toString();


        if (!asset.getName().equals(name))
            asset.setName(name);
        if (!asset.getDescription().equals(description)) {
            asset.setDescription(description);
        }
        if (asset.getBarcode() != barcode) {
            asset.setBarcode(barcode);
        }
        if (asset.getPrice() != price) {
            asset.setPrice(price);
        }
        if (!asset.getEmployeeName().equals(employeeName)) {
            asset.setEmployeeName(employeeName);
        }
        if (!asset.getLocation().equals(location)) {
            asset.setLocation(location);
        }
        if (!asset.getImagePath().equals(assetPhotoUri)) {
            asset.setImagePath(assetPhotoUri);
        }


        assetInfoManager.updateAssetInfo(AssetInfoListManager.createAssetInfo(asset));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                assetDAO.update(asset);
            }
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable("asset", asset);
        Navigation.findNavController(view).navigateUp();
    }

    public void onScanButtonClick(View view) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }

}