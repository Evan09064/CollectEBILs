package com.opsc.collectebils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser user;
    private DatabaseReference ref;
    private String userId;
    private Button logout_button;

    public  BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Spinner dropdown = findViewById(R.id.collectionLayout);
        String[] items = new String[]{"List View", "Thumbnail View"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        logout_button = (Button) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView full_name_view = (TextView) findViewById(R.id.full_name_view);
        //final TextView email_address_view = (TextView) findViewById(R.id.email_address_view);

        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String fullName = userProfile.fullName;
                    String emailAddress = userProfile.emailAddress;

                    full_name_view.setText(fullName);
                    //email_address_view.setText(emailAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Initialize and assign variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_collections:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        startActivity(new Intent(getApplicationContext(), MyCollectionsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.explore:
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.marketplace:
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        startActivity(new Intent(getApplicationContext(), MarketplaceActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.dashboard:
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_button:
                LogoutUser();
                break;
        }
    }

    private void LogoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}