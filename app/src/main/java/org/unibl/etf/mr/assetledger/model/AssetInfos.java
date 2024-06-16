package org.unibl.etf.mr.assetledger.model;

import android.content.Context;

import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssetInfos {


    private static AssetInfos instance;
    private List<AssetInfo> assetInfoList;

    AssetDAO assetDAO;


    public static synchronized AssetInfos getInstance() {
        if (instance == null) {
            instance = new AssetInfos();
        }
        return instance;

    }

    private AssetInfos() {

    }

    public List<AssetInfo> getAll() {
        return assetInfoList;
    }

    public void loadAssetInfoList(Context context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                assetDAO = AssetDatabase.getInstance(context).getAssetDAO();
                assetInfoList = assetDAO.getAllAssetInfo();
                if (assetInfoList == null)
                    assetInfoList = new ArrayList<>();
            }
        });
    }

    public void deleteAssetInfo(long id) {
        assetInfoList.removeIf(a -> a.getId() == id);
    }

    public static AssetInfo createAssetInfo(Asset asset) {
        AssetInfo assetInfo = new AssetInfo();

        assetInfo.setId(asset.getId());
        assetInfo.setName(asset.getName());
        assetInfo.setEmployeeName(asset.getEmployeeName());
        assetInfo.setImagePath(asset.getImagePath());
        assetInfo.setLocation(asset.getLocation());
        assetInfo.setCreationDate(asset.getCreationDate());


        return assetInfo;
    }

    public void updateAssetInfo(AssetInfo assetInfo) {

        AssetInfo oldAssetInfo = assetInfoList.stream().filter(a -> a.getId() == assetInfo.getId()).findFirst().get();
        assetInfoList.remove(oldAssetInfo);
        assetInfoList.add(assetInfo);

    }

}
