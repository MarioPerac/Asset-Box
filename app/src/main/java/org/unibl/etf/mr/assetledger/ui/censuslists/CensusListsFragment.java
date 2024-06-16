package org.unibl.etf.mr.assetledger.ui.censuslists;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.Item;

import java.util.List;

public class CensusListsFragment extends Fragment {

    View root;

    List<Item> items;
    List<Item> filteredItems;

    private RecyclerView recyclerView;

    public CensusListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_census_lists, container, false);

        return root;
    }
}