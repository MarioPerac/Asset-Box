package org.unibl.etf.mr.assetledger.ui.assets;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfos;
import org.unibl.etf.mr.assetledger.model.MapLocation;

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

    AssetInfos assetInfos;

    AssetDAO assetDAO;

    public AssetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset = (Asset) getArguments().getSerializable("asset");
            assetInfos = AssetInfos.getInstance();
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

        image.setImageURI(Uri.parse(asset.getImagePath()));
        name.setText(asset.getName());
        creationDate.setText(asset.getCreationDate().toString());
        description.setText(asset.getDescription());
        price.setText(String.valueOf(asset.getPrice()));
        location.setText(asset.getLocation());
        employeeName.setText(asset.getEmployeeName());


        buttonEdit.setOnClickListener(this::onEditClick);
        buttonDelete.setOnClickListener(this::onDeleteClick);
        buttonViewLocation.setOnClickListener(this::onViewLocationButtonClick);

        return root;
    }

    private void onEditClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("asset", asset);
        Navigation.findNavController(view).navigate(R.id.action_assetDetailsFragment_to_editAssetFragment, bundle);
    }

    private void onDeleteClick(View view) {

        assetInfos.deleteAssetInfo(asset.getId());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                assetDAO.delete(asset);
            }
        });

        Navigation.findNavController(root).navigateUp();
    }

    private void onViewLocationButtonClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("location", new MapLocation(asset.getLocation(), asset.getLocationLatitude(), asset.getLocationLongitude()));
        Navigation.findNavController(view).navigate(R.id.action_assetDetailsFragment_to_mapsFragment, bundle);
    }
}