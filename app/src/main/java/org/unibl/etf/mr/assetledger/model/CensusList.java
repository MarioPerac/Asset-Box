package org.unibl.etf.mr.assetledger.model;

import android.content.Context;

import org.unibl.etf.mr.assetledger.assetsdb.AssetDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CensusList {

    private List<Item> items;
    private static CensusList instance;

    private CensusList() {
    }


    public static CensusList getInstance() {
        if (instance == null) {
            instance = new CensusList();
        }
        return instance;
    }


    public List<Item> getItems() {
        return items;
    }

    public void loadAssetInfoList(Context context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                assetDAO = AssetDatabase.getInstance(context).getAssetDAO();
//                assetInfoList = assetDAO.getAllAssetInfo();
                //to do
            }
        });
    }
}
