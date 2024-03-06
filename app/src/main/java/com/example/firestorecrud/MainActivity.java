package com.example.firestorecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Declare FirebaseFirestore and RecyclerView as class members to access them in onResume
    private FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);

        db = FirebaseFirestore.getInstance(); // Initialize it here for class-wide use
        recyclerView = findViewById(R.id.recycler); // Initialize it here for class-wide use

        FloatingActionButton add = findViewById(R.id.addUser);
        add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddUserActivity.class)));

        FloatingActionButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshUsers();
            }
        });

        // New code for register redirection button
        Button registerRedirect = findViewById(R.id.loginRedirect);
        registerRedirect.setOnClickListener(view -> redirectToLogin());
    }

    // This method will be called when coming back to MainActivity
    @Override
    protected void onResume() {
        super.onResume();
        // Call the method to refresh users here
        refreshUsers();
    }

    // Extracted method to refresh users
    private void refreshUsers() {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> arrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        user.setId(document.getId());
                        arrayList.add(user);
                    }
                    UserAdapter adapter = new UserAdapter(MainActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(User user) {
                            App.user = user;
                            startActivity(new Intent(MainActivity.this, EditUserActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // New method for redirecting to RegistrationActivity
    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
