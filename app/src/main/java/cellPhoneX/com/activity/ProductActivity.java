package cellPhoneX.com.activity;

import static cellPhoneX.com.activity.SignUpActivity.USERS_TABLE;

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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellPhoneX.com.R;
import cellPhoneX.com.model.CartModel;
import cellPhoneX.com.model.ProductModel;
import cellPhoneX.com.model.UserModel;
import cellPhoneX.com.tools.Utils;

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
    Button anhien;
    ImageView back;

    private void bind_views() {
        product_image = findViewById(R.id.product_image);
        product_title = findViewById(R.id.product_title);
        product_price = findViewById(R.id.product_price);
        product_details = findViewById(R.id.product_details);
        add_to_cart = findViewById(R.id.add_to_cart);
        back = findViewById(R.id.back);
        anhien = findViewById(R.id.an_hien);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        anhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_data();
            }
        });

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

        if (product.hidden == "Hiện") {
            anhien.setText("Hiện");
        } else {
            anhien.setText("Ẩn");
        }


        product_price.setText(product.price + " đ");

        product_details.setText(product.details + "");
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_product_to_cart();
            }
        });


    }

    private void add_product_to_cart() {
        List<CartModel> list_cart_item = CartModel.find(CartModel.class, "productId = ?", product.product_id);

        if (list_cart_item.size() == 0) {
            CartModel newcart = new CartModel();
            newcart.product_id = product.product_id;
            newcart.product_name = product.title;
            newcart.product_price = product.price + "";
            newcart.product_photo = product.photo;
            newcart.quantity = 1;
            try {
                //cart_item.save();
                CartModel.save(newcart);
                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Failed yo save because " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        list_cart_item.get(0).product_id = product.product_id;
        list_cart_item.get(0).product_name = product.title;
        list_cart_item.get(0).product_price = product.price + "";
        list_cart_item.get(0).product_photo = product.photo;
        list_cart_item.get(0).quantity = list_cart_item.get(0).quantity+1;

        try {
            //cart_item.save();
            CartModel.save(list_cart_item.get(0));
            Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed yo save because " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        return generatedString;
    }

    ProductModel product = new ProductModel();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    UserModel loggedInUser;

    private void get_data() {
        loggedInUser = Utils.get_logged_in_user();

        db.collection("PRODUCTS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Toast.makeText(context, "Product not found.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                product = documentSnapshot.toObject(ProductModel.class);

                if (loggedInUser.user_type.equals("admin")) {
                    anhien.setVisibility(View.VISIBLE);
                } else {
                    anhien.setVisibility(View.INVISIBLE);
                }

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

    private void update_data() {
        String i = "Hiện";

        if (anhien.getText() == "Hiện") {
            anhien.setText("Ẩn");
            i = "Ẩn";
            product.hidden = "Ẩn";
        } else if (anhien.getText() == "Ẩn") {
            anhien.setText("Hiện");
            i = "Hiện";
            product.hidden = "Hiện";
        }



        Map<String, Object> map = new HashMap<>();
        map.put("hidden", i);


        db.collection("PRODUCTS")
                .document(id)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}