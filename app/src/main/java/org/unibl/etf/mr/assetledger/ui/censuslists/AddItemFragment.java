package org.unibl.etf.mr.assetledger.ui.censuslists;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfoListManager;
import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.model.ItemListManager;


public class AddItemFragment extends Fragment {
    View root;
    Asset asset;

    ImageView image;

    TextView name;
    TextView creationDate;
    TextView description;
    TextView price;
    TextView textViewLocation;

    TextView textViewEmployeeName;

    TextView barcode;

    EditText editTextNewEmployeeName;
    EditText editTextNewLocation;

    Button buttonEdit;

    ItemListManager itemListManager;


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
        editTextNewEmployeeName = root.findViewById(R.id.newEmployeeName);
        editTextNewLocation = root.findViewById(R.id.newLocation);
        buttonEdit = root.findViewById(R.id.buttonEdit);

        image.setImageURI(Uri.parse(asset.getImagePath()));
        name.setText(asset.getName());
        creationDate.setText(asset.getCreationDate().toString());
        description.setText(asset.getDescription());
        price.setText(String.valueOf(asset.getPrice()));
        textViewLocation.setText(asset.getLocation());
        textViewEmployeeName.setText(asset.getEmployeeName());

        buttonEdit.setOnClickListener(this::onEditButtonClick);

        return root;
    }

    public void onEditButtonClick(View view) {

        String newEmployeeName = null;
        if (!editTextNewEmployeeName.getText().toString().isEmpty())
            newEmployeeName = editTextNewEmployeeName.getText().toString();
        else
            newEmployeeName = textViewEmployeeName.getText().toString();

        String newLocation = null;
        if (!editTextNewLocation.getText().toString().isEmpty())
            newLocation = editTextNewLocation.getText().toString();
        else
            newLocation = textViewLocation.getText().toString();


        Item item = new Item(0, asset.getId(), textViewEmployeeName.getText().toString(), newEmployeeName, textViewLocation.getText().toString(), newLocation, 0);
        item.setAsset(asset);

        ItemListManager.getInstance().addItem(item);
        Navigation.findNavController(view).navigateUp();
        Navigation.findNavController(view).navigateUp();
    }
}