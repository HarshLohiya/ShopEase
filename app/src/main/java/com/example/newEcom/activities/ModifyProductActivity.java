package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newEcom.R;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModifyProductActivity extends AppCompatActivity {
    LinearLayout detailsLinearLayout;
    TextInputEditText nameEditText, descEditText, specEditText, stockEditText, priceEditText, discountEditText;
    Button imageBtn, modifyProductBtn;
    ImageView backBtn, productImageView;
    TextView removeImageBtn;

    AutoCompleteTextView idDropDown, categoryDropDown;
    ArrayAdapter<String> idAdapter, categoryAdapter;
    ProductModel currProduct;
    String[] categories;
    String category, docId, productImage;
    Uri imageUri;
    int productId;
    Context context = this;
    boolean imageUploaded = true;

    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);

        detailsLinearLayout = findViewById(R.id.detailsLinearLayout);
        idDropDown = findViewById(R.id.idDropDown);
        nameEditText = findViewById(R.id.nameEditText);
        categoryDropDown = findViewById(R.id.categoryDropDown);
        descEditText = findViewById(R.id.descriptionEditText);
        specEditText = findViewById(R.id.specificationEditText);
        stockEditText = findViewById(R.id.stockEditText);
        priceEditText = findViewById(R.id.priceEditText);
        discountEditText = findViewById(R.id.discountEditText);
        productImageView = findViewById(R.id.productImageView);

        imageBtn = findViewById(R.id.imageBtn);
        modifyProductBtn = findViewById(R.id.modifyProductBtn);
        backBtn = findViewById(R.id.backBtn);
        removeImageBtn = findViewById(R.id.removeImageBtn);

        imageBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        });

        modifyProductBtn.setOnClickListener(v -> {
            updateToFirebase();
        });

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        removeImageBtn.setOnClickListener(v -> {
            removeImage();
        });

        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Uploading image...");
        dialog.setCancelable(false);

        initDropDown(new MyCallback() {
            @Override
            public void onCallback(String[] cate) {
                categoryAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, cate);
                categoryDropDown.setAdapter(categoryAdapter);
                categoryDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        category = adapterView.getItemAtPosition(i).toString();
                    }
                });
            }

            @Override
            public void onCallback(int[] size) {
                categories = new String[size[0]];
            }

            @Override
            public void onCallback(List<ProductModel> productsList, List<String> docIdList) {
//                products = productsList;
                String[] ids = new String[productsList.size()];
                for (int i=0; i<productsList.size(); i++)
                    ids[i] = Integer.toString(productsList.get(i).getProductId());

                idAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, ids);
                idDropDown.setAdapter(idAdapter);
                idDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        docId = docIdList.get(i);
                        initProduct(productsList.get(i));
                    }
                });
            }
        });
    }

    private void initDropDown(MyCallback myCallback) {
        int size[] = new int[1];

        FirebaseUtil.getProducts().orderBy("productId")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            List<ProductModel> products = new ArrayList<>();
                            List<String> docIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                products.add(document.toObject(ProductModel.class));
                                docIds.add(document.getId());
                                i++;
                            }
                            myCallback.onCallback(products, docIds);
                        }
                    }
                });

        FirebaseUtil.getCategories().orderBy("name")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            size[0] = task.getResult().size();
                        }
                        myCallback.onCallback(size);
                    }
                });
        categories = new String[size[0]];

        FirebaseUtil.getCategories().orderBy("name")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categories[i] = ((String) document.getData().get("name"));
                                Log.i("Category", categories[i]);
                                i++;
                            }
                            myCallback.onCallback(categories);
                        }
                    }
                });
    }

    private void initProduct(ProductModel p) {
        currProduct = p;
        productId = currProduct.getProductId();

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Modifying...");
        dialog.setCancelable(false);
//        dialog.show();

        Picasso.get().load(currProduct.getImage()).into(productImageView, new Callback() {
            @Override
            public void onSuccess() {
                dialog.dismiss();
            }
            @Override
            public void onError(Exception e) {
            }
        });

        detailsLinearLayout.setVisibility(View.VISIBLE);
        productImageView.setVisibility(View.VISIBLE);
        removeImageBtn.setVisibility(View.VISIBLE);

        nameEditText.setText(currProduct.getName());
        categoryDropDown.setText(currProduct.getCategory());
        descEditText.setText(currProduct.getDescription());
        specEditText.setText(currProduct.getSpecification());
        stockEditText.setText(currProduct.getStock()+"");
        priceEditText.setText(currProduct.getPrice()+"");
        discountEditText.setText(currProduct.getDiscount()+"");
    }

    private void removeImage() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog
                .setTitleText("Are you sure?")
                .setContentText("Do you want to remove this image?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        imageUploaded = false;
                        productImageView.setImageDrawable(null);
                        productImageView.setVisibility(View.GONE);
                        removeImageBtn.setVisibility(View.GONE);
                        alertDialog.cancel();
                    }
                }).show();
    }

    private void updateToFirebase(){
        if (!validate())
            return;

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        if (imageUri != null ) {
            FirebaseUtil.getProductImageReference(productId + "").putFile(imageUri)
                    .addOnCompleteListener(t -> {
                        FirebaseUtil.getProductImageReference(productId + "").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                productImage = uri.toString();
                                FirebaseUtil.getProducts().document(docId).update("image", productImage);
                                updateDataToFirebase();
                                dialog.dismiss();
                                Toast.makeText(context, "Product has been successfully modified!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    });
        }
        else {
            updateDataToFirebase();
            dialog.dismiss();
            Toast.makeText(context, "Product has been successfully modified!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void updateDataToFirebase() {
        if (!nameEditText.getText().toString().equals(currProduct.getName())) {
            FirebaseUtil.getProducts().document(docId).update("name", nameEditText.getText().toString());
            FirebaseUtil.getProducts().document(docId).update("searchKey", Arrays.asList(currProduct.getName().trim().toLowerCase().split(" ")));
        }
        if (!categoryDropDown.getText().toString().equals(currProduct.getCategory()))
            FirebaseUtil.getProducts().document(docId).update("category", categoryDropDown.getText().toString());
        if (!descEditText.getText().toString().equals(currProduct.getDescription()))
            FirebaseUtil.getProducts().document(docId).update("description", descEditText.getText().toString());
        if (!specEditText.getText().toString().equals(currProduct.getSpecification()))
            FirebaseUtil.getProducts().document(docId).update("specification", specEditText.getText().toString());
        if (!stockEditText.getText().toString().equals(currProduct.getStock()+""))
            FirebaseUtil.getProducts().document(docId).update("stock", Integer.parseInt(stockEditText.getText().toString()));
        if (!priceEditText.getText().toString().equals(currProduct.getPrice()+""))
            FirebaseUtil.getProducts().document(docId).update("price", Integer.parseInt(priceEditText.getText().toString()));
        if (!discountEditText.getText().toString().equals(currProduct.getDiscount()+""))
            FirebaseUtil.getProducts().document(docId).update("discount", Integer.parseInt(discountEditText.getText().toString()));
    }


    boolean validate() {
        boolean isValid = true;
        if (nameEditText.getText().toString().trim().length() == 0) {
            nameEditText.setError("Name is required");
            isValid = false;
        }
        if (categoryDropDown.getText().toString().trim().length() == 0) {
            categoryDropDown.setError("Category is required");
            isValid = false;
        }
        if (descEditText.getText().toString().trim().length() == 0) {
            descEditText.setError("Description is required");
            isValid = false;
        }
        if (stockEditText.getText().toString().trim().length() == 0) {
            stockEditText.setError("Stock is required");
            isValid = false;
        }
        if (priceEditText.getText().toString().trim().length() == 0) {
            priceEditText.setError("Price is required");
            isValid = false;
        }

        if (!imageUploaded) {
            Toast.makeText(context, "Image is not selected", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                imageUploaded = true;
                dialog.show();

                Picasso.get().load(imageUri).into(productImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
                productImageView.setVisibility(View.VISIBLE);
                removeImageBtn.setVisibility(View.VISIBLE);
//
//                productId = Integer.parseInt(idDropDown.getText().toString());
//                FirebaseUtil.getProductImageReference(productId + "").putFile(imageUri)
//                        .addOnCompleteListener(t -> {
//                            dialog.dismiss();
//                            imageUploaded = true;
//
//                            FirebaseUtil.getProductImageReference(productId + "").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    productImage = uri.toString();
//
//                                    Picasso.get().load(uri).into(productImageView);
//                                    productImageView.setVisibility(View.VISIBLE);
//                                    removeImageBtn.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        });
            }
        }
    }

    public interface MyCallback {
        void onCallback(String[] categories);
        void onCallback(int[] size);
        void onCallback(List<ProductModel> products, List<String> docIds);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        FirebaseUtil.getProductImageReference(productId + "").delete();
//    }
}