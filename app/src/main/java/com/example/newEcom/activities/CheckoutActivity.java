package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.developer.kalert.KAlertDialog;
import com.example.newEcom.R;
import com.example.newEcom.adapters.CartAdapter;
import com.example.newEcom.model.OrderItemModel;
import com.example.newEcom.model.ProductModel;
import com.example.newEcom.utils.EmailSender;
import com.example.newEcom.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import cn.pedant.SweetAlert.SweetAlertDialog;

//import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckoutActivity extends AppCompatActivity {
    TextView subtotalTextView, deliveryTextView, totalTextView;
    //    RecyclerView cartRecyclerView;
    Button checkoutBtn;
    ImageView backBtn;
    CartAdapter cartAdapter;
    ArrayList<ProductModel> productModelArrayList;
    ProgressDialog progressDialog;
//    Cart cart;

    int subTotal;

    EditText nameEditText, emailEditText, phoneEditText, addressEditText, commentEditText;
    String name, email, phone, address, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        totalTextView = findViewById(R.id.totalTextView);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        backBtn = findViewById(R.id.backBtn);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        commentEditText = findViewById(R.id.commentEditText);

        subTotal = getIntent().getIntExtra("price", 10000);
        subtotalTextView.setText("₹ " + subTotal);
        if (subTotal >= 5000) {
            deliveryTextView.setText("₹ 0");
            totalTextView.setText("₹ " + subTotal);
        } else {
            deliveryTextView.setText("₹ 500");
            totalTextView.setText("₹ " + (subTotal + 500));
        }

        checkoutBtn.setOnClickListener(v -> {
            processOrder(new FirestoreCallback() {
                @Override
                public void onCallback(String docid, int oldstock, int quan) {
                    FirebaseUtil.getProducts().document(docid).update("stock", oldstock - quan);
                }
            });
        });
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getCartProducts();
    }

    private void processOrder(FirestoreCallback callback) {
        name = nameEditText.getText().toString();
        email = emailEditText.getText().toString();
        phone = phoneEditText.getText().toString();
        address = addressEditText.getText().toString();
        comment = commentEditText.getText().toString();

        final int[] prevOrderId = new int[1];
        final int[] countOfOrderedItems = new int[1];
        final int[] priceOfOrders = new int[1];

        final List<String>[] productName = new ArrayList[1];
        final List<Integer>[] productPrice = new ArrayList[1];
        final List<Integer>[] productQuantity = new ArrayList[1];
        productName[0] = new ArrayList<>();
        productPrice[0] = new ArrayList<>();
        productQuantity[0] = new ArrayList<>();
        //        final OrderItemModel[] item = new OrderItemModel[1];
        FirebaseUtil.getDetails().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        prevOrderId[0] = (int) (long) task.getResult().get("lastOrderId");
                        countOfOrderedItems[0] = (int) (long) task.getResult().get("countOfOrderedItems");
                        priceOfOrders[0] = (int) (long) task.getResult().get("priceOfOrders");
                    }
                });

        FirebaseUtil.getCartItems()
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                                productName[0].add(document.get("name").toString());
                                productPrice[0].add((int) (long) document.get("price"));
                                productQuantity[0].add((int) (long) document.get("quantity"));

                                OrderItemModel item = new OrderItemModel(prevOrderId[0] + 1, (int) (long) document.get("productId"), document.get("name").toString(), document.get("image").toString(),
                                        (int) (long) document.get("price"), (int) (long) document.get("quantity"), Timestamp.now(), name, email, phone, address, comment);

                                FirebaseFirestore.getInstance().collection("orders").document(FirebaseAuth.getInstance().getUid()).collection("items").add(item);
                                int quantity = (int) (long) document.get("quantity");

                                FirebaseUtil.getProducts().whereEqualTo("productId", document.get("productId"))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String docId = task.getResult().getDocuments().get(0).getId();
                                                    int oldStock = (int) (long) task.getResult().getDocuments().get(0).get("stock");
                                                    Log.i("Stock", oldStock + "");
                                                    Log.i("Id", docId);
                                                    callback.onCallback(docId, oldStock, quantity);
                                                } else
                                                    Toast.makeText(CheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//
                                FirebaseUtil.getCartItems().document(document.getId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("lastOrderId", prevOrderId[0] + 1);
                            map.put("countOfOrderedItems", countOfOrderedItems[0] + count);
                            map.put("priceOfOrders", priceOfOrders[0] + subTotal);

                            FirebaseUtil.getDetails().update(map);

                            String subject = "Your Order is successfully placed with ShopEase!";
                            String messageBody = "Dear " + name + ",\n\n" +
                                    "Thank you for placing your order with ShopEase. We are excited to inform you that your order has been successfully placed.\n\n" +
                                    "Order Details:\n" +
                                    "-----------------------------------------------------------------------------------\n" +
                                    String.format("%-50s %-10s %-10s\n", "Product Name", "Quantity", "Price") +
                                    "-----------------------------------------------------------------------------------\n";
                            for (int i = 0; i < productName[0].size(); i++) {
                                messageBody += String.format("%-50s %-10s ₹%-10d\n", productName[0].get(i), productQuantity[0].get(i), productPrice[0].get(i));
                            }
                            messageBody += "-----------------------------------------------------------------------------\n" +
                                    String.format("%-73s ₹%-10d\n", "Total:", subTotal) +
                                    "-----------------------------------------------------------------------------\n\n" +
                                    "Thank you for choosing our service. If you have any questions or concerns, feel free to contact our customer support.\n\n" +
                                    "Best Regards,\n" +
                                    "ShopEase Team";
                            EmailSender emailSender = new EmailSender(subject, messageBody, email);
                            Log.i("startEmail", email);
                            emailSender.sendEmail();

                            new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Order placed Successfully!")
                                    .setContentText("You will shortly receive an email confirming the order details.")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                            intent.putExtra("orderPlaced", true);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();

//                            new KAlertDialog(CheckoutActivity.this, KAlertDialog.SUCCESS_TYPE, false)
//                                    .setTitleText("Order placed Successfully!")
//                                    .setContentText("You will shortly receive an email confirming the order details.")
//                                    .setConfirmClickListener("Done", new KAlertDialog.KAlertClickListener() {
//                                        @Override
//                                        public void onClick(KAlertDialog kAlertDialog) {
//                                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
//                                            intent.putExtra("orderPlaced", true);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    })
//                                    .show();
                        } else {
                            new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Order Failed!")
                                    .setContentText("Something went wrong, please try again.")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
//                            Toast.makeText(CheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                            new AlertDialog.Builder(CheckoutActivity.this)
//                                    .setTitle("Order Failed")
//                                    .setMessage("Something went wrong, please try again.")
//                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        }
//                                    }).show();
                        }
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(String docid, int oldstock, int quan);
    }


//    private void getCartProducts() {
//        cartRecyclerView = findViewById(R.id.cartRecyclerView);
//        productModelArrayList = new ArrayList<>();
//
//        cart = TinyCartHelper.getCart();
//        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()){
//            ProductModel product = (ProductModel) item.getKey();
//            int quantity = item.getValue();
////            product.setQuantity(quantity);
//
//            productModelArrayList.add(product);
//        }
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
//        cartRecyclerView.setLayoutManager(layoutManager);
//        cartRecyclerView.addItemDecoration(itemDecoration);
////        cartAdapter = new CartAdapter(this, productModelArrayList, new CartAdapter.CartListener() {
////            @Override
////            public void onQuantityChanged() {
////                checkoutPriceTextView.setText(String.format("Rs. %.2f",cart.getTotalPrice()));
////            }
////        });
//        cartRecyclerView.setAdapter(cartAdapter);
//
////        checkoutPriceTextView.setText(String.format("Rs. %.2f",cart.getTotalPrice()));
////        finalPriceTextView.setText(String.format("Rs. %.2f",cart.getTotalPrice().doubleValue()+500));
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}