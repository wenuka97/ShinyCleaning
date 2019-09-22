package com.example.shinycleaning;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;


public class SetDateFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        calendar.add(Calendar.DATE, +1);
        Date nDate = calendar.getTime();
        datePickerDialog.getDatePicker().setMinDate(nDate.getTime() - (nDate.getTime() % (24 * 60 * 60 * 1000)));

        return datePickerDialog;
    }
}