package com.zuul.androidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú; esto agrega ítems a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja los clics de los ítems del menú aquí.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Log.d("HomeActivity", "action_search");
            return true;
        } else if (id == R.id.action_add) {
            Log.d("HomeActivity", "action_add");
            return true;
        } else if (id == R.id.action_info) {
            Log.d("HomeActivity", "action_info");
            return true;
        } else if (id == R.id.action_settings) {
            Log.d("HomeActivity", "action_settings");
            return true;
        } else if (id == R.id.action_help) {
            Log.d("HomeActivity", "action_help");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
