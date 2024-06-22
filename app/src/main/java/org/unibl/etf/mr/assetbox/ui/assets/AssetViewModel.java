package org.unibl.etf.mr.assetbox.ui.assets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssetViewModel extends ViewModel {
    private final MutableLiveData<String> assetPhotoUri = new MutableLiveData<>();

    public void setAssetPhotoUri(String uri) {
        assetPhotoUri.setValue(uri);
    }

    public LiveData<String> getAssetPhotoUri() {
        return assetPhotoUri;
    }
}

