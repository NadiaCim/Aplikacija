package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.db.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass  = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Unesite email");
                etEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                etPassword.setError("Unesite lozinku");
                etPassword.requestFocus();
                return;
            }

            DatabaseHelper.AuthResult res = db.loginOrCreateUser(email, pass);
            switch (res) {
                case NEW_USER:
                    showMsg("Dobrodošli u TasteTest");
                    // mali delay da korisnik vidi poruku
                    v.postDelayed(() -> goHome(), 1200);
                    break;
                case OK:
                    showMsg("Prijavljeni ste");
                    v.postDelayed(() -> goHome(), 1200);
                    break;
                case WRONG_PASS:
                    showMsg("Neispravni unos.");
                    // kod krive lozinke NE navigiramo
                    break;
            }
        });

        // bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                } else if (id == R.id.nav_search) {
                    startActivity(new Intent(this, SearchActivity.class));
                    return true;
                } else if (id == R.id.nav_profile) {
                    return true;
                }
                return false;
            });
        }
    }

    private void goHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showMsg(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
