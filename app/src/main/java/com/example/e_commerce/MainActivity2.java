package com.example.e_commerce;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.transition.Slide;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {


    public static String imgSliderArray = "";
    ArrayList<SlideModel> imageList = new ArrayList<>();


    TextView productDescription, productPrice, productDiscount, productTitle,
            productCategory, productStock, productReturnpolicy,
            productRating, productShipping, productAvailability, productWarranty;


    TextView productReviews;

    Button btn_buy, btn_add_cart;


    public static String Description = "";
    public static String Price = "";
    public static String DiscountPercent = "";
    public static String Title = "";
    public static String Category = "";
    public static String Stock = "";
    public static String RetuenPolicy = "";
    public static String WrrantyInformation = "";
    public static String ShippingInformation = "";
    public static String AvailabilityStatus = "";


    public static String Reviews = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageSlider imageSlider = findViewById(R.id.imagSlider); //-----------Image slider Array
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productDiscount = findViewById(R.id.productDiscount);
        productTitle = findViewById(R.id.productTitle);
        productCategory = findViewById(R.id.productCategory);
        productStock = findViewById(R.id.productStock);
        productReturnpolicy = findViewById(R.id.productReturnPolicy);
        productWarranty = findViewById(R.id.productWarranty);
        productShipping = findViewById(R.id.productShipping);
        productAvailability = findViewById(R.id.productAvailability);
        productReviews = findViewById(R.id.productReviews);

        btn_add_cart = findViewById(R.id.btn_add_cart);
        btn_buy = findViewById(R.id.btn_buy);


// -----------------------------------------------------------------------------------------------------
        try {
            JSONArray jsonArray = new JSONArray(imgSliderArray);
            if (jsonArray.length() > 0) {
                JSONArray imageLinkArray = jsonArray.getJSONArray(0);

                for (int i = 0; i < imageLinkArray.length(); i++) {
                    String imageLink = imageLinkArray.getString(i);
                    imageList.add(new SlideModel(imageLink, null));
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        imageSlider.setImageList(imageList);
// ----------------------------------------------------------------------------------------------------------


        productDescription.setText(Description);
        productPrice.setText(Price + " $");
        productDiscount.setText(DiscountPercent + "%" + " Discount");
        productTitle.setText(Title);
        productCategory.setText(Category);
        productStock.setText(Stock + "Item Availabile");
        productReturnpolicy.setText(RetuenPolicy);
        productWarranty.setText(WrrantyInformation);
        productShipping.setText(ShippingInformation);
        productAvailability.setText(AvailabilityStatus);

        productReviews.setText(Reviews);


        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "Add To cart Successfull", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //------------------------- No Internet-------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        Network.checkInternet(this);
    }

    //------------------------- No Internet End Here-------------------------------------

}