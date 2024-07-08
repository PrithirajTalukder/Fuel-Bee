package com.example.fuelbee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentScreen extends AppCompatActivity {

    Button paymentButton;
    EditText etName, etEmail, etAddress, etMobile;
    String publishKey = "pk_test_51PZdQdLMuGbPZaeaZ87cnfQaRtzK14Pmefj7rVeykXufVHrfBZvWwyeUsj5k7wEedGTkxpfL1n7MwmYsuA0z0Pj400VEGDeF0N";
    String secretKey = "sk_test_51PZdQdLMuGbPZaeaf8YVM63roGdaEdYptbOGxz4EwgUwbfXHXCZPxdYlYAatPf41JwHNt6MVmdGQ76kvoRBd7sZ000xpxXTvXB";

    PaymentSheet paymentSheet;

    String customerID;
    String ephemeralKey;
    String clientSecret;

    PaymentDAO paymentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);

        paymentButton = findViewById(R.id.pay_with_stripe);
        etName = findViewById(R.id.payment_name);
        etEmail = findViewById(R.id.payment_email);
        etAddress = findViewById(R.id.payment_address);
        etMobile = findViewById(R.id.payment_mobile);

        PaymentConfiguration.init(this, publishKey);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> onPaymentResult(paymentSheetResult));

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    if (clientSecret != null && !clientSecret.isEmpty()) {
                        PaymentFlow();
                    } else {
                        Toast.makeText(PaymentScreen.this, "Payment details not ready yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        paymentDAO = new PaymentDAO(this);
        paymentDAO.open();

        createCustomer();
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Please enter your name");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Please enter your address");
            etAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mobile)) {
            etMobile.setError("Please enter your mobile number");
            etMobile.requestFocus();
            return false;
        }

        if (mobile.length() < 11) { // Adjusted to check for 11 digits
            etMobile.setError("Mobile number must be at least 11 digits");
            etMobile.requestFocus();
            return false;
        }

        return true;
    }

    private void createCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            Toast.makeText(PaymentScreen.this, "Customer ID: " + customerID, Toast.LENGTH_SHORT).show();

                            getEphemeralKey(customerID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentScreen.this, "Failed to create customer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PaymentScreen.this, "Error creating customer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);
    }

    private void getEphemeralKey(String customerID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ephemeralKey = object.getString("id");
                            Toast.makeText(PaymentScreen.this, "Ephemeral Key: " + ephemeralKey, Toast.LENGTH_SHORT).show();

                            getClientSecret(customerID, ephemeralKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentScreen.this, "Failed to get ephemeral key: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PaymentScreen.this, "Error getting ephemeral key: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                header.put("Stripe-Version", "2024-06-20");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerID, String ephemeralKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            clientSecret = object.getString("client_secret");
                            Toast.makeText(PaymentScreen.this, "Client Secret: " + clientSecret, Toast.LENGTH_SHORT).show();
                            paymentButton.setEnabled(true); // Enable the button here after obtaining client secret
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentScreen.this, "Failed to get client secret: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PaymentScreen.this, "Error getting client secret: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "780"+ "00"); // Provide amount here
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("FuelBee", new PaymentSheet.CustomerConfiguration(
                customerID,
                ephemeralKey
        )));
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
            storePaymentDetails(); // Store payment details on success
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void storePaymentDetails() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        double paymentAmount = 780.00; // Set the actual payment amount

        Payment payment = new Payment();
        payment.setName(name);
        payment.setEmail(email);
        payment.setAddress(address);
        payment.setMobile(mobile);
        payment.setPaymentAmount(paymentAmount);

        paymentDAO.addPayment(payment); // Ensure addPayment is called

        Log.d("PaymentScreen", "Payment details stored successfully");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        paymentDAO.close();
    }
}
