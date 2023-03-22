package com.example.snailscurlup.listycity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView userList;
    ArrayAdapter<User> userAdapter;
    ArrayList<User> userDataList;
    Button cumulativeButton;
    Button TopQrButton;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cumulativeButton = findViewById(R.id.buttonUserList);   //button
        TopQrButton = findViewById(R.id.buttonQRList);

        userList = findViewById(R.id.user_list);
        userDataList = new ArrayList<>();
        userAdapter = new com.example.snailscurlup.listycity.CustomList(this, userDataList);
        userList.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();

        Query User_leaderboard = db.collection("Users").orderBy("Total Score", Query.Direction.DESCENDING);
        Query QR_leaderboard = db.collection("QR").orderBy("score", Query.Direction.DESCENDING);

        cumulativeButton.setBackgroundColor(Color.YELLOW);
        TopQrButton.setBackgroundColor(Color.CYAN);
        User_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String userName = doc.getId();
                    String Score = (String) doc.getData().get("Total Score");
                    String phone = (String) doc.getData().get("PhoneNumber");
                    userDataList.add(new User(userName, Score, phone));
                }
                userAdapter.notifyDataSetChanged();
            }
        });
        cumulativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cumulativeButton.setBackgroundColor(Color.YELLOW);
                TopQrButton.setBackgroundColor(Color.CYAN);
                User_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        userDataList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String userName = doc.getId();
                            String Score = (String) doc.getData().get("Total Score");
                            String phone = (String) doc.getData().get("PhoneNumber");
                            userDataList.add(new User(userName, Score, phone));
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        TopQrButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  TopQrButton.setBackgroundColor(Color.YELLOW);
                  cumulativeButton.setBackgroundColor(Color.CYAN);
                  QR_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
                      @Override
                      public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                          userDataList.clear();
                          for (QueryDocumentSnapshot doc : value) {
                              String city = doc.getId();
                              String province = (String) doc.getData().get("score");
                              String phone = (String) doc.getData().get("PhoneNumber");
                              userDataList.add(new User(city, province, phone));
                          }
                          userAdapter.notifyDataSetChanged();
                      }
                  });
              }
          }
        );





    }
}
