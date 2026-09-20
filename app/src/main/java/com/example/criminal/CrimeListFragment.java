package com.example.criminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Класс реализующий фрагмент, который отображает список преступлений
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView; // виджет отображающий прокручивающийся список
    private CrimeAdapter mAdapter;

    // находим и отображаем RecyclerView на нашем фрагменте
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        // получение RecyclerView из макета
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // назначаем новый менеджер расположения
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // обновление интерфейса
        updateUI();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // включить меню
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu); // заполнить меню по макету
    }

    // срабатывает при выборе элемента в меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.new_crime) { // если это элемент добавить преступление
            // создаем "пустое" преступление и добавляем его в бд
            Crime crime = new Crime();
            CrimeLab.get(getActivity()).addCrime(crime);
            // вызываем активность редактирования этого преступления
            Intent intent = CrimeActivity.newIntent(getActivity(), crime.getmId());
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.show_subtitle) { // если это элемент показывающий подзаголовок
            updateSubtitle();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
//        switch (item.getItemId()) {
//            case R.id.new_crime:
//                Crime crime = new Crime();
//                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimeActivity.newIntent(getActivity(), crime.getmId());
//                startActivity(intent);
//                return true;
//
//            default: return super.onOptionsItemSelected(item);
//        }
    }

    // обновляем информацию в подзаголовке
    private void updateSubtitle() {
        CrimeLab crL = CrimeLab.get(getActivity());
        int crimeCount = crL.getmCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle); // установка подзаголовка
    }

    // при возвращении к активности с этим фрагментом, нужно обновить интерфейс
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // обновление интерфейса
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();
        // если адаптера нет, то создать и связать его с RecyclerView
        if(mAdapter==null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else  {
            mAdapter.setCrimes(crimes); // установить адаптеру обновленный список преступлений
            mAdapter.notifyDataSetChanged(); // сообщить адаптеру, что данные изменились
        }

        //updateSubtitle();
    }

    // Как я понял, он представляет из себя один элемент из списка
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView; // изображение исправленного преступления

        // заполняем все в конструкторе
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title); // заголовок
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date); // дата
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved); // решение
            itemView.setOnClickListener(this); // обработчик нажатия для конкретного элемента списка
        }

        // обработка связывания
        public void bind(Crime crime) {
            // получаем все поля из полученного преступления
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(mCrime.getmDate().toString());
            // связывание с отображением решенности преступления (если решено, изображение видно, нет - не видно)
            mSolvedImageView.setVisibility(mCrime.ismSolved() ? View.VISIBLE : View.GONE);
        }

        // при нажатии открыть CrimeActivity и предать ей идентификатор преступления,
        // которое надо отобразить
        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getmId());
            startActivity(intent);
        }
    }

    // адаптер, вроде как нужен для связки интерфейса с уже самими преступлениями
    // "RecyclerView взаимодействует с адаптером, когда требуется создать
    //  новый объект ViewHolder или связать существующий объект ViewHolder с
    //  объектом Crime."
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        // "Вызывается виджетом RecyclerView, когда
        //  ему требуется новое представление для отображения элемента."
        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater lauoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(lauoutInflater, parent);
        }

        // связывание объекта Crime с CrimeHolder
        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position); // получение преступления из списка
            holder.bind(crime); // передача его в CrimeHolder
        }

        // получение количества элементов в списке
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}
