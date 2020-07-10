package com.example.mymall;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mymall.Main2Activity.showCart;
import static com.example.mymall.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_cart_query = false;

    public static String productID;
    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRattingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;

    private LinearLayout coupenRedemptionLayout;

    private TextView rewardTitle;
    private TextView rewardBody;

    /////pro description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    private TextView productOnlyDescriptionBody;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    //////pro description

    /////////rating layout
    private LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    /////////rating layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    public static boolean AlREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton addToWishListBtn;

    private FirebaseFirestore firebaseFirestore;

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    private DocumentSnapshot documentSnapshot;
    // private String productID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        productTitle = findViewById(R.id.product_title);
        averageRattingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_rating_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn =findViewById(R.id.add_to_cart_btn);
        coupenRedemptionLayout =findViewById(R.id.coupe_redemption_layout);

        //////loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog

        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    for(long x =1;x<(long)documentSnapshot.get("no_of_product_images") +1;x++){
                        productImages.add(documentSnapshot.get("product_image_"+x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRattingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+") đánh giá");
                    productPrice.setText(documentSnapshot.get("product_price")+"VND");
                    cuttedPrice.setText(documentSnapshot.get("cutted_price")+"VND");
                    if((boolean)documentSnapshot.get("COD")){
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    }
                    else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long)documentSnapshot.get("free_coupens")+documentSnapshot.get("free_coupen_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                    if((boolean)documentSnapshot.get("use_tab_layout")){
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();
                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                        for(long x = 1; x<(long)documentSnapshot.get("total_spec_title")+1;x++){
                            productSpecificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                            for(long y = 1;y< (long)documentSnapshot.get("spec_title_"+x+"_total_fields")+1;y++){
                                productSpecificationModelList.add(new ProductSpecificationModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                            }
                        }
                    }
                    else{
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }
                    totalRatings.setText((long)documentSnapshot.get("total_ratings")+"Đánh giá");

                    for(int x = 0;x< 5;x++){
                        TextView rating = (TextView)ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long)documentSnapshot.get( (5-x) +"_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get( (5-x) +"_star"))));

                    }
                    totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));
                    if(currentUser != null) {
                        if (DBqueries.wishList.size() == 0) {
                            DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog);
                        }
                        if(DBqueries.cartList.size() ==0){
                            DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false);
                        }
                        else {
                            loadingDialog.dismiss();
                        }
                    }
                    else {
                        loadingDialog.dismiss();
                    }
                    if(DBqueries.wishList.contains(productID)){
                        AlREADY_ADDED_TO_WISHLIST = true;
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }else {
                        AlREADY_ADDED_TO_WISHLIST = false;
                    }

                    if(DBqueries.cartList.contains(productID)){
                        ALREADY_ADDED_TO_CART = true;
                    }else {
                        ALREADY_ADDED_TO_CART = false;
                    }
                }
                else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null){
                    signInDialog.show();
                }
                else {
                    if (AlREADY_ADDED_TO_WISHLIST) {
                        AlREADY_ADDED_TO_WISHLIST = false;
                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {

                        Map<String,Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_"+String.valueOf(DBqueries.wishList.size()),productID);
                        addProduct.put("list_size", (long) (DBqueries.wishList.size()+1));
                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                .set(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Map<String,Object> updateListSize = new HashMap<>();
                                    updateListSize.put("list_size", (long) (DBqueries.wishList.size()+1));

                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                            .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //////////////////////////xxx
//                                                if(DBqueries.wishlistModelList.size()!=0){
//                                                    DBqueries.wishlistModelList.add(new ViewAllProduct(documentSnapshot.get("product_image_1").toString(),
//                                                            documentSnapshot.get("product_title").toString(),
//                                                            documentSnapshot.get("average_rating").toString(),
//                                                            (long)documentSnapshot.get("total_rating"),
//                                                            documentSnapshot.get("product_price").toString(),
//                                                            documentSnapshot.get("cutted_price").toString(),
//                                                            (boolean)documentSnapshot.get("COD")));
//                                                }
///////////////////////////////////xxxx
                                                AlREADY_ADDED_TO_WISHLIST = true;
                                                addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                DBqueries.wishList.add(productID);
                                                Toast.makeText(ProductDetailsActivity.this,"Thêm vào danh sách yêu thích",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ///////rating Layout
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0;x < rateNowContainer.getChildCount();x++){
            final int startPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUser == null){
                        signInDialog.show();
                    }
                    else {
                        setRating(startPosition);
                    }
                }
            });
        }
        //////rating Layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    signInDialog.show();
                }
                else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if(!running_cart_query){
                        running_cart_query = true;
                        if(ALREADY_ADDED_TO_CART){
                            running_cart_query = false;
                            Toast.makeText(ProductDetailsActivity.this,"Đã thêm vào giỏ hàng !",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String,Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_"+String.valueOf(DBqueries.cartList.size()),productID);
                            addProduct.put("list_size",(long)(DBqueries.cartList.size()+1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.cartItemModelList.size() != 0) {
                                            DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID,documentSnapshot.get("product_image_1").toString(),
                                                    documentSnapshot.get("product_title").toString(),
                                                    (long) documentSnapshot.get("free_coupens"),
                                                    documentSnapshot.get("product_price").toString(),
                                                    documentSnapshot.get("cutted_price").toString(),
                                                    (long) 1,
                                                    (long) 0,
                                                    (long) 0));
                                        }
                                        ALREADY_ADDED_TO_CART = true;
                                        DBqueries.cartList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Được thêm vào giỏ hàng thành công !", Toast.LENGTH_SHORT).show();
                                        invalidateOptionsMenu();
                                        running_cart_query = false;
                                    } else {
                                        running_cart_query = false;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        /////signinDialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.cancel_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.ok_btn);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this,RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn= true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn= true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        /////signinDialog

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            coupenRedemptionLayout.setVisibility(View.GONE);
        }
        else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);

        }
        if (currentUser !=null){
            if(DBqueries.cartList.size() ==0){
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog);
            }
            else {
                loadingDialog.dismiss();
            }
        }
        else {
            loadingDialog.dismiss();
        }
        if(DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CART = true;
        }else {
            ALREADY_ADDED_TO_CART = false;
        }
        if(DBqueries.wishList.contains(productID)){
            AlREADY_ADDED_TO_WISHLIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        }else {
            AlREADY_ADDED_TO_WISHLIST = false;
        }
    }

    private void setRating(int startPosition) {
        for (int x = 0;x<rateNowContainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(x <= startPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
         cartItem = menu.findItem(R.id.main_cart_icon);
        if(DBqueries.cartList.size() >0){
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.bagde_icon);
            badgeIcon.setImageResource(R.mipmap.cart_white);
            TextView badgeCount = cartItem.getActionView().findViewById(R.id.bagde_count);
            if(DBqueries.cartList.size()<99) {
                badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
            }else {
                badgeCount.setText("99");
            }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUser == null){
                        signInDialog.show();
                    }
                    else {
                        Intent cartIntent = new Intent(ProductDetailsActivity.this, Main2Activity.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });
        }else {
            cartItem.setActionView(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        else if(id==R.id.main_search_icon){
            //todo:search
            return true;
        }
        else if(id==R.id.main_cart_icon){
                if(currentUser == null){
                    signInDialog.show();
                }
                else {
                    Intent cartIntent = new Intent(ProductDetailsActivity.this, Main2Activity.class);
                    showCart = true;
                    startActivity(cartIntent);
                    return true;
                }
        }

        return  super.onOptionsItemSelected(item);
    }
}

