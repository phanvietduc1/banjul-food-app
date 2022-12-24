package cellPhoneX.com.activity;

import static cellPhoneX.com.activity.ChckOutActivity.CUSTOMER_ORDERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cellPhoneX.com.R;
import cellPhoneX.com.adapter.AdapterOrders;
import cellPhoneX.com.model.OrderModel;
import cellPhoneX.com.model.UserModel;
import cellPhoneX.com.tools.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        UserModel loggedInUser = Utils.get_logged_in_user();
        initToolbar();
        get_data(loggedInUser.email);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<OrderModel> orders = new ArrayList<>();

    private void get_data(String email) {
        db.collection(CUSTOMER_ORDERS)
                .whereEqualTo("customer.email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

//        TextView v = findViewById(R.id.doanhthu);
//        int tongprice = 0;
//        for (int i=0; i<orders.size(); i++)
//        {
//            tongprice += Integer.valueOf(orders.get(i).money.replaceAll("[^0-9]", ""));
//        }
//        v.setText("Tổng doanh thu: " + tongprice + " vnđ");


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterOrders.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrderModel obj, int position) {
                Intent i = new Intent(CustomerOrdersActivity.this, CustomerOrderActivity.class);
                i.putExtra("order_id", obj.order_id);
                CustomerOrdersActivity.this.startActivity(i);
            }
        });


    }


    ProgressBar progressBar;
    private RecyclerView recyclerView;

    private void initToolbar() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Admin Orders");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setSystemBarColor(this);
    }
}