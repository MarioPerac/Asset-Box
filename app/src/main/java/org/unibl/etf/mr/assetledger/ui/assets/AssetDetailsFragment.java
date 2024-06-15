package org.unibl.etf.mr.assetledger.ui.assets;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.Asset;


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

    public AssetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset = (Asset) getArguments().getSerializable("asset");
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


        image.setImageURI(Uri.parse(asset.getImagePath()));
        name.setText(asset.getName());
        creationDate.setText(asset.getCreationDate().toString());
        description.setText(asset.getDescription());
        price.setText(String.valueOf(asset.getPrice()));
        location.setText(asset.getLocation());
        employeeName.setText(asset.getEmployeeName());
        

        return root;
    }
}