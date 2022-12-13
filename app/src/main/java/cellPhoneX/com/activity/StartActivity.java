package cellPhoneX.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import cellPhoneX.com.R;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    private long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent i = new Intent(this, SecondActivity.class);
        this.startActivity(i);

//        try {
//            Thread.sleep(7 * 1000);
//
//            Intent i = new Intent(this, SecondActivity.class);
//            this.startActivity(i);
//        } catch (InterruptedException ie) {
//            Thread.currentThread().interrupt();
//        }
    }

    public void myTask() {
        Intent i = new Intent(this, SecondActivity.class);
        this.startActivity(i);
    }
}