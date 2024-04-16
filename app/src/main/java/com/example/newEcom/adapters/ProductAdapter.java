package com.example.newEcom.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.fragments.ProductFragment;
import com.example.newEcom.model.ProductModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirestoreRecyclerAdapter<ProductModel, ProductAdapter.ProductViewHolder> {
    private Context context;
    private AppCompatActivity activity;

    public ProductAdapter(@NonNull FirestoreRecyclerOptions<ProductModel> options, Context context){
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_adapter,parent,false);
        activity = (AppCompatActivity) view.getContext();
        return new ProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductModel product) {
        Picasso.get().load(product.getImage()).into(holder.productImage, new Callback() {
            @Override
            public void onSuccess() {
                if (holder.getBindingAdapterPosition() == 1) {
                    ShimmerFrameLayout shimmerLayout = activity.findViewById(R.id.shimmerLayout);
                    shimmerLayout.setVisibility(View.GONE);
                    activity.findViewById(R.id.mainLinearLayout).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onError(Exception e) {
            }
        });
        holder.productLabel.setText(product.getName());
        holder.productPrice.setText("Rs. "+ product.getPrice());
        holder.originalPrice.setText("Rs. " + product.getOriginalPrice());
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int discountPerc = (product.getDiscount() * 100) / product.getOriginalPrice();
        holder.discountPercentage.setText(discountPerc + "% OFF");

        holder.itemView.setOnClickListener(v -> {
//            ProductFragment productFragment = new ProductFragment();
//            getIntent().putExtra("productObj", product);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("carInfo", product);  // Key, value
            Fragment fragment = ProductFragment.newInstance(product);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment).addToBackStack(null).commit();
//            Intent intent = new Intent(context, ProductDetailActivity.class);
////            intent.putExtra("name", product.getName());
////            intent.putExtra("image", product.getImage());
////            intent.putExtra("id", product.getId());
////            intent.putExtra("price", product.getPrice());
//            intent.putExtra("productObj", product);
//            context.startActivity(intent);
        });
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView productLabel, productPrice, originalPrice, discountPercentage;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productListImage);
            productLabel = itemView.findViewById(R.id.productLabel);
            productPrice = itemView.findViewById(R.id.productPrice);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            discountPercentage = itemView.findViewById(R.id.discountPercentage);
        }
    }
}


