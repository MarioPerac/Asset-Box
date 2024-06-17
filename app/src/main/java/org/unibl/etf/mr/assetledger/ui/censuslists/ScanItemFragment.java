package org.unibl.etf.mr.assetledger.ui.censuslists;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.model.Asset;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanItemFragment extends Fragment {
    View root;

    EditText editTextBarcode;
    Button buttonScan;
    Button buttonFind;
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String barcode = result.getContents();
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            Asset asset = AssetDatabase.getInstance(getContext()).getAssetDAO().getByBarcode(Long.parseLong(barcode));
                            getActivity().runOnUiThread(() -> {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("asset", asset);
                                Navigation.findNavController(root).navigate(R.id.action_scanItemFragment_to_addItemFragment, bundle);
                            });

                        }
                    });
                }
            });

    public ScanItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_scan_item, container, false);

        editTextBarcode = root.findViewById(R.id.editTextBarcode);
        buttonScan = root.findViewById(R.id.buttonScan);
        buttonFind = root.findViewById(R.id.buttonFind);
        buttonScan.setOnClickListener(this::onScanButtonClick);
        buttonFind.setOnClickListener(this::onFindButtonClick);
        return root;
    }

    public void onScanButtonClick(View view) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }

    public void onFindButtonClick(View view) {
        String barcode = editTextBarcode.getText().toString();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Asset asset = AssetDatabase.getInstance(getContext()).getAssetDAO().getByBarcode(Long.parseLong(barcode));
                getActivity().runOnUiThread(() -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("asset", asset);
                    Navigation.findNavController(root).navigate(R.id.action_scanItemFragment_to_addItemFragment, bundle);
                });

            }
        });
    }
}