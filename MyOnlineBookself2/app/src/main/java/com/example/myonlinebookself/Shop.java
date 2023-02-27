package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.shop);

        List<Item> rentBook = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Livre")
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String uri = "@drawable/" + document.getString("image_name");
                                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                                rentBook.add(new Item(document.getId(), document.getString("title"), document.getString("author"), document.getString("description"), document.getString("details"), imageResource));
                            }
                            dialog.dismiss();
                            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                    switch (menuItem.getItemId()){
                                        case R.id.shop:
                                            return true;
                                        case R.id.books:
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            overridePendingTransition(0,0);
                                            return true;
                                        case R.id.account:
                                            startActivity(new Intent(getApplicationContext(),Account.class));
                                            overridePendingTransition(0,0);
                                            return true;
                                    }
                                    return false;
                                }
                            });
                            RecyclerView myRecyclerView2 = findViewById(R.id.recycleview2);
                            myRecyclerView2.setLayoutManager(new LinearLayoutManager(Shop.this));
                            myRecyclerView2.setAdapter(new MyAdapterShop(getApplicationContext(),rentBook));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
        });
    }
}