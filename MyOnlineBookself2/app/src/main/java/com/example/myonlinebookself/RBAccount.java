package com.example.myonlinebookself;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RBAccount extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();    //Getting instance of the Firebase authentication system
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (getSupportActionBar() != null) {            //If title bar exists
            getSupportActionBar().hide();               //Hiding it
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.account);

        //Logic for the navigation menu
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), RBShop.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.books:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        return true;
                }
                return false;
            }
        });

        TextView mUsernameTextView = findViewById(R.id.username_textview);
        TextView mBookReadTextView = findViewById(R.id.books_read_textview);
        mUsernameTextView.setText("Hello " + mAuth.getCurrentUser().getEmail() + "!");
        countNumberOfBooksRead(mBookReadTextView);
    }

    /**
     * Method called when the user clicks on the Logout button.
     * */
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(RBAccount.this, RBLoginActivity.class);
        // set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        logout();
    }

    /**
     * Method to count all the books read by the user and displays the result in a TextView.
     *
     * @param textView is the TextView where the number of books read will be displayed
     * */
    public void countNumberOfBooksRead(TextView textView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();         //Getting instance of the database
        db.collection("OwnedBooks")         //For the table "OwnedBooks"
                .get()                                  //Getting all the books
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int counter = 0;
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if (document.getString("userId").equals(mAuth.getUid()) )       //If the books belongs to the user
                                {
                                    counter++;                                                       //Increasing the counter
                                }
                            }
                            textView.setText("Number of books read: "+counter);
                        }
                    }
                });
    }
}