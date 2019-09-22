package com.example.shinycleaning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ActivityHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNav;

    private CardView reservationCD, ongoingServiceCD, viewReqCD, chatRoomCD;
    private Button vProfileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        reservationCD = findViewById(R.id.makeReservationCD);
        ongoingServiceCD = findViewById(R.id.ongoingServiceCD);
        viewReqCD = findViewById(R.id.viewReqCD);
        chatRoomCD = findViewById(R.id.chatRoomCD);
        vProfileBtn = findViewById(R.id.profileBtn);

        reservationCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resIntent = new Intent(ActivityHomePage.this, ServiceReservation.class);
                startActivity(resIntent);
            }
        });

        chatRoomCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(ActivityHomePage.this, MessageHome.class);
                startActivity(chatIntent);
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_my_profile:

//                Intent i = new Intent(MainPageActivity.this, UserProfileActivity.class);
//                i.putExtra("profile","user");
//
//                startActivity(i);

                break;

            case R.id.nav_logout:

                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
