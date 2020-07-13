package com.example.mymall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mymall.RegisterActivity.setSignUpFragment;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT =0;
    private static final int CART_FRAGMENT =1;
    private static final int ORDERS_FRAGMENT =2;
    private static final int ACCOUNT_FRAGMENT =5;
    public static Boolean showCart = false;
    public static Activity main2Activity;


    private FrameLayout frameLayout;
    private ImageView noInternetConnection;
    private ImageView actionBarLogo;
    private int currentFragment = -1;
    private NavigationView navigationView;

    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFrags;
    private AppBarLayout.LayoutParams params;
    private CircleImageView profileView;
    private TextView fullname,email;
    private ImageView addProfileIcon;

    public static DrawerLayout drawer;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        scrollFrags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout= findViewById(R.id.main_framelayout);
        noInternetConnection = findViewById(R.id.no_internet_connection);
        profileView = findViewById(R.id.main_profile_image);
        fullname = findViewById(R.id.main_fullname);
        email = findViewById(R.id.main_email);
        addProfileIcon = findViewById(R.id.add_profile_icon);


        if (showCart) {
            main2Activity = this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("Giỏ hàng", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }

        signInDialog = new Dialog(Main2Activity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.cancel_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.ok_btn);
        final Intent registerIntent = new Intent(Main2Activity.this,RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }
        else if(currentUser !=null){
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DBqueries.fullname = task.getResult().get("fullname").toString();
                        DBqueries.email = task.getResult().get("email").toString();
                        DBqueries.profile = task.getResult().get("profile").toString();

                        profileView = findViewById(R.id.main_profile_image);
                        fullname = findViewById(R.id.main_fullname);
                        email = findViewById(R.id.main_email);
                        addProfileIcon = findViewById(R.id.add_profile_icon);

                        fullname.setText(DBqueries.fullname);
                        email.setText(DBqueries.email);
                        if (DBqueries.profile.equals("")){
                            addProfileIcon.setVisibility(View.VISIBLE);
                        }else {
                            addProfileIcon.setVisibility(View.INVISIBLE);

                            Glide.with(Main2Activity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.profile_placeholder)).into(profileView);
                        }

                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(Main2Activity.this,error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            }
            else {
                if(showCart){
                    main2Activity = null;
                    showCart = false;
                    finish();
                }
                else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main2, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.bagde_icon);
                badgeIcon.setImageResource(R.mipmap.cart_white);
                badgeCount = cartItem.getActionView().findViewById(R.id.bagde_count);
                if(currentUser != null){
                    if(DBqueries.cartList.size() == 0){
                        DBqueries.loadCartList(Main2Activity.this, new Dialog(Main2Activity.this),false,badgeCount,new TextView(Main2Activity.this));
                    }
                    else {
                        badgeCount.setVisibility(View.VISIBLE);
                        if(DBqueries.cartList.size() < 99){
                            badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                        }
                        else {
                            badgeCount.setText("99");
                        }
                    }
                }

                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentUser == null){
                            signInDialog.show();
                        }
                        else {
                            gotoFragment("Giỏ Hàng",new MyCartFragment(), CART_FRAGMENT);
                        }
                    }
                });
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
         if(id==R.id.main_search_icon){
             //todo: search
             return true;
         }
         else if(id==R.id.main_notification_icon){
             //todo: notification
             return true;
         }
         else if(id==R.id.main_cart_icon){
             if(currentUser==null) {
                 signInDialog.show();
             }
             else {
                 gotoFragment("Giỏ hàng",new MyCartFragment(),CART_FRAGMENT);
             }
             return true;
         }
         else if (id == android.R.id.home){
             if(showCart){
                 main2Activity = null;
                 showCart = false;
                 finish();
                 return true;
             }
         }

         return  super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title,Fragment fragment,int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if(fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else {
            params.setScrollFlags(scrollFrags);
        }
    }

    MenuItem menuItem;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public  boolean onNavigationItemSelected(MenuItem item){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        if(currentUser != null) {

            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_my_mall) {
                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_my_orders) {
                        gotoFragment("Danh sách hơn hàng", new MyOrdersFragment(), ORDERS_FRAGMENT);
                    } else if (id == R.id.nav_my_rewards) {

                    } else if (id == R.id.nav_my_cart) {
                        gotoFragment("Giỏ hàng", new MyCartFragment(), CART_FRAGMENT);
                    } else if (id == R.id.nav_my_wishlist) {

                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("Tài khoản", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                    } else if (id == R.id.nav_sign_out) {
                        FirebaseAuth.getInstance().signOut();
                        Intent registerIntent = new Intent(Main2Activity.this,RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                }
            });
            return true;
        }
        else {
            signInDialog.show();
            return false;
        }
    }

    private  void  setFragment(Fragment fragment,int fragmentNo){
        if(fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
}
