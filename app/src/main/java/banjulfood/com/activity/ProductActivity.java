package banjulfood.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import banjulfood.com.R;
import banjulfood.com.model.CartModel;
import banjulfood.com.model.ProductModel;

public class ProductActivity extends AppCompatActivity {

    String id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        context = this;

        /*SugarDb db = new SugarDb(this);
        db.onCreate(db.getDB());*/


        Intent i = getIntent();
        id = i.getStringExtra("id");
        if (id == null || id.length() < 1) {
            Toast.makeText(this, "ID NOT FOUND", Toast.LENGTH_SHORT).show();
            finish();
        }
        bind_views();
        get_data();
    }

    ImageView product_image;
    TextView product_title, product_price, product_details;
    Button add_to_cart;

    private void bind_views() {
        product_image = findViewById(R.id.product_image);
        product_title = findViewById(R.id.product_title);
        product_price = findViewById(R.id.product_price);
        product_details = findViewById(R.id.product_details);
        add_to_cart = findViewById(R.id.add_to_cart);

    }

    private void feed_data() {


        Glide.with(context)
                .load(product.photo)
                .animate(R.anim.abc_fade_in)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .centerCrop()
                .into(product_image)
        ;
        product_title.setText(product.title);
        product_title.setText(product.price + "");
        product_details.setText(product.details + "");
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_product_to_cart();
            }
        });


    }

    private void add_product_to_cart() {
        CartModel cart_item = new CartModel();
        cart_item.product_id = product.product_id;
        cart_item.product_name = product.title;
        cart_item.product_price = product.price + "";
        cart_item.product_photo = product.photo;
        cart_item.quantity = 1;

        try {
            //cart_item.save();
            CartModel.save(cart_item);
            Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed yo save because " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    ProductModel product = new ProductModel();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    private void get_data() {
        db.collection("PRODUCTS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Toast.makeText(context, "Product not found.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                product = documentSnapshot.toObject(ProductModel.class);

                feed_data();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });

    }


}