package org.unibl.etf.mr.assetledger.ui.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.MapLocation;

public class MapsFragment extends Fragment {

    View root;

    MapLocation location;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in " + location.getName()));

            googleMap.setOnMarkerClickListener(this::onMarkerClick);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        public boolean onMarkerClick(Marker marker) {
            //to do: implement
            return true;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_maps, container, false);
        if (getArguments() != null) {
            location = (MapLocation) getArguments().getSerializable("location");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}