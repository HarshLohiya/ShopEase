package com.example.newEcom.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.activities.MainActivity;
import com.example.newEcom.model.CartItemModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CartAdapter extends FirestoreRecyclerAdapter<CartItemModel, CartAdapter.CartViewHolder> {

    private Context context;
    private AppCompatActivity activity;
    //    private ArrayList<ProductModel> products;
    final int[] stock = new int[1];
    int totalPrice = 0;
    boolean gotSum = false;
//    CartListener cartListener;
//    Cart cart;
//    public interface CartListener {
//        public void onQuantityChanged();
//    }

    public CartAdapter(@NonNull FirestoreRecyclerOptions<CartItemModel> options, Context context) {
        super(options);
        this.context = context;
//        this.products = products;
//        this.cartListener = cartListener;
//        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_adapter, parent, false);
        activity = (AppCompatActivity) view.getContext();
//        Intent intent = new Intent("price");
//        intent.putExtra("totalPrice", totalPrice);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItemModel model) {
//        totalPrice += model.getPrice() * model.getQuantity();
//        Log.i("Check", model.getPrice() +" "+model.getQuantity());
//        totalPrice += (int) (long) document.getData().get("price") * (int) (long)document.getData().get("quantity");
        if (position == 0 && !gotSum) {
            calculateTotalPrice();
        }

        holder.productName.setText(model.getName());
        holder.singleProductPrice.setText("₹ " + model.getPrice());
        holder.productPrice.setText("₹ " + model.getPrice() * model.getQuantity());
        holder.originalPrice.setText("₹ " + model.getOriginalPrice());
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productQuantity.setText(model.getQuantity() + "");
        Picasso.get().load(model.getImage()).into(holder.productCartImage, new Callback() {
            @Override
            public void onSuccess() {
                if (holder.getBindingAdapterPosition() == getSnapshots().size()-1) {
                    ShimmerFrameLayout shimmerLayout = activity.findViewById(R.id.shimmerLayout);
                    shimmerLayout.setVisibility(View.GONE);
                    activity.findViewById(R.id.mainLinearLayout).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });

        holder.plusBtn.setOnClickListener(v -> {
            changeQuantity(model, true);
        });
        holder.minusBtn.setOnClickListener(v -> {
            changeQuantity(model, false);
        });
    }

    private void calculateTotalPrice() {
        gotSum = true;
//        Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
        for (CartItemModel model : getSnapshots()) {
            totalPrice += model.getPrice() * model.getQuantity();
//            Log.i("Check", model.getPrice() +" "+ model.getQuantity());
        }
//        Toast.makeText(context, totalPrice+"", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("price");
        intent.putExtra("totalPrice", totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void changeQuantity(CartItemModel model, boolean plus) {
        FirebaseUtil.getProducts().whereEqualTo("productId", model.getProductId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                stock[0] = (int) (long) document.getData().get("stock");
                            }
                        }
                    }
                });

        FirebaseUtil.getCartItems().whereEqualTo("productId", model.getProductId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docId = document.getId();
                                int quantity = (int) (long) document.getData().get("quantity");
                                if (plus) {
                                    if (quantity < stock[0]) {
                                        FirebaseUtil.getCartItems().document(docId).update("quantity", quantity + 1);
                                        totalPrice += model.getPrice();
                                    } else
                                        Toast.makeText(context, "Max stock available: " + stock[0], Toast.LENGTH_SHORT).show();
                                } else {
                                    totalPrice -= model.getPrice();
                                    if (quantity > 1)
                                        FirebaseUtil.getCartItems().document(docId).update("quantity", quantity - 1);
                                    else
                                        FirebaseUtil.getCartItems().document(docId)
                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });
                                }
                                MainActivity activity = (MainActivity) context;
                                activity.addOrRemoveBadge();

                                Intent intent = new Intent("price");
                                intent.putExtra("totalPrice", totalPrice);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }
                        }
                    }
                });
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, singleProductPrice, productQuantity, minusBtn, plusBtn, originalPrice;
        ImageView productCartImage;
//        ViewGroup viewGroup;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.nameTextView);
            singleProductPrice = itemView.findViewById(R.id.priceTextView1);
            productPrice = itemView.findViewById(R.id.priceTextView);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            productQuantity = itemView.findViewById(R.id.quantityTextView);
            productCartImage = itemView.findViewById(R.id.productImageCart);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
//            viewGroup = itemView.findViewById(android.R.id.content);
        }
    }


//    @Override
//    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//        ProductModel productModel = products.get(position);
////        if (productModel.getQuantity() > 0) {
//            holder.productName.setText(productModel.getName());
//            holder.productPrice.setText("Rs. " + productModel.getPrice() * productModel.getQuantity());
//            holder.productQuantity.setText(productModel.getQuantity() + " item(s)");
////        holder.productQuantity.setText(productModel.getStock());
//            Picasso.get().load(productModel.getImage()).into(holder.productCartImage);
//            holder.itemView.setOnClickListener(v -> {
//                setQuantityDialog(holder.viewGroup, productModel);
//            });
//        }
//    }
//
//    private void setQuantityDialog(ViewGroup viewGroup, ProductModel productModel) {
//        View view = LayoutInflater.from(context).inflate(R.layout.quantity_dialog, viewGroup, false);
//        AlertDialog dialog = new AlertDialog.Builder(context)
//                .setView(view)
//                .create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
//
//        TextView nameTextView = view.findViewById(R.id.productName);
//        TextView stockTextView = view.findViewById(R.id.productStock);
//        TextView quantityTextView = view.findViewById(R.id.quantity);
//        nameTextView.setText(productModel.getName());
//        stockTextView.setText("Stock: " + productModel.getStock());
//        quantityTextView.setText(String.valueOf(productModel.getQuantity()));
//
//
//        view.findViewById(R.id.minusBtn).setOnClickListener(v1 -> {
//            int quantity = productModel.getQuantity();
//            if (quantity > 0)
//                quantity--;
//            productModel.setQuantity(quantity);
//            quantityTextView.setText(String.valueOf(quantity));
//        });
//
//        view.findViewById(R.id.plusBtn).setOnClickListener(v1 -> {
//            int quantity = productModel.getQuantity();
//            quantity++;
//            if (quantity > productModel.getStock()){
//                Toast.makeText(context, "Max stock available: " + productModel.getStock(), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                productModel.setQuantity(quantity);
//                quantityTextView.setText(String.valueOf(quantity));
//            }
//        });

//        view.findViewById(R.id.saveBtn).setOnClickListener(v1 -> {
//            dialog.dismiss();
//            totalPrice = 0;
//            for (ProductModel p : products)
//                totalPrice += p.getPrice() * p.getQuantity();
//
//            Intent intent = new Intent("price");
//            intent.putExtra("totalPrice", totalPrice);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//            notifyDataSetChanged();
////            cart.updateItem(productModel, productModel.getQuantity());
////            cartListener.onQuantityChanged();
//        });
//
//        dialog.show();
//    }

//    @Override
//    public int getItemCount() {
//        return products.size();
//    }
}
