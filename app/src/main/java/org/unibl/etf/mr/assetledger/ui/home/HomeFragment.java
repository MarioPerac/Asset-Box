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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.databinding.FragmentHomeBinding;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.model.Category;
import org.unibl.etf.mr.assetledger.recyclerview.CategoryAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        assetCard.setOnClickListener(this::onAssetCardClick);

        employeeCard.setOnClickListener(this::onEmployeeCardClick);

        locationCard.setOnClickListener(this::onLocationCardClick);

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


    private void onAssetCardClick(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<AssetInfo> assetInfos = AssetDatabase.getInstance(getContext()).getAssetDAO().getAllAssetInfo();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();

                        if (assetInfos == null || assetInfos.isEmpty())
                            bundle.putSerializable("assets", new ArrayList<AssetInfo>());
                        else
                            bundle.putSerializable("assets", (Serializable) assetInfos);
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_assets, bundle);
                    }
                });
            }
        });
    }

    public void onLocationCardClick(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<AssetInfo> assetInfos = AssetDatabase.getInstance(getContext()).getAssetDAO().getAllAssetInfo();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();

                        if (assetInfos == null || assetInfos.isEmpty())
                            bundle.putSerializable("assets", new ArrayList<AssetInfo>());
                        else
                            bundle.putSerializable("assets", (Serializable) assetInfos);
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_locationsFragment, bundle);
                    }
                });
            }
        });
    }

    public void onEmployeeCardClick(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<AssetInfo> assetInfos = AssetDatabase.getInstance(getContext()).getAssetDAO().getAllAssetInfo();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();

                        if (assetInfos == null || assetInfos.isEmpty())
                            bundle.putSerializable("assets", new ArrayList<AssetInfo>());
                        else
                            bundle.putSerializable("assets", (Serializable) assetInfos);
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_employeesFragment, bundle);
                    }
                });
            }
        });
    }
}