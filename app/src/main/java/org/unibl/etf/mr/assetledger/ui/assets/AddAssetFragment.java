package org.unibl.etf.mr.assetledger.ui.assets;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.AssetInfos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.widget.Toast;


public class AddAssetFragment extends Fragment {

    ImageView assetImage;

    Button buttonAdd;

    AssetDAO assetDAO;
    Button buttonScan;

    EditText editTextName;
    EditText editTextDescription;

    EditText editTextEeployeeName;

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
        editTextEeployeeName = root.findViewById(R.id.editTextEmployeeName);
        editTextBarcode = root.findViewById(R.id.editTextBarcode);
        editTextPrice = root.findViewById(R.id.editTextPrice);
        editTextLocation = root.findViewById(R.id.editTextLocation);

        buttonScan.setOnClickListener(this::onScanButtonClick);
        buttonAdd.setOnClickListener(this::onAddButtonClick);
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


    public void onAddButtonClick(View view) {


        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        long barcode = Long.parseLong(editTextBarcode.getText().toString());
        double price = Double.parseDouble(editTextPrice.getText().toString());
        String employeeName = editTextEeployeeName.getText().toString();
        String location = editTextLocation.getText().toString();

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Address address;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            address = addresses.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Asset asset = new Asset(0, name, description, barcode, price, LocalDateTime.now(), employeeName, location, address.getLatitude(), address.getLongitude(), assetPhotoUri);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = assetDAO.insert(asset);
                asset.setId(id);
                List<AssetInfo> assetInfos = AssetInfos.getInstance().getAll();
                assetInfos.add(AssetInfos.createAssetInfo(asset));
                getActivity().runOnUiThread(() -> {
                    Navigation.findNavController(view).navigateUp();
                });

            }
        });


    }

    public void onScanButtonClick(View view) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }
}