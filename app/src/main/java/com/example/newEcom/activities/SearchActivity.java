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
import com.example.newEcom.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    ArrayList<ProductModel> productModelArrayList;
    String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTerm = getIntent().getStringExtra("query");
        initProducts();

        getSupportActionBar().setTitle(searchTerm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initProducts() {
//        productRecyclerView = findViewById(R.id.productRecyclerView);
//        productModelArrayList = new ArrayList<>();
//        getProducts(query);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        productRecyclerView.setLayoutManager(layoutManager);
////        productAdapter = new ProductAdapter(this, productModelArrayList);
//        productRecyclerView.setAdapter(productAdapter);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productModelArrayList = new ArrayList<>();
//        getProducts();
        Query q = FirebaseUtil.getProducts().whereArrayContains("searchKey", searchTerm.toLowerCase());
        Query query = FirebaseUtil.getProducts().orderBy("name").startAt(searchTerm).endAt(searchTerm+ "\uf8ff");
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(q, ProductModel.class)
                .build();

//        productAdapter = new ProductAdapter(options, this);
//        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        productRecyclerView.setAdapter(productAdapter);
//        productAdapter.startListening();
    }

    void getProducts(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?q=" + query;
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