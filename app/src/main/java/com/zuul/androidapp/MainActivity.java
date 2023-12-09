package com.zuul.androidapp;

import com.zuul.androidapp.database.DatabaseHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuul.androidapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private EditText telefonoEditText, emailEditText, passwrdEditText, repasswrdEditText;
    private boolean userLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar databaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Si el usuario está autenticado, muestra los fragments
        if (userLoggedIn) {
            showFragments();
        } else {
            showLoginLayout(); // Si no, muestra el layout de inicio de sesión
        }
    }

    private void showLoginLayout() {
        setContentView(R.layout.login_main);
    }

    public void loginUser(View view) {
        String username = getUsernameFromEditText();
        String password = getPasswordFromEditText();

        if (username.isEmpty() || password.isEmpty()) {
            showSnackbarError("Por favor, complete el usuario y la contraseña");
        } else {
            boolean isValidUser = databaseHelper.checkUserCredentials(username, password);
            if (isValidUser) {
                String userEmail = databaseHelper.getEmailForUser(username);

                userLoggedIn = true;
                saveUsernameLocally(username, userEmail);
                showFragments();
            } else {
                showSnackbarError("Usuario o contraseña incorrecta");
            }
        }
    }

    private void saveUsernameLocally(String username, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
    }

    private String getPasswordFromEditText() {
        EditText passwordEditText = findViewById(R.id.passwrd);
        return passwordEditText.getText().toString();
    }

    public void showRegisterLayout(View view) {
        setContentView(R.layout.register_main);

        // Referencias a los EditText
        telefonoEditText = findViewById(R.id.telefono);
        emailEditText = findViewById(R.id.email);
        passwrdEditText = findViewById(R.id.passwrd);
        repasswrdEditText = findViewById(R.id.repasswrd);

        // Configurar el teclado para el campo de teléfono
        telefonoEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        // Configurar el teclado para el campo de correo electrónico
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void registerUser(View view) {
        String username = getUsernameFromEditText();
        String address = getAddressFromEditText();
        String phone = telefonoEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwrdEditText.getText().toString();
        String confirmPassword = repasswrdEditText.getText().toString();

        if (username.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showSnackbarError("Por favor, complete todos los campos");

        } else if (!password.equals(confirmPassword)) {
            showSnackbarError("Las contraseñas no coinciden");

        } else {
            boolean isInserted = databaseHelper.insertUserData(username, address, phone, email, password);
            if (isInserted) {
                userLoggedIn = true;
                saveUsernameLocally(username, email);
                showFragments();
            } else {
                showSnackbarError("El usuario ya existe");
            }
        }
    }

    private String getUsernameFromEditText() {
        EditText usernameEditText = findViewById(R.id.username);
        return usernameEditText.getText().toString();
    }

    private String getAddressFromEditText() {
        EditText direccionEditText = findViewById(R.id.direccion);
        return direccionEditText.getText().toString();
    }

    private void showSnackbarError(String message) {
        View parentLayout = findViewById(android.R.id.content); // Obtener el layout principal
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showFragments() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_fav, R.id.nav_pedido)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Agregar un listener al menú para cerrar sesión
        Menu navMenu = navigationView.getMenu();
        MenuItem cerrarMenuItem = navMenu.findItem(R.id.nav_cerrar);
        cerrarMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Borrar SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Borrar todos los datos almacenados
                editor.apply();

                // Reiniciar la actividad
                Intent intent = getIntent();
                finish(); // Cerrar la actividad actual
                startActivity(intent); // Iniciar la actividad de nuevo

                return true;
            }
        });

        // Obtener SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // Obtener el nombre de usuario y el correo electrónico guardados
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");

        // Actualizar TextView en nav_header_main.xml
        View headerView = binding.navView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);

        // Actualizar los TextView con los valores de SharedPreferences
        textViewUsername.setText(username);
        textViewEmail.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void togglePasswordVisibility(View view) {
        ImageView imageView = (ImageView) view;
        String editTextTag = imageView.getTag().toString();

        EditText editText;
        if (editTextTag.equals("passwrd")) {
            editText = findViewById(R.id.passwrd);
        } else if (editTextTag.equals("passwrdLogin")){
            editText = findViewById(R.id.passwrd);
        } else {
            editText = findViewById(R.id.repasswrd);
        }

        if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Mostrar la contraseña
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar la contraseña
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        editText.setSelection(editText.length()); // Mantener el cursor al final del texto
    }
}