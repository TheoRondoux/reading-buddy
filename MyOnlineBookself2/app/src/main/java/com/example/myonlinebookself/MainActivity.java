package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myonlinebookself.items.Book;
import com.example.myonlinebookself.recycler.MyAdapterBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {            //If title bar exists
            getSupportActionBar().hide();               //Hiding it
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.books);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading your books...");
        dialog.show();
        getOwnedBooks(bottomNavigationView);
        dialog.dismiss();


    }

    /**
     *  Method to add the owned books to the Recycle View
     *
     * @param bottomNavigationView is the navigation mennu
     * @param db is the instance of the database
     * @param document is one of the results of a task
     * @param ownedBooks is the list of all the books owned by the logged user
     * */
    public void showOwnedBooks(FirebaseFirestore db, QueryDocumentSnapshot document, List<Book> ownedBooks, BottomNavigationView bottomNavigationView){
        db.collection("Livre")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                        {

                            if (documentSnapshot.getId().equals(document.getString("bookId"))){
                                String uri = "@drawable/" + documentSnapshot.getString("image_name");
                                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                ownedBooks.add(new Book(documentSnapshot.getId(), documentSnapshot.getString("title"), documentSnapshot.getString("author"), documentSnapshot.getString("description"), documentSnapshot.getString("details"), imageResource));
                            }
                        }
                        buildRecycleView(bottomNavigationView, ownedBooks);
                    }
                }
            });
    }

    public void buildRecycleView(BottomNavigationView bottomNavigationView, List<Book> ownedBooks){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), MOBShop.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.books:
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), MOBAccount.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new MyAdapterBook(getApplicationContext(),ownedBooks));
    }

    /**
     * Method that retrieve all the books owned by the logged user.
     *
     * @param bottomNavigationView is the navigation menu
     * */
    public void getOwnedBooks(BottomNavigationView bottomNavigationView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();         //Getting an instance of the database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();                //Getting instance of the Firebase authentication system

        List<Book> ownedBooks = new ArrayList<>();

        db.collection("OwnedBooks")                          //Looking into the "OwnedBooks" table
                .whereEqualTo("userId",mAuth.getUid())              //And searching all the books of the logged user
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)                 //If the user has at least one book read, displays it
                        {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                showOwnedBooks(db, document, ownedBooks, bottomNavigationView);
                            }
                        }
                        else if (task.isSuccessful() && task.getResult().size() == 0) {         //else, redirecting to the list of all books
                            Toast.makeText(MainActivity.this, "No books in you library...", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, MOBShop.class));
                        }
                        else {
                            Log.d(TAG, "Error while looking for owned books");
                        }
                    }
                });

    }
}

/*
Le code utilise la classe "RecyclerView" pour créer une vue défilante qui affiche les éléments de la liste.
D'abord, il récupère une instance de la vue défilante à partir de son identifiant de ressource, "recycleview",
à l'aide de la méthode "findViewById". Ensuite, il crée une nouvelle instance de la classe "LinearLayoutManager"
pour organiser les éléments de la liste en une disposition de type liste verticale.

Enfin, il crée une instance de la classe "MyAdapter" pour fournir les données d'éléments de la liste à la vue défilante et
l'associe à la vue défilante à l'aide de la méthode "setAdapter". La méthode "getApplicationContext()" est utilisée
pour obtenir le contexte de l'application à partir de l'activité en cours.
 */