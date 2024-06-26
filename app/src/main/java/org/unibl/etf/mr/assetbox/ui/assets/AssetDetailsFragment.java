package org.unibl.etf.mr.assetbox.ui.assets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;
import org.unibl.etf.mr.assetbox.util.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AssetDetailsFragment extends Fragment {


    View root;
    Asset asset;

    ImageView image;

    TextView name;
    TextView creationDate;
    TextView description;
    TextView price;
    TextView location;

    TextView employeeName;

    TextView barcode;

    Button buttonEdit, buttonDelete, buttonViewLocation;

    AssetInfoListManager assetInfoManager;

    AssetDAO assetDAO;

    public AssetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset = (Asset) getArguments().getSerializable("asset");
            assetInfoManager = AssetInfoListManager.getInstance();
            assetDAO = AssetDatabase.getInstance(getContext()).getAssetDAO();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_asset_details, container, false);


        image = root.findViewById(R.id.imageViewAsset);
        name = root.findViewById(R.id.textViewName);
        creationDate = root.findViewById(R.id.textViewCreationDateTime);
        description = root.findViewById(R.id.textViewDescription);
        price = root.findViewById(R.id.textViewPrice);
        location = root.findViewById(R.id.textViewLocation);
        employeeName = root.findViewById(R.id.textViewEmployeeName);
        barcode = root.findViewById(R.id.textViewBarcode);
        buttonEdit = root.findViewById(R.id.buttonEdit);
        buttonDelete = root.findViewById(R.id.buttonDelete);
        buttonViewLocation = root.findViewById(R.id.buttonViewLocation);

        if (asset.getImagePath() != null && !asset.getImagePath().isEmpty())
            image.setImageURI(Uri.parse(asset.getImagePath()));
        else
            image.setImageResource(R.drawable.box_add_asset_icon);
        name.setText(asset.getName());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        creationDate.setText(dtf.format(asset.getCreationDate()));
        if (asset.getDescription() != null && !asset.getDescription().isEmpty())
            description.setText(asset.getDescription());
        price.setText(String.valueOf(asset.getPrice()));
        location.setText(asset.getLocation());
        employeeName.setText(asset.getEmployeeName());
        barcode.setText(String.valueOf(asset.getBarcode()));

        buttonEdit.setOnClickListener(this::onEditClick);
        buttonDelete.setOnClickListener(this::onDeleteClick);
        if (isGooglePlayServicesAvailable(requireContext()) && isInternetAvailable()) {
            buttonViewLocation.setOnClickListener(this::onViewLocationButtonClick);
        } else {
            buttonViewLocation.setEnabled(false);
            Toast.makeText(getContext(), R.string.google_maps_unavailable_message, Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void onEditClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("asset", asset);
        Navigation.findNavController(view).navigate(R.id.action_assetDetailsFragment_to_editAssetFragment, bundle);
    }

    private void onDeleteClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.confirm_deletion);
        builder.setMessage(R.string.are_you_sure_delete_asset);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAsset();
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

    private void deleteAsset() {
        assetInfoManager.deleteAssetInfo(asset.getId());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                assetDAO.delete(asset);
                deleteImage(asset.getImagePath());

            }
        });

        Navigation.findNavController(root).navigateUp();
    }

    private boolean deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("file://")) {
                imagePath = imagePath.substring(7);
            }
            File file = new File(imagePath);
            return file.delete();
        }
        return false;
    }

    private void onViewLocationButtonClick(View view) {

        if (isGooglePlayServicesAvailable(requireContext()) && isInternetAvailable()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("location", asset.getLocation());
            Navigation.findNavController(view).navigate(R.id.action_assetDetailsFragment_to_mapsFragment, bundle);
        } else {
            buttonViewLocation.setEnabled(false);
            Toast.makeText(getContext(), R.string.google_maps_unavailable_message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}