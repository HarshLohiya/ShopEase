package com.example.newEcom.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newEcom.R;
import com.example.newEcom.activities.MainActivity;
import com.example.newEcom.adapters.SearchAdapter;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class CategoryFragment extends Fragment {
    RecyclerView productRecyclerView;
    SearchAdapter searchProductAdapter;
    ImageView backBtn;
    TextView labelTextView;

    String categoryName;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        labelTextView = view.findViewById(R.id.labelTextView);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        backBtn = view.findViewById(R.id.backBtn);

        categoryName = getArguments().getString("categoryName", "Electronics");

        labelTextView.setText(categoryName);
        getProducts(categoryName);

        MainActivity activity = (MainActivity) getActivity();
        activity.hideSearchBar();

        backBtn.setOnClickListener(v -> {
            activity.onBackPressed();
        });
        return view;
    }

    private void getProducts(String categoryName){
        Query query = FirebaseUtil.getProducts().whereEqualTo("category", categoryName);
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        searchProductAdapter = new SearchAdapter(options, getActivity());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerView.setAdapter(searchProductAdapter);
        searchProductAdapter.startListening();
    }
}