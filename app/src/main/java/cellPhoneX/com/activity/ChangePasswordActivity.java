package cellPhoneX.com.activity;

import static cellPhoneX.com.activity.SignUpActivity.USERS_TABLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cellPhoneX.com.R;
import cellPhoneX.com.model.UserModel;
import cellPhoneX.com.tools.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        bind_views();
    }

    EditText oldpass, newpass, pass;
    Context context;

    private void bind_views() {
        oldpass = findViewById(R.id.old_pass);
        newpass = findViewById(R.id.new_pass);
        pass = findViewById(R.id.password_view);

        findViewById(R.id.change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changepass();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        context = this;
    }

    String oldPass = "";
    String newPass = "";
    String curPass = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void changepass() {
        oldPass = oldpass.getText().toString().trim();
        newPass = newpass.getText().toString().trim();
        curPass = pass.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || curPass.isEmpty()) {
            Toast.makeText(this, "You must fill fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserModel loggedInUser = Utils.get_logged_in_user();

        if (loggedInUser.password != oldPass) {
            Toast.makeText(this, "Wrong old pass.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (curPass != newPass) {
            Toast.makeText(this, "Wrong validate pass.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("password", newPass);

        db.collection(USERS_TABLE)
                .document(loggedInUser.email)
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