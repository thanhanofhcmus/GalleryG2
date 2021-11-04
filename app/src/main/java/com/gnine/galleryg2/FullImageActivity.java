package com.gnine.galleryg2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gnine.galleryg2.fragments.EditFragment;
import com.gnine.galleryg2.fragments.ViewPagerFragment;
import com.gnine.galleryg2.slider.SliderAdapter;
import com.gnine.galleryg2.slider.SliderItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;


import java.util.ArrayList;
import java.util.List;

public class FullImageActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNav;
    private RelativeLayout viewPagerLayout;
    private Toolbar toolbar;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });


    }

    private void backToMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_favorite) {
            Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId()==R.id.action_information){
            Toast.makeText(this,"information",Toast.LENGTH_SHORT).show();

        }else if(item.getItemId()==R.id.action_aboutUs){
            Toast.makeText(this,"About us",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



}