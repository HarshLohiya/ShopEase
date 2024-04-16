package com.example.newEcom.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.newEcom.R;
import com.example.newEcom.activities.MainActivity;
import com.example.newEcom.adapters.ProductAdapter;
import com.example.newEcom.adapters.WishlistProductAdapter;
import com.example.newEcom.model.CartItemModel;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class WishlistFragment extends Fragment {
    RecyclerView productRecyclerView;
    WishlistProductAdapter productAdapter;
    ImageView backBtn;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        MainActivity activity = (MainActivity) getActivity();
        activity.hideSearchBar();

        productRecyclerView = view.findViewById(R.id.wishlistRecyclerView);
        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            activity.onBackPressed();
        });

        initProducts();

        return view;
    }

    private void initProducts() {
        Query query = FirebaseUtil.getWishlistItems().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CartItemModel> options = new FirestoreRecyclerOptions.Builder<CartItemModel>()
                .setQuery(query, CartItemModel.class)
                .build();

        productAdapter = new WishlistProductAdapter(options, getActivity());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }
}