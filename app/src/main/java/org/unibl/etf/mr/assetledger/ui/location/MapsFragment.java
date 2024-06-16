package org.unibl.etf.mr.assetledger.ui.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
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
import org.unibl.etf.mr.assetledger.model.AssetInfos;
import org.unibl.etf.mr.assetledger.model.MapLocation;

import java.io.Serializable;
import java.util.stream.Collectors;

public class MapsFragment extends Fragment {

    View root;

    MapLocation location;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d("Location", location.getName());
            LatLng city = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(city).title("Marker in " + location.getName()));

            googleMap.setOnMarkerClickListener(this::onMarkerClick);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 10));
            googleMap.getUiSettings().setZoomControlsEnabled(true);

        }

        public boolean onMarkerClick(Marker marker) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("assets", (Serializable) AssetInfos.getInstance().getAll().stream().filter(a -> a.getLocation().equals(location.getName())).collect(Collectors.toList()));
            Navigation.findNavController(root).navigate(R.id.action_mapsFragment_to_navigation_assets, bundle);
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