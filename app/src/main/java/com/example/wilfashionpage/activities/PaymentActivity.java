package com.example.wilfashionpage.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.wilfashionpage.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String API_GET_TOKEN ="https://localhost/braintree/main.php";
    final String API_CHECK_OUT ="https://localhost/braintree/checkout.php";

    String token;
    HashMap<String, String> paramsHash;
    Toolbar toolbar;
    TextView subTotal, discount, shipping, total;
    Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        double amount = 0.0;
        amount = getIntent().getDoubleExtra("amount", 0.0);

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.dis_count);
        shipping = findViewById(R.id.ship_ping);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);

        subTotal.setText("R" + amount);

        new getToken().execute();
        
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!subTotal.getText().toString().isEmpty())
                {
                    sendPayments();
                }
                else
                {
                    Toast.makeText(this, "Please select item", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "User cancelled", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("Successful"))
                            Toast.makeText(PaymentActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(PaymentActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());

            }
        })

        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                return null;
                Map<String, String> params = new HashMap<>();
                for (String key:params.keySet())
                {
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask {



        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("ERROR", exception.toString());

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }
    }


}