package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newEcom.R;
import com.example.newEcom.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminActivity extends AppCompatActivity {
    LinearLayout logoutBtn;
    CardView addProductBtn, modifyProductBtn, addCategoryBtn, modifyCategoryBtn, addBannerBtn, modifyBannerBtn;
    TextView countOrders, priceOrders, usersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logoutBtn = findViewById(R.id.logoutBtn);
        addProductBtn = findViewById(R.id.addProductBtn);
        modifyProductBtn = findViewById(R.id.modifyProductBtn);
        addCategoryBtn = findViewById(R.id.addCategoryBtn);
        modifyCategoryBtn = findViewById(R.id.modifyCategoryBtn);
        addBannerBtn = findViewById(R.id.addBannerBtn);
        modifyBannerBtn = findViewById(R.id.modifyBannerBtn);
        countOrders = findViewById(R.id.countOrders);
        priceOrders = findViewById(R.id.priceOrders);
//        usersTextView = findViewById(R.id.usersTextView);

        getDetails();

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        addProductBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddProductActivity.class));
        });

        modifyProductBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ModifyProductActivity.class));
        });

        addCategoryBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddCategoryActivity.class));
        });

        modifyCategoryBtn.setOnClickListener(v -> startActivity(new Intent(this, ModifyCategoryActivity.class)));
        addBannerBtn.setOnClickListener(v -> startActivity(new Intent(this, AddBannerActivity.class)));
        modifyBannerBtn.setOnClickListener(v -> startActivity(new Intent(this, ModifyBannerActivity.class)));
    }

    private void getDetails() {
        FirebaseUtil.getDetails().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            countOrders.setText(task.getResult().get("countOfOrderedItems").toString());
                            priceOrders.setText(task.getResult().get("priceOfOrders").toString());
                        }
                    }
                });
    }
}