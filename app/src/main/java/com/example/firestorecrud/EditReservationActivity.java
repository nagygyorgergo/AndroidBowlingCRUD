package com.example.firestorecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditReservationActivity extends AppCompatActivity {

    private Timestamp selectedDate;
    private TextView currentDateTV; // TextView to display the current date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        currentDateTV = findViewById(R.id.currentDateTV); // Initialize the TextView
        // Assuming App.reservation.getDate() returns a Timestamp
        if (App.reservation.getDate() != null) {
            selectedDate = App.reservation.getDate();
            // Format and display the initial date
            updateDateTextView(selectedDate.toDate());
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputEditText firstNameET = findViewById(R.id.firstNameET);
        TextInputEditText lastNameET = findViewById(R.id.lastNameET);
        TextInputEditText phoneET = findViewById(R.id.phoneET);
        TextInputEditText bioET = findViewById(R.id.bioET);
        TextInputEditText guestCountET = findViewById(R.id.guestCountET); // New field for guest count
        MaterialButton selectDateButton = findViewById(R.id.selectDateButton); // Button for selecting date
        MaterialButton save = findViewById(R.id.save);
        MaterialButton delete = findViewById(R.id.delete);

        // Assume App.reservation includes appropriate getters
        firstNameET.setText(App.reservation.getFirstName());
        lastNameET.setText(App.reservation.getLastName());
        phoneET.setText(App.reservation.getPhone());
        bioET.setText(App.reservation.getBio());
        guestCountET.setText(String.valueOf(App.reservation.getGuestCount())); // Set guest count
        // Set up selectedDate with existing reservation date
        selectedDate = App.reservation.getDate(); // Ensure this is handled appropriately

        selectDateButton.setOnClickListener(view -> showDatePickerDialog());

        save.setOnClickListener(view -> {
            // Extracting text values and trimming spaces
            String firstName = Objects.requireNonNull(firstNameET.getText()).toString().trim();
            String lastName = Objects.requireNonNull(lastNameET.getText()).toString().trim();
            String phone = Objects.requireNonNull(phoneET.getText()).toString().trim();
            String bio = Objects.requireNonNull(bioET.getText()).toString().trim();
            String guestCountStr = Objects.requireNonNull(guestCountET.getText()).toString().trim();

            // Check if any field is empty or the date is not selected
            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || bio.isEmpty() || guestCountStr.isEmpty() || selectedDate == null) {
                Toast.makeText(EditReservationActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
                return; // Stop execution if any field is empty
            }

            // Parse and save guest count
            int guestCount = guestCountStr.isEmpty() ? 0 : Integer.parseInt(guestCountStr);

            Map<String, Object> reservation = new HashMap<>();
            reservation.put("firstName", firstName);
            reservation.put("lastName", lastName);
            reservation.put("phone", phone);
            reservation.put("bio", bio);
            reservation.put("guestCount", guestCount);
            if (selectedDate != null) {
                reservation.put("date", selectedDate);
            }

            db.collection("reservations").document(App.reservation.getId()).set(reservation)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditReservationActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        // Update App.reservation with the new date here if needed
                        App.reservation.setDate(selectedDate); // Make sure your Reservation model supports this
                        // Ensure UI is updated to reflect the newly saved date
                        updateDateTextView(selectedDate.toDate());
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditReservationActivity.this, "Error while saving reservation", Toast.LENGTH_SHORT).show());
        });


        delete.setOnClickListener(view -> {
            db.collection("reservations").document(App.reservation.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditReservationActivity.this, "Reservation Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditReservationActivity.this, "Error while deleting reservation", Toast.LENGTH_SHORT).show());
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) calendar.setTime(selectedDate.toDate()); // Initialize picker with current reservation date
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
                    Toast.makeText(EditReservationActivity.this, "Date Selected: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, Toast.LENGTH_SHORT).show();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDateTextView(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        currentDateTV.setText("Current Date: " + sdf.format(date));
    }
}
