package com.example.sarah.nav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String email = "";
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets the action bar as this toolbar
        Toolbar t = findViewById(R.id.toolbar);
        setSupportActionBar(t);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.EMAIL);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, t, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new ProfileFragment()).commit();
                break;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new HomeFragment()).commit();
                break;
            case R.id.wishlist:

                Intent wishlist=new Intent(MainActivity.this, WishList.class);
                startActivity(wishlist);
                finish();
                break;
            case R.id.order:
                Intent order=new Intent(MainActivity.this, OrderCart.class);
                startActivity(order);
                finish();
                break;
            case R.id.history:
                Intent history=new Intent(MainActivity.this, OrderHistory.class);
                startActivity(history);
                finish();
                break;
            case R.id.logout:
                sessionManager.logout();
                break;
            case R.id.about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new AboutFragment()).commit();
                break;

            case R.id.call:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new CallFragment()).commit();
                Uri u=Uri.parse("tel:123456789");
                Intent callIntent = new Intent(Intent.ACTION_DIAL,u);
                //callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);
                break;

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                Toast.makeText(this, "Share Karo,Khush Raho", Toast.LENGTH_SHORT).show();
                break;

            case R.id.add:

                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}