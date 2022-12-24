package cellPhoneX.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import cellPhoneX.com.R;
import cellPhoneX.com.model.UserModel;
import cellPhoneX.com.tools.Utils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        UserModel loggedInUser = Utils.get_logged_in_user();

        ((TextView) findViewById(R.id.ten)).setText(loggedInUser.last_name);
        ((TextView) findViewById(R.id.diachi)).setText(loggedInUser.address);
        ((TextView) findViewById(R.id.mail)).setText(loggedInUser.email);
        ((TextView) findViewById(R.id.dt)).setText(loggedInUser.phone_number);
        ((TextView) findViewById(R.id.id)).setText(loggedInUser.user_id);
        ((TextView) findViewById(R.id.type)).setText(loggedInUser.user_type);
    }
}