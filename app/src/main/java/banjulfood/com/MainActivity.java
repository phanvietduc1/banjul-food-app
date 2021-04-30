package banjulfood.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import banjulfood.com.activity.AdminOrdersActivity;
import banjulfood.com.activity.ChckOutActivity;
import banjulfood.com.activity.ProductActivity;
import banjulfood.com.adapter.AdapterProduct;
import banjulfood.com.model.ProductModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterProduct mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initToolbar();

        get_data();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent i = new Intent(this, ChckOutActivity.class);
            this.startActivity(i);
        } else if (item.getItemId() == R.id.action_orders) {
            Intent i = new Intent(this, AdminOrdersActivity.class);
            this.startActivity(i);
        } else if (item.getItemId() == R.id.action_add_product) {
            Toast.makeText(this, "You clicked on Add New Product", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    List<ProductModel> products = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void get_data() {
        db.collection("PRODUCTS").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products = queryDocumentSnapshots.toObjects(ProductModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });

    }

    ProgressBar progressBar;

    private void initComponents() {


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        mAdapter = new AdapterProduct(products, this, "0");
        recyclerView.setAdapter(mAdapter);


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterProduct.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ProductModel obj, int position) {
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                i.putExtra("id", obj.product_id);
                MainActivity.this.startActivity(i);
            }
        });


    }


    private void initToolbar() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Banjul Foods");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }


    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}