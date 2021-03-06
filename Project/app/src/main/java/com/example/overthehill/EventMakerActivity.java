package com.example.overthehill;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventMakerActivity<calendarView> extends AppCompatActivity {

    private EditText eventName, eventDescription;
    private CalendarView calendarView;
    private MapView mapView;
    private Button makeEvent;
    private Switch sponsored;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_maker);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.maker);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), SponsoredEventsActivity.class));
                        overridePendingTransition(0,0 );
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0 );
                        return true;
                    case R.id.events:
                        startActivity(new Intent(getApplicationContext(), EventMakerActivity.class));
                        overridePendingTransition(0,0 );
                        return true;

                    case R.id.maker:

                        return true;

                }

                return false;
            }
        });

        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        mapView = findViewById(R.id.mapView);
        calendarView = findViewById(R.id.calendarView);
        makeEvent = findViewById((R.id.makeEvent));
        sponsored = findViewById(R.id.sponsored);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        makeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventType;
                if(sponsored.isChecked()){
                    eventType = "sponsoredEvents";
                }
                else {
                    eventType = "events";
                }


                Map<String,Object> event = new HashMap<>();
                event.put("Title",eventName.getText().toString());
                event.put("Description",eventDescription.getText().toString());
                event.put("date",(int) calendarView.getDate());


                db.collection(eventType)
                        .add(event)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                clearFeilds();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void clearFeilds(){
        eventName.clearComposingText();
        eventDescription.clearComposingText();
    }

}
