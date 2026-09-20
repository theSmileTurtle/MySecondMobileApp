package com.example.criminal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

// Активность, которая отображает детально какое-либо преступление
// в теории можно наследовать от SingleFragmentActivity, но я не уверен,
// выглядит так как будто можно, да у них же одинаковые onCreate
public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // поиск фрагмента, находящегося в контейнере
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // если в контейнере ничего не найдено, то надо создать фрагмент,
        // а именно CrimeFragment (фрагмент, в котором и отображается детальная информация)
        if(fragment==null){
            fragment = new CrimeFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    // Для передачи идентификатора преступления через интента
    public static final String EXTRA_CRIME_ID = "com.exampleCriminal.crime_id";
    // Статический метод, с помощью которого в другой активности мы созаем интент и передаем ему
    // идентификатор преступления, которое надо будет отобразить в CrimeActivity
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId); // ключ - значение
        return intent; // интент, из которого стартанем эту активность
    }
}