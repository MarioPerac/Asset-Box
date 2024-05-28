package org.unibl.etf.mr.assetledger.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.assetledger.R;
import org.unibl.etf.mr.assetledger.model.Category;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    private OnCategoryClickListener listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recicler_category, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        final Category category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryIcon.setImageResource(category.getIconId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryIcon;
        public CategoryViewHolder(View v) {
            super(v);
            categoryName = v.findViewById(R.id.categoryTextView);
            categoryIcon = v.findViewById(R.id.categoryImageView);
        }
    }


}
