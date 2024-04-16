package com.example.newEcom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.fragments.CategoryFragment;
import com.example.newEcom.model.CategoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class CategoryAdapter extends FirestoreRecyclerAdapter<CategoryModel, CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private AppCompatActivity activity;

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<CategoryModel> options, Context context){
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_adapter,parent,false);
        activity = (AppCompatActivity) view.getContext();
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull CategoryModel category) {
//        CategoryModel category = categories.get(position);
//        Log.i("NAme", category.getName());
        holder.categoryLabel.setText(category.getName());
        Picasso.get().load(category.getIcon()).into(holder.categoryImage);
//        Log.i("Color", category.getColor());
//        holder.categoryLabel.setTextColor(Color.parseColor(category.getColor()));
        holder.categoryImage.setBackgroundColor(Color.parseColor(category.getColor()));
        Bundle bundle = new Bundle();
        bundle.putString("categoryName", category.getName());
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, CategoryActivity.class);
//            intent.putExtra("catId", category.getId());
//            intent.putExtra("categoryName", category.getName());
//            context.startActivity(intent);
            if (!fragment.isAdded())
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment).addToBackStack(null).commit();
        });
    }

//    @Override
//    public int getItemCount() {
//        return categories.size();
//    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView categoryLabel;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryLabel = itemView.findViewById(R.id.categoryLabel);
        }
    }
}
