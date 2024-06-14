package org.unibl.etf.mr.assetledger.ui.assets;

import static android.app.Activity.RESULT_OK;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import org.unibl.etf.mr.assetledger.model.AssetInfo;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.pm.PackageManager;
import android.widget.Toast;


public class AddAssetFragment extends Fragment {

    ImageView assetImage;

    Button buttonAdd;

    AssetDAO assetDAO;
    Button buttonScan;

    EditText name;
    EditText description;

    EditText employeeName;

    EditText barcode;
    EditText price;


    private static int CAMERA_PERMISSION_CODE = 1;
    private static int READ_MEDIA_IMAGES = 2;


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    barcode.setText(result.getContents());
                }
            });

    public AddAssetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_asset, container, false);


        assetImage = root.findViewById(R.id.imageViewAddAsset);
        FloatingActionButton addImage = root.findViewById(R.id.addAssetImageButton);

        buttonAdd = root.findViewById(R.id.buttonAdd);
        buttonScan = root.findViewById(R.id.buttonScan);
        name = root.findViewById(R.id.editTextName);
        description = root.findViewById(R.id.editTextDescription);
        employeeName = root.findViewById(R.id.editTextEmployeeName);
        barcode = root.findViewById(R.id.editTextBarcode);
        price = root.findViewById(R.id.editTextPrice);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanButtonClick(v);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {

                }
            }
        });


        return root;
    }


    private void saveImageToGallery(Uri fileUri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(fileUri));
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "asset_image_" + System.currentTimeMillis() + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/AssetImages");

            Uri imageUri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (imageUri != null) {
                try (OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(imageUri)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    Log.i("SaveImage", "Image saved to gallery: " + imageUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SaveImage", "Error saving image to gallery", e);
        }
    }


    public void onAddButtonClick(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                //izvrsi logiku

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (assetInfoList.isEmpty()) {
//                            emptyListMessage.setVisibility(View.VISIBLE);
//                        } else {
//                            emptyListMessage.setVisibility(View.GONE);
//                        }
                    }
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