package com.example.criminal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// Абстрактный класс для того чтобы повторно использовать создание однотипных активностей
// к примеру CrimeListActivity, таким образом переопределяется метод createFragment,
// которые создает нужный нам фрагмент
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment(); // создание отображаемого фрагмента

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // проверяем есть ли там фрагмент в контейнере
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // если нет, то создадим
        if(fragment==null){
            fragment = createFragment();
            // и сразу отобразим
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
