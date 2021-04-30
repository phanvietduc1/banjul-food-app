package banjulfood.com.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import banjulfood.com.R;
import banjulfood.com.model.ProductModel;

public class FoodAddActivity extends AppCompatActivity {

    ImageButton btn_done;
    ImageView product_photo;
    private final int PICK_IMAGE_REQUEST = 1;
    public final String PRODUCT_TABLE = "PRODUCTS";
    TextInputEditText Category_view;
    ProductModel new_product = new ProductModel();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);
        bind_views();


    }

    TextInputEditText product_name, product_details, product_price;

    private void bind_views() {
        new_product.product_id = db.collection(PRODUCT_TABLE).document().getId();

        btn_done = findViewById(R.id.btn_done);
        product_price = findViewById(R.id.product_price);
        product_details = findViewById(R.id.product_details);
        product_name = findViewById(R.id.product_name);
        product_photo = findViewById(R.id.product_photo);
        progressDialog = new ProgressDialog(this);
        Category_view = findViewById(R.id.Category_view);
        Category_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_category();
            }
        });


        product_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_product();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select product image"), PICK_IMAGE_REQUEST);
    }

    public static final String[] categories = new String[]{"Women", "Men", "Kids", "Electronics"};

    int selected_category = -1;

    private void select_category() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select category");
        builder.setSingleChoiceItems(categories, selected_category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Category_view.setText(categories[i]);
                selected_category = i;
            }
        });

        builder.setNegativeButton("OK", null);
        builder.show();
    }

    private Uri imagePath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                product_photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    ProgressDialog progressDialog;
    StorageReference storage_ref_main;

    private void upload_to_firestore() {
        Toast.makeText(this, "Time to uokiad to fire...", Toast.LENGTH_SHORT).show();
        db.collection(PRODUCT_TABLE).document(new_product.product_id).set(new_product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.hide();
                progressDialog.dismiss();
                Toast.makeText(FoodAddActivity.this, "Uploaded successfully!.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                Toast.makeText(FoodAddActivity.this, "Failed upload product because " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    private void submit_product() {

        new_product.title = product_name.getText().toString();
        if (new_product.title.isEmpty()) {
            Toast.makeText(this, "Product name can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        new_product.category = Category_view.getText().toString();
        if (new_product.category.isEmpty()) {
            Toast.makeText(this, "You must select product category.", Toast.LENGTH_SHORT).show();
            select_category();
            return;
        }


        if (product_price.getText().toString().isEmpty()) {
            Toast.makeText(this, "Product price can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new_product.price = Integer.valueOf(product_price.getText().toString());
        } catch (Exception e) {

        }

        if (new_product.price < 0) {
            Toast.makeText(this, "Product price can't be less than zero.", Toast.LENGTH_SHORT).show();
            return;
        }

        new_product.details = product_details.getText().toString();


        if (imagePath == null) {
            Toast.makeText(this, "You must select product photo.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        storage_ref_main = FirebaseStorage.getInstance().getReference();

        storage_ref_main.child("products/" + new_product.product_id).putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(FoodAddActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                storage_ref_main.child("products/" + new_product.product_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new_product.photo = uri.toString();
                        upload_to_firestore();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new_product.photo = "https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=2000&q=80";
                        upload_to_firestore();
                        return;
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodAddActivity.this, "Failed to upload photo" + e.getMessage(), Toast.LENGTH_SHORT).show();
                new_product.photo = "https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=2000&q=80";
                upload_to_firestore();
                return;
            }
        });

    }
}