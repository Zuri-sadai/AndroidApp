package com.zuul.androidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        // Se añade el listener del ActionBarDrawerToggle al DrawerLayout
        drawerLayout.addDrawerListener(toggle);

        // Se añade un SimpleDrawerListener para manejar los eventos de apertura y cierre
        drawerLayout.addDrawerListener(new SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("MainActivity", "Drawer opened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("MainActivity", "Drawer closed");
            }
        });

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // Maneja la navegación de los ítems del menú aquí.
                    int id = menuItem.getItemId();

                    // Agregar un Log para cada selección del menú
                    Log.d("MainActivity", "Item seleccionado: " + menuItem.getTitle());

                    // Ejemplo: Si tienes un ítem de menú con id 'nav_home'
                    if (id == R.id.nav_fav) {
                        Log.d("MainActivity", "nav_fav");
                        return true;
                    } else if (id == R.id.nav_pedido) {
                        Log.d("MainActivity", "nav_pedido");
                        return true;
                    } else if (id == R.id.nav_promo) {
                        Log.d("MainActivity", "nav_promo");
                        return true;
                    } else if (id == R.id.nav_cerrar) {
                        Log.d("MainActivity", "nav_cerrar");
                        return true;
                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.action_cart) {
            Log.d("MainActivity", "action_cart");
            return true;
        } else if (id == R.id.action_profile) {
            Log.d("MainActivity", "action_profile");
            return true;
        } else if (id == R.id.action_info) {
            Log.d("MainActivity", "action_info");
            return true;
        } else if (id == R.id.action_settings) {
            Log.d("MainActivity", "action_settings");
            return true;
        } else if (id == R.id.action_help) {
            Log.d("MainActivity", "action_help");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
