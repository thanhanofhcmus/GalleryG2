package com.gnine.galleryg2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gnine.galleryg2.data.ImageData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FullImageActivity extends AppCompatActivity{

    //Find the solution to send list image, position to EditFragment, CutFragment
    private ArrayList<ImageData> imageDataList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        getDataFromMainActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> backToMain());

        sendDataToViewPagerFragment();

    }

    private void backToMain() {
        Intent intent = new Intent(FullImageActivity.this, MainActivity.class);
        startActivity(intent);
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
            Toast.makeText(this, "information", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.action_aboutUs) {
            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromMainActivity(){
        Bundle bundle= (Bundle) getIntent().getExtras().get("image list");
        imageDataList= (ArrayList<ImageData>) bundle.get("image list");
        position= (int) getIntent().getExtras().get("position");
        System.out.println(position);
    }

    private void sendDataToViewPagerFragment(){
        Bundle bundle=new Bundle();
        bundle.putSerializable("image list", (Serializable) imageDataList);
        bundle.putInt("position", position);
    }
}