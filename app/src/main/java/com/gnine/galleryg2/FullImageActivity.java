package com.gnine.galleryg2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    public static final String IMAGE_DATA_KEY= "IMAGE_DATA_KEY";
    public static final String BITMAP_DATA_KEY="BITMAP_DATA_KEY";
    private ArrayList<ImageData> imageDataList;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_information) {
            ViewPager2 viewPager2=findViewById(R.id.viewPagerImageSlider);
            int position=viewPager2.getCurrentItem();
            Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_aboutUs) {
            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        Bundle bundle= getIntent().getExtras();
        imageDataList = bundle.getParcelableArrayList(MainActivity.IMAGE_LIST_KEY);
        position = bundle.getInt(MainActivity.IMAGE_POSITION_KEY);
    }

}