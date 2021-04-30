package banjulfood.com.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import banjulfood.com.R;
import banjulfood.com.adapter.AdapterProduct;
import banjulfood.com.model.CartModel;
import banjulfood.com.model.OrderModel;
import banjulfood.com.model.ProductModel;

import static banjulfood.com.activity.ChckOutActivity.CUSTOMER_ORDERS;

public class AdminOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        context = this;

        order_id = getIntent().getStringExtra("order_id");
        if (order_id == null) {
            Toast.makeText(context, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //init_toolbar();
        get_data();

    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OrderModel order;

    private void get_data() {
        db.collection(CUSTOMER_ORDERS).document(order_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Toast.makeText(context, "Order not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                order = documentSnapshot.toObject(OrderModel.class);
                feed_data();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to get Order because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }

    RecyclerView recyclerView;
    AdapterProduct mAdapter;
    TextView order_id_view;

    EditText customer_name, customer_address, customer_contact;
    Button delete_order;

    private void feed_data() {

        cartModels = order.cart;

        for (CartModel c : cartModels) {
            ProductModel p = new ProductModel();
            p.title = c.product_name;
            p.category = "";
            p.details = "";
            try {
                p.price = Integer.valueOf(c.product_price);
            } catch (Exception e) {

            }
            p.product_id = c.product_id;
            p.photo = c.product_photo;
            products.add(p);
        }


        recyclerView = findViewById(R.id.cart_products);
        order_id_view = findViewById(R.id.order_id);
        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_address);
        customer_contact = findViewById(R.id.customer_contact);
        delete_order = findViewById(R.id.delete_order);

        order_id_view.setText("ORDER #" + order.order_id);
        customer_name.setText(order.customer.first_name + " " + order.customer.last_name);
        customer_address.setText(order.customer.address);
        customer_contact.setText(order.customer.email);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new AdapterProduct(products, this, "1");
        recyclerView.setAdapter(mAdapter);

        delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(CUSTOMER_ORDERS).document(order_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Order Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to delete Deleted Order because " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }
        });

    }


    String order_id = null;
    Context context;

    List<ProductModel> products = new ArrayList<>();
    List<CartModel> cartModels = null;
}