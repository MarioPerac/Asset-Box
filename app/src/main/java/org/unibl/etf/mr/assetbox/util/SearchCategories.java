package org.unibl.etf.mr.assetbox.util;

import android.content.Context;

import org.unibl.etf.mr.assetbox.R;

public enum SearchCategories {

    Name(R.string.category_name),
    Date(R.string.category_date);
    private int resourceId;

    SearchCategories(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static SearchCategories fromString(String category, Context context) {
        for (SearchCategories searchCategory : SearchCategories.values()) {
            if (context.getString(searchCategory.getResourceId()).equals(category)) {
                return searchCategory;
            }
        }
        return SearchCategories.Name;
    }
}
