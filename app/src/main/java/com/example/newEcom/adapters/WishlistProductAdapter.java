package com.example.newEcom.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.activities.MainActivity;
import com.example.newEcom.fragments.ProductFragment;
import com.example.newEcom.model.CartItemModel;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistProductAdapter extends FirestoreRecyclerAdapter<CartItemModel, WishlistProductAdapter.WishlistProductViewHolder> {
    private Context context;
    private AppCompatActivity activity;

    public WishlistProductAdapter(@NonNull FirestoreRecyclerOptions<CartItemModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public WishlistProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist_adapter,parent,false);
        activity = (AppCompatActivity) view.getContext();
        return new WishlistProductViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishlistProductAdapter.WishlistProductViewHolder holder, int position, @NonNull CartItemModel product) {
        holder.productNameTextView.setText(product.getName());
        Picasso.get().load(product.getImage()).into(holder.productImageView);
        holder.productPriceTextView.setText("Rs. "+ product.getPrice());
        holder.originalPrice.setText("₹ " + product.getOriginalPrice());
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int discountPerc = (product.getOriginalPrice() - product.getPrice()) * 100 / product.getOriginalPrice();
        holder.discountPercentage.setText(discountPerc + "% OFF");

        holder.productLinearLayout.setOnClickListener(v -> {
            Fragment fragment = ProductFragment.newInstance(product.getProductId());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment).addToBackStack(null).commit();
        });

        holder.addToCartBtn.setOnClickListener(v -> {
            addToCart(product, new MyCallback() {
                @Override
                public void onCallback(int stock) {
                    FirebaseUtil.getCartItems().whereEqualTo("productId", product.getProductId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean documentExists = false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            documentExists = true;
                                            String docId = document.getId();
                                            int quantity = (int) (long) document.getData().get("quantity");
                                            if (quantity < stock) {
                                                FirebaseUtil.getCartItems().document(docId).update("quantity", quantity + 1);
                                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(context, "Max stock available: " + stock, Toast.LENGTH_SHORT).show();
                                        }
                                        if (!documentExists) {
                                            CartItemModel cartItem = new CartItemModel(product.getProductId(), product.getName(), product.getImage(), 1, product.getPrice(), product.getOriginalPrice(), Timestamp.now());
                                            FirebaseUtil.getCartItems().add(cartItem);
                                            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                                        }
                                        MainActivity activity = (MainActivity) context;
                                        activity.addOrRemoveBadge();
                                    }
                                }
                            });
                }
            });
        });

        holder.removeWishlistBtn.setOnClickListener(v -> {
            FirebaseUtil.getWishlistItems().whereEqualTo("productId", product.getProductId())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String docId = document.getId();

                                    FirebaseUtil.getWishlistItems().document(docId).delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            }
                        }
                    });
        });
    }

    private void addToCart(CartItemModel product, MyCallback myCallback) {
        FirebaseUtil.getProducts().whereEqualTo("productId", product.getProductId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int stock = (int) (long) document.getData().get("stock");
                                myCallback.onCallback(stock);
                            }
                        }
                    }
                });
    }

    public class WishlistProductViewHolder extends RecyclerView.ViewHolder{
        TextView productNameTextView, productPriceTextView, originalPrice, discountPercentage;
        ImageView productImageView;
        LinearLayout productLinearLayout;
        Button addToCartBtn, removeWishlistBtn;

        public WishlistProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImage);
            productNameTextView = itemView.findViewById(R.id.productName);
            productPriceTextView = itemView.findViewById(R.id.productPrice);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            discountPercentage =itemView.findViewById(R.id.discountPercentage);
            productLinearLayout = itemView.findViewById(R.id.productLinearLayout);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            removeWishlistBtn = itemView.findViewById(R.id.removeWishlistBtn);
        }
    }

    public interface MyCallback {
        void onCallback(int stock);
    }
}
