package com.example.assignment2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Meal extends AppCompatActivity {
    private final String url = "https://www.themealdb.com/api/json/v1/1/search.php";
    private EditText edtMealName;
    private RequestQueue queue;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        edtMealName = findViewById(R.id.edtMealName);
        txtResult = findViewById(R.id.txtResult);
        queue = Volley.newRequestQueue(this);

        Button btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnShow_Click(view);
            }
        });
    }

    public void btnShow_Click(View view) {
        String mealName = edtMealName.getText().toString();
        if (mealName.equals("")) {
            txtResult.setText("Enter Meal name");
        } else {
            String str = url + "?s=" + mealName;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, str,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray mealsArray = jsonObject.getJSONArray("meals");

                                if (mealsArray.length() > 0) {
                                    JSONObject mealObj = mealsArray.getJSONObject(0);

                                    String mealName = mealObj.getString("strMeal");
                                    String category = mealObj.getString("strCategory");
                                    String instructions = mealObj.getString("strInstructions");

                                    result = "Meal Name: " + mealName + "\n";
                                    result += "Category: " + category + "\n";
                                    result += "Instructions: " + instructions;

                                    txtResult.setText(result);

                                    InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    input.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                } else {
                                    txtResult.setText("Meal not found");
                                }
                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Meal.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }
    }
}