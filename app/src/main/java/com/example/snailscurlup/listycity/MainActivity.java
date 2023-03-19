package com.example.snailscurlup.listycity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView userList;
    ArrayAdapter<User> userAdapter;
    ArrayList<User> userDataList;

    com.example.snailscurlup.listycity.CustomList customList;

    final String TAG = "Sample";
    Button addAccountButton;
    Button checkAccountButton;
    EditText addUsername;
    EditText addEmail;
    EditText addPhoneNumber;
    FirebaseFirestore db;
    private DatabaseReference mDatabase;


    String random_user ="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAccountButton = findViewById(R.id.add_account_button);   //button
        checkAccountButton = findViewById(R.id.check_button);


        //textfields
        addUsername = findViewById(R.id.add_username);
        addEmail = findViewById(R.id.add_email);
        addPhoneNumber = findViewById(R.id.add_phone_number);


        userList = findViewById(R.id.user_list);
        userDataList = new ArrayList<>();
        userAdapter = new com.example.snailscurlup.listycity.CustomList(this, userDataList);
        userList.setAdapter(userAdapter);


        db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("Users");


        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = addUsername.getText().toString();
                final String email = addEmail.getText().toString();
                final String phoneNumber = addPhoneNumber.getText().toString();


                //makes user fields
                HashMap<String, String> data = new HashMap<>();
                if (userName.length() > 0) {  //if (userName.length() > 0 && email.length() > 0 && phoneNumber.length() > 0)
                    data.put("Email", email);
                    data.put("PhoneNumber", phoneNumber);
                    data.put("Total Score", "0");
                    data.put("Codes Scanned","0");

                    collectionReference
                            .document(userName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.d(TAG, "Data not be added!" + e.toString());
                                }
                            });
                    //clears user entry after adding
                    addUsername.setText("");
                    addEmail.setText("");
                    addPhoneNumber.setText("");
                    }
                //Make a subcollection to hold QRCode
                HashMap<String, String> empty = new HashMap<>();
                CollectionReference QRRef= db.collection("Users").document(userName).collection("QRList");

                QRRef
                        .document("wallet")
                        .set(new HashMap<>())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "Data not be added!" + e.toString());
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
