package cellPhoneX.com.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import cellPhoneX.com.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cellPhoneX.com.databinding.ActivityNewBinding;

public class MainNewActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onClick(View view) {
//        when(view.id) {
//            R.id.leftBtnIv -> onLeftButtonClicked()
//            R.id.clearInputBtnIv -> onClearInputButtonClicked()
//        }

        switch (view.getId() /*to get clicked view id**/) {
            case R.id.leftBtnIv:

                // do something when the corky is clicked

                break;
            case R.id.clearInputBtnIv:

                // do something when the corky2 is clicked

                break;

            default:
                break;
        }
    }
}
