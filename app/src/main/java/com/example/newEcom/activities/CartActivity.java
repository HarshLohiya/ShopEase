package com.example.newEcom.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newEcom.R;
import com.example.newEcom.adapters.CartAdapter;
import com.example.newEcom.model.ProductModel;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    TextView cartPriceTextView;
    RecyclerView cartRecyclerView;
    Button continueBtn;
    CartAdapter cartAdapter;
    ArrayList<ProductModel> productModelArrayList;

    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartPriceTextView = findViewById(R.id.cartPriceTextView);
        continueBtn = findViewById(R.id.continueBtn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCartProducts();
        continueBtn.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("price"));
    }

    private void getCartProducts() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        productModelArrayList = new ArrayList<>();

//        Cart cart = TinyCartHelper.getCart();
//        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()){
//            ProductModel product = (ProductModel) item.getKey();
//            int quantity = item.getValue();
////            product.setQuantity(quantity);
//
//            productModelArrayList.add(product);
//        }
//        Map<Integer, Map> data;
////        Carteasy cs = new Carteasy();
////        data = cs.ViewAll(getApplicationContext());
//
//        for (Map.Entry<Integer, Map> entry : data.entrySet()) {
//            //get the Id
//            Log.d("Key: ",entry.getKey().toString());
//            Log.d("Value: ", entry.getValue().toString());
//
//            //Get the items tied to the Id
//            ProductModel product = new ProductModel();
//            Map<String, String> innerdata = entry.getValue();
//            for (Map.Entry<String, String> innerentry : innerdata.entrySet()) {
//                Log.d("Inner Key: ",innerentry.getKey());
//                Log.d("Inner Value: ",innerentry.getValue());
//                if (innerentry.getKey().equals("name"))
//                    product.setName(innerentry.getValue());
////                else if (innerentry.getKey().equals("price"))
////                    product.setPrice(Double.parseDouble(innerentry.getValue()));
//                else if (innerentry.getKey().equals("image"))
//                    product.setImage(innerentry.getValue());
//                else
//                    product.setStock(Integer.parseInt(innerentry.getValue()));
//            }
////            product.setQuantity(1);
////            totalPrice += product.getPrice() * product.getQuantity();
//            productModelArrayList.add(product);
//        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.addItemDecoration(itemDecoration);
//        cartAdapter = new CartAdapter(this, productModelArrayList, new CartAdapter.CartListener() {
//            @Override
//            public void onQuantityChanged() {
//                cartPriceTextView.setText(String.format("Rs. %.2f", totalPrice));
//            }
//        });
//        cartAdapter = new CartAdapter(this, productModelArrayList);
        cartRecyclerView.setAdapter(cartAdapter);

        cartPriceTextView.setText("₹ " + totalPrice);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalPrice = intent.getIntExtra("totalPrice", 1000);
//            Log.i("Price", totalPrice+"");
            cartPriceTextView.setText("Rs. " + totalPrice);
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}