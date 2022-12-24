package cellPhoneX.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import cellPhoneX.com.R;
import cellPhoneX.com.adapter.PhotoAdapter;
import cellPhoneX.com.model.Photo;
import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.circle);

        photoAdapter = new PhotoAdapter(this, getListPhoto());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignin();
            }
        });
    }

    List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.image0));
        list.add(new Photo(R.drawable.image1));
        return list;
    }

    void gotoSignin(){
        Intent i = new Intent(this, SignInActivity.class);
        this.startActivity(i);
    }
}