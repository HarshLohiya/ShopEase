package com.example.newEcom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newEcom.R;
import com.example.newEcom.adapters.ProductAdapter;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    ArrayList<ProductModel> productModelArrayList;
    int catId;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catId = getIntent().getIntExtra("catId", 0);
        categoryName = getIntent().getStringExtra("categoryName");
//        initProducts();

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initProducts() {
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productModelArrayList = new ArrayList<>();
        getProducts(catId);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        productRecyclerView.setLayoutManager(layoutManager);
//        productAdapter = new ProductAdapter(this, productModelArrayList);
        productRecyclerView.setAdapter(productAdapter);
    }

    void getProducts(int catId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?category_id=" + catId;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray jsonArray = mainObj.getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
//                            ProductModel product = new ProductModel(
//                                    obj.getString("name"),
//                                    Constants.PRODUCTS_IMAGE_URL + obj.getString("image"),
//                                    obj.getString("status"),
//                                    obj.getDouble("price"),
//                                    obj.getDouble("price_discount"),
//                                    obj.getInt("id"),
//                                    obj.getInt("stock")
//                            );
//                            productModelArrayList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}