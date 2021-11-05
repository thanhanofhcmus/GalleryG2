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

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> backToMain());

        setSupportActionBar(toolbar);
    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
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


}