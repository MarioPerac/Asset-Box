package org.unibl.etf.mr.assetbox.model;


import android.content.Context;

import org.unibl.etf.mr.assetbox.assetsdb.AssetDatabase;
import org.unibl.etf.mr.assetbox.assetsdb.dao.CensusListDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CensusListsManager {
    private static CensusListsManager instance;
    private List<CensusList> censusLists;

    private CensusListDAO censusListDAO;

    private CensusListsManager() {
        censusLists = new ArrayList<>();
    }

    public static synchronized CensusListsManager getInstance() {
        if (instance == null) {
            instance = new CensusListsManager();
        }
        return instance;
    }

    public List<CensusList> getCensusLists() {
        return censusLists;
    }

    public void setCensusLists(List<CensusList> censusLists) {
        this.censusLists = censusLists;
    }

    public void addCensusList(CensusList censusList) {
        censusLists.add(censusList);
    }

    public void removeCensusList(CensusList censusList) {
        censusLists.remove(censusList);
    }

    public CensusList getCensusListById(long id) {
        for (CensusList censusList : censusLists) {
            if (censusList.getId() == id) {
                return censusList;
            }
        }
        return null;
    }

    public void clearCensusLists() {
        censusLists.clear();
    }

    public void loadCensusLists(Context context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                censusListDAO = AssetDatabase.getInstance(context).getCensuslistDAO();
                censusLists = censusListDAO.getAll();
                if (censusLists == null)
                    censusLists = new ArrayList<>();
            }
        });
    }
}
