package banjulfood.com.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orm.SugarDb;

import java.util.List;

import banjulfood.com.MainActivity;
import banjulfood.com.R;
import banjulfood.com.model.UserModel;
import banjulfood.com.tools.Utils;

import static banjulfood.com.activity.SignUpActivity.USERS_TABLE;

public class SignInActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        context = this;



        bind_views();

        UserModel loggedInUser = Utils.get_logged_in_user();
        if (loggedInUser != null) {
            Toast.makeText(this, "You are already logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

    EditText email_view, password_view;
    Button sign_in_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void bind_views() {
        email_view = findViewById(R.id.email_view);
        password_view = findViewById(R.id.password_view);
        sign_in_button = findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_user();
            }
        });
    }

    String email_value = "";
    String pass_value = "";
    ProgressDialog pd;
    Context context;

    private void sign_user() {
        email_value = email_view.getText().toString().trim();
        pass_value = password_view.getText().toString().trim();

        if (email_value.isEmpty() || pass_value.isEmpty()) {
            Toast.makeText(this, "You must fill both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);
        pd.show();

        db.collection(USERS_TABLE)
                .whereEqualTo("email", email_value)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots == null) {
                            pd.hide();
                            pd.dismiss();
                            Toast.makeText(context, " Email not found on our database.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots.isEmpty()) {
                            pd.hide();
                            pd.dismiss();
                            Toast.makeText(context, " Email not found on our database.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<UserModel> users = queryDocumentSnapshots.toObjects(UserModel.class);

                        if (!users.get(0).password.equals(pass_value)) {
                            pd.hide();
                            pd.dismiss();
                            Toast.makeText(context, "Wrong password.", Toast.LENGTH_LONG).show();
                            return;
                        }


                        pd.hide();
                        pd.dismiss();
                        if (login_user(users.get(0))) {
                            Toast.makeText(context, "" + "You account was logged in successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            return;
                        } else {
                            Toast.makeText(context, "Failed to login your account.", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this,
                                "Failed to connect to internet because " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        pd.hide();
                        pd.dismiss();
                    }
                });

    }


    private boolean login_user(UserModel u) {
        try {
            UserModel.save(u);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}