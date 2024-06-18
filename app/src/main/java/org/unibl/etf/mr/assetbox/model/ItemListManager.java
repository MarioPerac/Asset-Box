package org.unibl.etf.mr.assetbox.model;

import java.util.ArrayList;
import java.util.List;

public class ItemListManager {

    private List<Item> items;

    private static ItemListManager instance;

    private ItemListManager() {
        items = new ArrayList<>();
    }

    public static ItemListManager getInstance() {
        if (instance == null)
            instance = new ItemListManager();

        return instance;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    public List<Item> getItems() {
        return items;
    }
}
