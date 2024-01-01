package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPass;

    private Button btnSignUp;

    ArrayList<User> list=new ArrayList<>();


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtUsername=findViewById(R.id.edtUsername);
        edtPass=findViewById(R.id.edtPass);
        btnSignUp=findViewById(R.id.btnSignUp);

        setupSharedPrefs();
        loadUsers();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtUsername.getText().toString().isEmpty() && !edtPass.getText().toString().isEmpty()) {
                    User user = new User(edtUsername.getText().toString(), edtPass.getText().toString());
                    list.add(user);
                    saveUsers();
                    edtUsername.getText().clear();
                    edtPass.getText().clear();
                }
            }
        });



    }

    private void setupSharedPrefs() {
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void loadUsers() {
        Gson gson = new Gson();
        String json = prefs.getString("Users", null);
        Type type = new TypeToken<List<User>>() {}.getType();
        list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    private void saveUsers() {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("Users", json);
        editor.apply();
    }
}