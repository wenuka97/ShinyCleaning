package com.example.shinycleaning;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class ServiceReservation extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText dateTxt;
    Spinner serviceType;
    EditText startTime, workingHour, serviceDate, workerCount;
    CheckBox checkEquipment;
    Button confirmBtn;

    FirebaseAuth fbAuth;
    DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reservation);
        loadUiView();


        String[] values = {"Home Cleaning", "Baby Sitting"};
        Spinner serviceSpin = findViewById(R.id.serviceTypeSpin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ServiceReservation.this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        serviceSpin.setAdapter(adapter);

        //calender implementation
        dateTxt = findViewById(R.id.dateTxt);
        dateTxt.setOnClickListener(this);


        //timepicker implementation
        final EditText startTime = (EditText) findViewById(R.id.startTimeTxt);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startTime = new StartTimeFragment();
                startTime.show(getSupportFragmentManager(), "Start Time");
            }
        });

        //service request method implementation

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String service_type = serviceType.getSelectedItem().toString();
                String service_date = serviceDate.getText().toString();
                String working_hour = workingHour.getText().toString();
                String start_time = startTime.getText().toString();
                String worker_count = workerCount.getText().toString();
                String check_equip;
                if (checkEquipment.isChecked()) {
                    check_equip = "not required";
                } else {
                    check_equip = "required";
                }


                System.out.println("Service type: " + service_type);
                System.out.println("Service date: " + service_date);
                System.out.println("Service hours: " + working_hour);
                System.out.println("Service time: " + start_time);
                System.out.println("Service members: " + worker_count);
                System.out.println("Service equipments: " + check_equip);

                if (TextUtils.isEmpty(service_type) || TextUtils.isEmpty(service_date)
                        || TextUtils.isEmpty(working_hour) || TextUtils.isEmpty(start_time)
                        || TextUtils.isEmpty(worker_count) || TextUtils.isEmpty(check_equip)) {

                    Toast.makeText(ServiceReservation.this, "All Fields are required", Toast.LENGTH_SHORT).show();

                } else {

                    serviceRequest(service_type, service_date, working_hour, start_time, worker_count, check_equip);

                }

            }
        });


    }

    //store request info in firebase
    private void serviceRequest(String service_type, String service_date, String working_hour, String start_time, String worker_count, String check_equip) {


        FirebaseUser firebaseUser = fbAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

//        currentMember = signAuth.getInstance().getCurrentUser();
//        final String id = currentMember.getUid();

        dbReference = FirebaseDatabase.getInstance().getReference().child("serviceRequests").child(userId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uId", userId);
        hashMap.put("serviceType", service_type);
        hashMap.put("date", service_date);
        hashMap.put("workHours", working_hour);
        hashMap.put("startTime", start_time);
        hashMap.put("workerCount", worker_count);
        hashMap.put("equipment", check_equip);

        dbReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> requestTask) {
                if (requestTask.isSuccessful()) {
                    Toast.makeText(ServiceReservation.this, "Request recorded ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ServiceReservation.this, ActivityHomePage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    private void loadUiView() {

        serviceType = (Spinner) findViewById(R.id.serviceTypeSpin);
        serviceDate = (EditText) findViewById(R.id.dateTxt);
        workingHour = (EditText) findViewById(R.id.workHoursTxt);
        startTime = (EditText) findViewById(R.id.startTimeTxt);
        workerCount = (EditText) findViewById(R.id.workerCountTxt);
        checkEquipment = (CheckBox) findViewById(R.id.checkMaterials);
        confirmBtn = (Button) findViewById(R.id.reqConfirmBtn);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText startTime = (EditText) findViewById(R.id.startTimeTxt);
        startTime.setText(hourOfDay + " : " + minute);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dateTxt:
                SetDateFragment setDateFragment = new SetDateFragment();
                setDateFragment.show(getSupportFragmentManager(), "Date Picker");
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonthe) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonthe);
        int currentMonth = month + 1;
        dateTxt.setText(dayOfMonthe + "/" + currentMonth + "/" + year);
    }
}
