package com.example.mymall;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import static com.example.mymall.RegisterActivity.setSignUpFragment;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT =0;
    private static final int CART_FRAGMENT =1;
    private static final int ORDERS_FRAGMENT =2;
    private static final int ACCOUNT_FRAGMENT =5;
    public static Boolean showCart = false;

    private FrameLayout frameLayout;
    private ImageView noInternetConnection;
    private ImageView actionBarLogo;
    private int currentFragment = -1;
    private NavigationView navigationView;

    private Window window;
    private Toolbar toolbar;
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


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout= findViewById(R.id.main_framelayout);
        noInternetConnection = findViewById(R.id.no_internet_connection);

        if (showCart) {
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

             final Dialog signInDialog = new Dialog(Main2Activity.this);
             signInDialog.setContentView(R.layout.sign_in_dialog);
             signInDialog.setCancelable(true);
             signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

             Button dialogSignInBtn = signInDialog.findViewById(R.id.cancel_btn);
             Button dialogSignUpBtn = signInDialog.findViewById(R.id.ok_btn);
             final Intent registerIntent = new Intent(Main2Activity.this,RegisterActivity.class);

             dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     signInDialog.dismiss();
                     setSignUpFragment = false;
                     startActivity(registerIntent);
                 }
             });
             signInDialog.show();

             dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     signInDialog.dismiss();
                     setSignUpFragment = true;
                     startActivity(registerIntent);
                 }
             });

             //gotoFragment("Giỏ hàng",new MyCartFragment(),CART_FRAGMENT);
             return true;
         }
         else if (id == android.R.id.home){
             if(showCart){
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
        if(fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public  boolean onNavigationItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.nav_my_mall){
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            setFragment(new HomeFragment(),HOME_FRAGMENT);
        }
        else if(id==R.id.nav_my_orders){
            gotoFragment("Danh sách hơn hàng",new MyOrdersFragment(),ORDERS_FRAGMENT);
        }
        else if(id==R.id.nav_my_rewards){

        }
        else if(id==R.id.nav_my_cart){
            gotoFragment("Giỏ hàng",new MyCartFragment(),CART_FRAGMENT);
        }
        else if(id==R.id.nav_my_wishlist){

        }
        else if(id==R.id.nav_my_account){
            gotoFragment("Tài khoản",new MyAccountFragment(),ACCOUNT_FRAGMENT);
        }
        else if(id==R.id.nav_sign_out){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
