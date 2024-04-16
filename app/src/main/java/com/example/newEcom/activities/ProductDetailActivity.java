package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newEcom.R;
import com.example.newEcom.fragments.CartFragment;
import com.example.newEcom.fragments.HomeFragment;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView productImage;
    TextView productName, productDescription, productPrice;
    Button addToCartBtn;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    CartFragment cartFragment;

    ProductModel currentProduct;
//    String name, image;
//    Double price;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        addToCartBtn = findViewById(R.id.addToCartBtn);

//        name = getIntent().getStringExtra("name");
//        image = getIntent().getStringExtra("image");
//        price = getIntent().getDoubleExtra("price", 1);
//        id = getIntent().getIntExtra("id", 0);
        currentProduct = (ProductModel) getIntent().getSerializableExtra("productObj");

        Picasso.get().load(currentProduct.getImage()).into(productImage);
        productName.setText(currentProduct.getName());
        productPrice.setText(currentProduct.getPrice()+"");
//        productDescription.setText(currentProduct.get);
//        getSupportActionBar().setTitle(currentProduct.getName());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getProductDetails();

//        Cart cart = TinyCartHelper.getCart();
        addToCartBtn.setOnClickListener(v -> {
//            cart.addItem(currentProduct, 1);
            addToCartBtn.setEnabled(false);
            addToCartBtn.setText("Added in cart");

//            Carteasy cs = new Carteasy();
//            cs.add(currentProduct.getId()+"", "name", currentProduct.getName());
//            cs.add(currentProduct.getId()+"", "image", currentProduct.getImage());
//            cs.add(currentProduct.getId()+"", "price", currentProduct.getPrice());
//            cs.add(currentProduct.getId()+"", "quantity", currentProduct.getStock());
//            cs.add(currentProduct.getId()+"", "currency", "dollar");

//            cs.persistData(getApplicationContext(), true);
//            cs.commit(getApplicationContext());
        });

        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, homeFragment).commit();
                }
                if (item.getItemId() == R.id.cart){
                    Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                    intent.putExtra("fragment", "cart");
                    startActivity(intent);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, cartFragment).commit();
                }
                finish();
                return true;
            }
        });
    }

    void getProductDetails(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCT_DETAILS_URL + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {

                JSONObject mainObj = new JSONObject(response);
                if (mainObj.getString("status").equals("success")) {
                    JSONObject obj = mainObj.getJSONObject("product");
                    String description = obj.getString("description");
                    productDescription.setText(
                            Html.fromHtml(description));

//                    currentProduct = new ProductModel(
//                            obj.getString("name"),
//                            Constants.PRODUCTS_IMAGE_URL + obj.getString("image"),
//                            obj.getString("status"),
//                            obj.getDouble("price"),
//                            obj.getDouble("price_discount"),
//                            obj.getInt("id"),
//                            obj.getInt("stock")
//                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart)
            startActivity(new Intent(this, CartActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}