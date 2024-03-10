package com.example.firestorecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.DatePickerDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddReservationActivity extends AppCompatActivity {

    private Timestamp selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputEditText firstNameET = findViewById(R.id.firstNameET);
        TextInputEditText lastNameET = findViewById(R.id.lastNameET);
        TextInputEditText phoneET = findViewById(R.id.phoneET);
        TextInputEditText bioET = findViewById(R.id.bioET);
        MaterialButton addReservation = findViewById(R.id.addReservation);
        TextInputEditText guestCountET = findViewById(R.id.guestCountET);
        MaterialButton selectDateButton = findViewById(R.id.selectDateButton);

        selectDateButton.setOnClickListener(view -> showDatePickerDialog());

        addReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extracting the text values and trimming to remove leading and trailing spaces
                String firstName = Objects.requireNonNull(firstNameET.getText()).toString().trim();
                String lastName = Objects.requireNonNull(lastNameET.getText()).toString().trim();
                String phone = Objects.requireNonNull(phoneET.getText()).toString().trim();
                String bio = Objects.requireNonNull(bioET.getText()).toString().trim();
                String guestCountStr = Objects.requireNonNull(guestCountET.getText()).toString().trim();

                // Check if any field is empty
                if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || bio.isEmpty() || guestCountStr.isEmpty() || selectedDate == null) {
                    Toast.makeText(AddReservationActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
                    return; // Stop the execution if any field is empty
                }

                // Parse and add guest count
                int guestCount = guestCountStr.isEmpty() ? 0 : Integer.parseInt(guestCountStr);

                // Preparing the data to save
                Map<String, Object> reservation = new HashMap<>();
                reservation.put("firstName", firstName);
                reservation.put("lastName", lastName);
                reservation.put("phone", phone);
                reservation.put("bio", bio);
                reservation.put("guestCount", guestCount);
                reservation.put("date", selectedDate); // No need to check for null here since we've already checked

                // Get the current user's UID and include it in the reservation map
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) { // Check if the user is logged in
                    String uid = currentUser.getUid();
                    reservation.put("userId", uid); // Add the user's UID to the reservation map
                } else {
                    Toast.makeText(AddReservationActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if no user is logged in
                }

                // Proceed to add reservation to Firestore
                db.collection("reservations").add(reservation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddReservationActivity.this, "Reservation added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddReservationActivity.this, "There was an error while adding the reservation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, year1);
                    selectedCalendar.set(Calendar.MONTH, monthOfYear);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectedDate = new Timestamp(selectedCalendar.getTime());
                    Toast.makeText(AddReservationActivity.this, "Date Selected: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, Toast.LENGTH_SHORT).show();
                }, year, month, day);
        datePickerDialog.show();
    }
}
