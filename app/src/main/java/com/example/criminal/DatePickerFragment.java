package com.example.criminal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.Inflater;

// диалоговый фрагмент с календарем для выбора даты
public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date"; // имя аргумента даты
    public static final String EXTRA_DATE = "com.example.Criminal.date";
    private DatePicker mDatePicker;

    //
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // заполнение из макета с виджетом даты
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null); // установка сегодняшней даты

        return new AlertDialog.Builder(getActivity())
                .setView(v) // установка диалогу загруженного представления
                .setTitle(R.string.date_picker_title) // заголовок диалога
                .setPositiveButton(android.R.string.ok, // кнопка ок в диалоге
                        new DialogInterface.OnClickListener() { // обработка нажатия на кнопку
                            @Override // отправка выбраной даты
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(CrimeActivity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    //метод для отправки даты через интент
    private void sendResult(int resultCode, Date date) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date); // ключ - значение
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                resultCode, intent);
    }
}
