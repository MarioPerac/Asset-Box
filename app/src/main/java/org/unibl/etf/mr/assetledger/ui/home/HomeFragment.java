package org.unibl.etf.mr.assetledger.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.databinding.FragmentHomeBinding;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.AssetInfos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private AssetInfos assetInfos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetInfos = AssetInfos.getInstance();
        assetInfos.loadAssetInfoList(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        CardView assetCard = root.findViewById(R.id.assetCard);
        CardView employeeCard = root.findViewById(R.id.employeeCard);
        CardView locationCard = root.findViewById(R.id.locationCard);
        CardView censusListCard = root.findViewById(R.id.censusListCard);
        FloatingActionButton addAssetButton = root.findViewById(R.id.addAssetButton);
        addAssetButton.setOnClickListener(this::onAddAssetClick);

        assetCard.setOnClickListener(this::onAssetCardClick);

        employeeCard.setOnClickListener(this::onEmployeeCardClick);

        locationCard.setOnClickListener(this::onLocationCardClick);

        censusListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void onAssetCardClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_assets);
    }

    public void onLocationCardClick(View view) {

        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_locationsFragment);
    }

    public void onEmployeeCardClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_employeesFragment);

    }

    private void onAddAssetClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addAssetFragment);
    }
}