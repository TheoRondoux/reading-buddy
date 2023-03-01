
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

import com.example.myonlinebookself.items.Book;
import com.example.myonlinebookself.recycler.MyAdapterShop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RBShop extends AppCompatActivity {
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        if (getSupportActionBar() != null) {            //If title bar exists
            getSupportActionBar().hide();               //Hiding it
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);    //Retrieving navigation menu
        bottomNavigationView.setSelectedItemId(R.id.shop);

        List<Book> rentBook = new ArrayList<>();

        //Creating and showing a waiting dialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading all books...");
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();     //Getting database instance
        db.collection("Livre")          //Looking for the "Livre" table, containing all books
                .get()                              //Getting the data of all books in the table
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {       //If the query is complete
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){                //Adding all the books in the list
                                String uri = "@drawable/" + document.getString("image_name");
                                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                                rentBook.add(new Book(document.getId(), document.getString("title"), document.getString("author"), document.getString("description"), document.getString("details"), imageResource));
                            }
                            dialog.dismiss(); //Once all the books have been loaded, deleting the loading dialog

                            //Logic for the navigation menu
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
                                            startActivity(new Intent(getApplicationContext(), RBAccount.class));
                                            overridePendingTransition(0,0);
                                            return true;
                                    }
                                    return false;
                                }
                            });
                            RecyclerView myRecyclerView2 = findViewById(R.id.recycleview2);
                            myRecyclerView2.setLayoutManager(new LinearLayoutManager(RBShop.this));
                            myRecyclerView2.setAdapter(new MyAdapterShop(getApplicationContext(),rentBook));        //Giving all the books to the recycler view
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
        });


    }
}