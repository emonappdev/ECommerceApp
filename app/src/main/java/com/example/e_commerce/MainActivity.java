package com.example.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    GridView gridView;
    LottieAnimationView animationView;
    HashMap<String, String> hashMap = new HashMap<>();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // --------------------------------- On Back Press ---------------------------------------------


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish(); // Activity বন্ধ
                } else {
                    Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

        // --------------------------------- On Back Press  End---------------------------------------------


        gridView = findViewById(R.id.gridView);
        animationView = findViewById(R.id.animationView);
        // -----------------------------------------------------------------------------------------------------------

        String url = "https://dummyjson.com/products";
        animationView.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // animationView.setVisibility(View.GONE);
                Log.d("serverRes", response.toString());

                try {
                    //animationView.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("products");


                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);

                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String price = jsonObject.getString("price");
                        String description = jsonObject.getString("description");
                        String category = jsonObject.getString("category");
                        String discountPercentage = jsonObject.getString("discountPercentage");
                        String stock = jsonObject.getString("stock");
                        String weight = jsonObject.getString("weight");
                        String thumbnail = jsonObject.getString("thumbnail");
                        String returnPolicy = jsonObject.getString("returnPolicy");
                        String warrantyInformation = jsonObject.getString("warrantyInformation");
                        String shippingInformation = jsonObject.getString("shippingInformation");
                        String availabilityStatus = jsonObject.getString("availabilityStatus");
                        // String ratingg =jsonObject.getString("ratingg");


                        String[] images = new String[]{jsonObject.getString("images")};
                        String imagesArray = Arrays.toString(images);

                        // "images" হচ্ছে JSONArray, তাই প্রথম ইমেজটা নিতে হবে
                        JSONArray imageArray = jsonObject.getJSONArray("images");
                        String image = imageArray.getString(0); // প্রথম ইমেজ নিচ্ছি


// -----------------------------------------------------------------------------------------------------------------

                        JSONArray reviewsArray = jsonObject.getJSONArray("reviews");
                        String myReviews = "";


                        for (int i = 0; i < reviewsArray.length(); i++) {
                            JSONObject review = reviewsArray.getJSONObject(i);


                            String rating = review.getString("rating");
                            String comment = review.getString("comment");
                            String date = review.getString("date");
                            String reviewerName = review.getString("reviewerName");
                            String reviewerEmail = review.getString("reviewerEmail");


                            myReviews += "Rating : " + rating + "\n Comment: " + comment + "\n Date: " + date + "\n reviwerName: " + reviewerName + "\n reviewerEmail: " + reviewerEmail + "\n\n";
                        }


                        hashMap = new HashMap<>();

                        hashMap.put("id", id);
                        hashMap.put("title", title);
                        hashMap.put("thumbnail", thumbnail);
                        hashMap.put("price", price);
                        hashMap.put("description", description);
                        hashMap.put("category", category);
                        hashMap.put("discountPercentage", discountPercentage);
                        hashMap.put("stock", stock);
                        hashMap.put("weight", weight);
                        hashMap.put("returnPolicy", returnPolicy);
                        hashMap.put("warrantyInformation", warrantyInformation);
                        hashMap.put("availabilityStatus", availabilityStatus);
                        hashMap.put("shippingInformation", shippingInformation);
                        // hashMap.put("ratingg",ratingg);
                        hashMap.put("myReviews", myReviews);


                        hashMap.put("images", imagesArray);

                        arrayList.add(hashMap);

                    }


                    // ২ সেকেন্ডের delay দিয়ে অ্যানিমেশন hide
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationView.setVisibility(View.GONE);


                        }
                    }, 1500); // 2000 মিলিসেকেন্ড = ২ সেকেন্ড


                    MyAdapter myAdapter = new MyAdapter();
                    gridView.setAdapter(myAdapter);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                animationView.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);


        // ----------------------------------------------------------------------------------------------------------------


    }


    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.item, parent, false);

            ImageView imageTum = myView.findViewById(R.id.imageTum);
            TextView imageTitle = myView.findViewById(R.id.imageTitle);
            TextView imagePrice = myView.findViewById(R.id.imagePrice);
            Button btnAddToCart = myView.findViewById(R.id.btnAddToCart);

            HashMap<String, String> hashMap = arrayList.get(position);

            String title = hashMap.get("title");
            String thumbnail = hashMap.get("thumbnail");
            String price = hashMap.get("price");
            String description = hashMap.get("description");
            String category = hashMap.get("category");
            String discountPercentage = hashMap.get("discountPercentage");
            String stock = hashMap.get("stock");
            String weight = hashMap.get("weight");
            String returnPolicy = hashMap.get("returnPolicy");
            //String rating =hashMap.get("rating");
            String warrantyInformation = hashMap.get("warrantyInformation");
            String availabilityStatus = hashMap.get("availabilityStatus");
            String shippingInformation = hashMap.get("shippingInformation");
            String myReviews = hashMap.get("myReviews");


            String images = hashMap.get("images");


            Picasso.get()
                    .load(thumbnail)
                    .into(imageTum);

            imageTitle.setText(title);
            imagePrice.setText(price + " $");


            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity2.imgSliderArray = images;

                    MainActivity2.Description = description;
                    MainActivity2.Title = title;
                    MainActivity2.Price = price;
                    MainActivity2.DiscountPercent = discountPercentage;
                    MainActivity2.Category = category;
                    MainActivity2.Stock = stock;
                    MainActivity2.RetuenPolicy = returnPolicy;
                    MainActivity2.WrrantyInformation = warrantyInformation;
                    MainActivity2.ShippingInformation = shippingInformation;
                    MainActivity2.AvailabilityStatus = availabilityStatus;
                    //MainActivity2.Rating =rating;
                    MainActivity2.Reviews = myReviews;


                    startActivity(new Intent(MainActivity.this, MainActivity2.class));

                }
            });

            return myView;
        }
    } // ----------------------------------------MyAdapter End Here ---------------------------------------------


    //------------------------- No Internet-------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        Network.checkInternet(this);
    }

    //------------------------- No Internet End Here-------------------------------------


}