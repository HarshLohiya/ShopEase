package com.example.newEcom.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newEcom.R;
import com.example.newEcom.activities.AdminActivity;
import com.example.newEcom.activities.MainActivity;
import com.example.newEcom.adapters.CategoryAdapter;
import com.example.newEcom.adapters.ProductAdapter;
import com.example.newEcom.model.CategoryModel;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

public class HomeFragment extends Fragment {
    RecyclerView categoryRecyclerView, productRecyclerView;
    MaterialSearchBar searchBar;
    ImageCarousel carousel;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout mainLinearLayout;

    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;

//    TextView textView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchBar = getActivity().findViewById(R.id.searchBar);
//        textView = view.findViewById(R.id.textView);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        carousel = view.findViewById(R.id.carousel);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);

        MainActivity activity = (MainActivity) getActivity();
        activity.showSearchBar();
        shimmerFrameLayout.startShimmer();

//        textView.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), AdminActivity.class));
//        });

        initCarousel();
        initCategories();
        initProducts();

        return view;
    }

    private void initCarousel() {
        FirebaseUtil.getBanner().orderBy("bannerId").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        carousel.addData(new CarouselItem(document.get("bannerImage").toString()));
                    }
                }
            }
        });
    }

    private void initCategories() {
        Query query = FirebaseUtil.getCategories();
        FirestoreRecyclerOptions<CategoryModel> options = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();

        categoryAdapter = new CategoryAdapter(options, getContext());
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.startListening();
    }

    private void initProducts() {
        Query query = FirebaseUtil.getProducts();
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        productAdapter = new ProductAdapter(options, getContext());
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }
}