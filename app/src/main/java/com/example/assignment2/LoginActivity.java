package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPass;
    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    public static final String FLAG = "FLAG";
    private boolean flag = false;
    private CheckBox cbRemember;

    private Button btnLogin;

    ArrayList<User> list = new ArrayList<>();

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        edtUsername = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);

        flag = prefs.getBoolean(FLAG, false);
        if (flag) {
            String name = prefs.getString(NAME, "");
            String password = prefs.getString(PASS, "");
            edtUsername.setText(name);
            edtPass.setText(password);
            cbRemember.setChecked(true);
        }

        Gson gson = new Gson();
        String json = prefs.getString("Users", null);
        Type type = new TypeToken<List<User>>() {}.getType();
        list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String name = edtUsername.getText().toString();
        String password = edtPass.getText().toString();

        for (User user : list) {
            if (user.getUsername().equals(name) && user.getPassword().equals(password)) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", user.getUsername());
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String json = prefs.getString("Users", null);
        Type type = new TypeToken<List<User>>() {}.getType();
        list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    public void btnLoginOnClick(View view) {
        String name = edtUsername.getText().toString();
        String password = edtPass.getText().toString();

        if (cbRemember.isChecked()) {
            if (!flag) {
                editor.putString(NAME, name);
                editor.putString(PASS, password);
                editor.putBoolean(FLAG, true);
                editor.apply();
            }
        }
    }
}
