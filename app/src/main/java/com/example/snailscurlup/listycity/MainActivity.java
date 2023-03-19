package com.example.snailscurlup.listycity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    com.example.snailscurlup.listycity.CustomList customList;

    final String TAG = "Sample";
    Button addAccountButton;
    Button checkAccountButton;

    FirebaseFirestore db;
    private DatabaseReference mDatabase;


    String random_user ="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAccountButton = findViewById(R.id.buttonQRList);   //button
        checkAccountButton = findViewById(R.id.buttonUserList);



        userList = findViewById(R.id.user_list);
        userDataList = new ArrayList<>();
        userAdapter = new com.example.snailscurlup.listycity.CustomList(this, userDataList);
        userList.setAdapter(userAdapter);


        db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("Users");


        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query User_leaderboard = db.collection("Users").orderBy("Total Score", Query.Direction.DESCENDING).limit(5);
                User_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        userDataList.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            Log.d(TAG, String.valueOf(doc.getData().get("Email")));
                            String city = doc.getId();
                            String province = (String) doc.getData().get("Email");
                            String phone = (String) doc.getData().get("PhoneNumber");
                            userDataList.add(new User(city, province, phone));
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });


            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userDataList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Email")));
                    String city = doc.getId();
                    String province = (String) doc.getData().get("Email");
                    String phone = (String) doc.getData().get("PhoneNumber");
                    userDataList.add(new User(city, province, phone));
                }
                userAdapter.notifyDataSetChanged();
            }
        });


        //checks how many QR codes they own
        CollectionReference QRListReference = db.collection("Users").document(random_user).collection("QRList");



        checkAccountButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //TAKE THIS QUERY WITHOUT THE ONCLICK
                  AggregateQuery countQuery = QRListReference.count();   //Reference needs to be set
                  countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
                      if (task.isSuccessful()) {
                          AggregateQuerySnapshot snapshot = task.getResult();
                          int result= (int) snapshot.getCount() -1;
                          Log.d(TAG, "Count: " + result);
                      } else {
                          Log.d(TAG, "Count failed: ", task.getException());
                      }
                  });
                  //TO HERE
              }
          }


        );



    }
}
