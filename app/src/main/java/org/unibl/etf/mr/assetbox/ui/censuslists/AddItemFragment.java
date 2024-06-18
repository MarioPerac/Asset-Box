package org.unibl.etf.mr.assetbox.ui.censuslists;

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

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.model.ItemListManager;


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

    private EditText editTextNewEmployeeName;
    private EditText editTextNewLocation;

    private Button buttonEdit;

    private ItemListManager itemListManager;


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
        if (asset.getImagePath() != null && !asset.getImagePath().isEmpty())
            image.setImageURI(Uri.parse(asset.getImagePath()));
        else
            image.setImageResource(R.drawable.box_add_asset_icon);
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
        String newEmployeeName = editTextNewEmployeeName.getText().toString().trim();
        String newLocation = editTextNewLocation.getText().toString().trim();

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

}