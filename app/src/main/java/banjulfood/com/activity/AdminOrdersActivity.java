package banjulfood.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import banjulfood.com.R;
import banjulfood.com.adapter.AdapterOrders;
import banjulfood.com.model.OrderModel;

import static banjulfood.com.MainActivity.setSystemBarColor;
import static banjulfood.com.activity.ChckOutActivity.CUSTOMER_ORDERS;

public class AdminOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        initToolbar();
        get_data();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<OrderModel> orders = new ArrayList<>();

    private void get_data() {
        db.collection(CUSTOMER_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                orders = queryDocumentSnapshots.toObjects(OrderModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });

    }


    private AdapterOrders mAdapter;

    private void initComponents() {


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        mAdapter = new AdapterOrders(orders, this, "0");
        recyclerView.setAdapter(mAdapter);


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterOrders.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrderModel obj, int position) {
                Intent i = new Intent(AdminOrdersActivity.this, AdminOrderActivity.class);
                i.putExtra("order_id", obj.order_id);
                AdminOrdersActivity.this.startActivity(i);
            }
        });


    }


    ProgressBar progressBar;
    private RecyclerView recyclerView;

    private void initToolbar() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }

}