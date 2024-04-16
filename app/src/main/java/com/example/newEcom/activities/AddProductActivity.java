package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newEcom.R;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductActivity extends AppCompatActivity {
    TextInputEditText idEditText, nameEditText, descEditText, specEditText, stockEditText, priceEditText, discountEditText;
    Button imageBtn, addProductBtn;
    ImageView backBtn, productImageView;
    TextView removeImageBtn;

    AutoCompleteTextView categoryDropDown;
    ArrayAdapter<String> arrayAdapter;
    String[] categories;
    String category, productImage, shareLink;
    String productName;
    int productId;
    Context context = this;
    boolean imageUploaded = false;

    //    ProgressDialog dialog;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        idEditText = findViewById(R.id.idEditText);
        nameEditText = findViewById(R.id.nameEditText);
        categoryDropDown = findViewById(R.id.categoryDropDown);
        descEditText = findViewById(R.id.descriptionEditText);
        specEditText = findViewById(R.id.specificationEditText);
        stockEditText = findViewById(R.id.stockEditText);
        priceEditText = findViewById(R.id.priceEditText);
        discountEditText = findViewById(R.id.discountEditText);
        productImageView = findViewById(R.id.productImageView);

        imageBtn = findViewById(R.id.imageBtn);
        addProductBtn = findViewById(R.id.addProductBtn);
        backBtn = findViewById(R.id.backBtn);
        removeImageBtn = findViewById(R.id.removeImageBtn);

        FirebaseUtil.getDetails().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            productId = Integer.parseInt(task.getResult().get("lastProductId").toString()) + 1;
                            idEditText.setText(productId + "");
                        }
                    }
                });

//        FirebaseUtil.getCategories().orderBy("name")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int i = 0;
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                categories[i] = ((String) document.getData().get("name"));
//                                Log.i("Category", categories[i]);
//                                i++;
//                            }
//                        }
//                    }
//                });

//        Toast.makeText(this, Arrays.toString(categories), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, categories.length+"", Toast.LENGTH_SHORT).show();

        imageBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        });

        addProductBtn.setOnClickListener(v -> {
            generateDynamicLink();
        });

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        removeImageBtn.setOnClickListener(v -> {
            removeImage();
        });

//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Uploading image...");
//        dialog.setCancelable(false);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Uploading image...");
        dialog.setCancelable(false);
    }

    private void getCategories(MyCallback myCallback) {
        int size[] = new int[1];

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

    private void addToFirebase() {
        productName = nameEditText.getText().toString();
        List<String> sk = Arrays.asList(productName.trim().toLowerCase().split(" "));
        String desc = descEditText.getText().toString();
        String spec = specEditText.getText().toString();
        int price = Integer.parseInt(priceEditText.getText().toString());
        int discount = Integer.parseInt(discountEditText.getText().toString());
        int stock = Integer.parseInt(stockEditText.getText().toString());
//        productId = Integer.parseInt(idEditText.getText().toString());

        ProductModel model = new ProductModel(productName, sk, productImage, category, desc, spec, price, discount, price - discount, productId, stock, shareLink, 0f, 0);
//        Log.i("Link2", shareLink);
        FirebaseUtil.getProducts().add(model)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        FirebaseUtil.getDetails().update("lastOrderId", productId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddProductActivity.this, "Product has been added successfully!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                    }
                });
    }

    private void generateDynamicLink() {
//        Log.i("Function", "Function called");
        if (!validate())
            return;

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/?product_id=" + productId))
                .setDomainUriPrefix("https://shopease.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.newEcom").build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                        .setTitle(productName)
                        .setImageUrl(Uri.parse(productImage))
                        .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            shareLink = shortLink.toString();
//                            Log.i("Link True", shareLink);

                            addToFirebase();
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                            Log.i("Error", "There is some error");
                        }
                    }
                });
    }

    private boolean validate() {
        boolean isValid = true;
        if (idEditText.getText().toString().trim().length() == 0) {
            idEditText.setError("Id is required");
            isValid = false;
        }
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

                        FirebaseUtil.getProductImageReference(productId + "").delete();
                        alertDialog.dismiss();
                    }
                }).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                if (idEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Please fill the id first", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();

                productId = Integer.parseInt(idEditText.getText().toString());
                FirebaseUtil.getProductImageReference(productId + "").putFile(imageUri)
                        .addOnCompleteListener(t -> {
                            imageUploaded = true;

                            FirebaseUtil.getProductImageReference(productId + "").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    productImage = uri.toString();

                                    Picasso.get().load(uri).into(productImageView, new Callback() {
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
                                }
                            });
                        });
            }
        }
    }

    public interface MyCallback {
        void onCallback(String[] categories);

        void onCallback(int[] size);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseUtil.getProductImageReference(productId + "").delete();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCategories(new MyCallback() {
            @Override
            public void onCallback(String[] cate) {
                arrayAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, cate);
                categoryDropDown.setAdapter(arrayAdapter);
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
        });
    }
}