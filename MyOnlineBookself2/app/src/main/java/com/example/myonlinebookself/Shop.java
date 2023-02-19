package com.example.myonlinebookself;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.shop);

        List<Item> rentBook = new ArrayList<>();
        rentBook.add(new Item("Harry Potter 1","J.K Rowling","Harry Potter à l école des sorciers. Je teste pour savoir si la vue va s'adapter à l ecran ou si cela va empiter sur le bouton","edezdkzdkz",R.drawable.harry_1));
        rentBook.add(new Item("Harry Potter 2","J.K Rowling","Harry Potter et la chambre des secrets.","izjfzfzofjz",R.drawable.harry_2));
        rentBook.add(new Item("Harry Potter 3","J.K Rowling","Harry Potter et le prisonnier d'Azkaban.","zjedezejzeojz",R.drawable.harry_3));


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
        myRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView2.setAdapter(new MyAdapterShop(getApplicationContext(),rentBook));
    }
}