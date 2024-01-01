package com.example.assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SecondApi extends AppCompatActivity {

    private  final String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private TextView txtCocktailDetails;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_api);

        txtCocktailDetails = findViewById(R.id.txtCocktailDetails);
        Button btnFetchRandomCocktail = findViewById(R.id.btnFetchCocktail);
        requestQueue = Volley.newRequestQueue(this);

        btnFetchRandomCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchRandomCocktailDetails();
            }
        });
    }

    private void fetchRandomCocktailDetails() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayCocktailDetails(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtCocktailDetails.setText("Error getting cocktail details");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void displayCocktailDetails(JSONObject cocktailData) {
        try {
            JSONArray drinks = cocktailData.getJSONArray("drinks");

            if (drinks.length() > 0) {
                JSONObject cocktail = drinks.getJSONObject(0);

                String cocktailName = cocktail.optString("strDrink", "Unknown");
                String cocktailCategory = cocktail.optString("strCategory", "Unknown");
                String cocktailInstructions = cocktail.optString("strInstructions", "No instructions available");

                String details = "Name: " + cocktailName + "\nCategory: " + cocktailCategory + "\nInstructions: " + cocktailInstructions;

                txtCocktailDetails.setText(details);
            } else {
                txtCocktailDetails.setText("No cocktail details available");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
