package org.unibl.etf.mr.assetledger.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.databinding.FragmentHomeBinding;
import org.unibl.etf.mr.assetledger.model.Category;
import org.unibl.etf.mr.assetledger.recyclerview.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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

        assetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Asset card", Toast.LENGTH_SHORT).show();
            }
        });

        employeeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Employees card", Toast.LENGTH_SHORT).show();
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click
            }
        });

        censusListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onAssetCardClick(View view){
        Toast.makeText(getContext(), "Asset Card", Toast.LENGTH_SHORT).show();
    }

}