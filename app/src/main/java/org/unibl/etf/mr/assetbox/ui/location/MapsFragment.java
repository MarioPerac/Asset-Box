package org.unibl.etf.mr.assetbox.ui.location;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unibl.etf.mr.assetbox.R;
import org.unibl.etf.mr.assetbox.model.AssetInfoListManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MapsFragment extends Fragment {

    private View root;
    private String location;
    SupportMapFragment mapFragment;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Address address = getAddress(location);
            if (location != null && address != null) {
                LatLng city = new LatLng(address.getLatitude(), address.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(city).title("Marker in " + location));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 10));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setOnMarkerClickListener(marker -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("assets", (Serializable) AssetInfoListManager.getInstance().getAll().stream().filter(a -> a.getLocation().equals(location)).collect(Collectors.toList()));
                    Navigation.findNavController(root).navigate(R.id.action_mapsFragment_to_navigation_assets, bundle);
                    return true;
                });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_maps, container, false);
        if (getArguments() != null) {
            location = (String) getArguments().getSerializable("location");
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    private Address getAddress(String location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Address address;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            address = addresses.get(0);
        } catch (IOException e) {
            address = null;
        }

        return address;
    }

}
