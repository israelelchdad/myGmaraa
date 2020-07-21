package com.example.myapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.model.DafLearning1;
import com.example.model.Profile;
import com.example.model.shas_masechtot_list_models.AllShasItem;
import com.example.model.shas_masechtot_list_models.SederItem;
import com.example.myapp.R;
import com.example.myapp.adapters.RecyclerViewStudyOptionsMasechetAdapter;
import com.example.myapp.adapters.RecyclerViewStudyOptionsSederAdapter;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.ActivityProfileBinding;

import com.example.myapp.utils.ManageSharedPreferences;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


import static com.example.myapp.activity.SplashActivity.KEY_EXTRA_List1;

public class ProfileActivity extends AppCompatActivity implements RecyclerViewStudyOptionsMasechetAdapter.CreateTypeOfStudy {

    private ActivityProfileBinding binding;
    private ArrayList<DafLearning1> mListLearning = new ArrayList<>();
    private AllShasItem mAllShas;
    private Profile mProfile = new Profile(0);
    private String mStringTypeOfStudy;
    private RecyclerView myRecyclerViewStudyOptions;
    private RecyclerViewStudyOptionsSederAdapter mRecyclerViewStudyOptionsAdapter;
    private TextView studyOptionDafHyomy;
    private TextView studyOptionTalmudBavly;
    private boolean mOptionTalmudBavlyOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initListAllShah();
        initViews();
        initListener();
    }

    private void initListAllShah() {
        Gson gson = new Gson();
        try {
            String txt = convertStreamToString(Objects.requireNonNull(this).getAssets().open("list_all_shas_json.txt"));
            mAllShas = gson.fromJson(txt, AllShasItem.class);
        } catch (Exception e) {
        }
    }

    private void initViews() {
        studyOptionDafHyomy = binding.studyOptionDafHyomy;
        studyOptionTalmudBavly = binding.studyOptionTalmudBavly;
    }

    private void initListener() {
        studyOptionDafHyomy.setOnClickListener(v -> {
            CreateListTypeOfStudy("דף היומי",2711);
        });
        studyOptionTalmudBavly.setOnClickListener(v -> {
            if (mOptionTalmudBavlyOpen == false){
                mOptionTalmudBavlyOpen = true;
                initRecyclerView();
            }else {
                mOptionTalmudBavlyOpen = false;
                myRecyclerViewStudyOptions.setVisibility(View.GONE);
            }

        });
        binding.ProfileCreateLearningBU.setOnClickListener(v -> createLearningClicked(v));
    }

    private void initRecyclerView() {
        myRecyclerViewStudyOptions = binding.myRecyclerView;
        myRecyclerViewStudyOptions.setVisibility(View.VISIBLE);
        myRecyclerViewStudyOptions.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewStudyOptionsAdapter = new RecyclerViewStudyOptionsSederAdapter(this, (ArrayList<SederItem>) mAllShas.getSeder());
        myRecyclerViewStudyOptions.setAdapter(mRecyclerViewStudyOptionsAdapter);
    }

    private void createLearningClicked(View v) {
        onRadioNumberOfReps();
        alertDialogAreYouSure();
    }

    public void onRadioNumberOfReps() {
        int selectedId = binding.ProfileNumberOfRepsRG.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.radio_No_thanks:
                mProfile.setNumberOfReps(0);
                break;
            case R.id.radio_Once:
                mProfile.setNumberOfReps(1);
                break;
            case R.id.radio_Twice:
                mProfile.setNumberOfReps(2);
                break;
            case R.id.radio_3_times:
                mProfile.setNumberOfReps(3);
                break;
        }
    }

    private void alertDialogAreYouSure() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ManageSharedPreferences.setProfile(mProfile, getBaseContext());
                        AppDataBase.getInstance(getBaseContext()).daoLearning1().insertAllLearning(mListLearning);
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.putExtra(KEY_EXTRA_List1, mListLearning);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        String typeOfStudy = "מסוג: " + mStringTypeOfStudy;
        String numberOfReps = "חזרות: " + mProfile.getNumberOfReps();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ברצונך ליצור לימוד יומי");
        builder.setMessage(typeOfStudy + "\n" + numberOfReps);
        builder.setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void createListAllShas() {
        Calendar startDafHayomi = findDateOfStartDafHayomi();
        mListLearning.clear();
        int id = 1;
        for (int i = 0; i < mAllShas.getSeder().size(); i++) {
            for (int j = 0; j < mAllShas.getSeder().get(i).getMasechtot().size(); j++) {
                for (int k = 2; k < (mAllShas.getSeder().get(i).getMasechtot().get(j).getPages() + 2); k++) {
                    DafLearning1 mPage = new DafLearning1(mAllShas.getSeder().get(i).getMasechtot().get(j).getName(), k,"דף היומי" ,id);
                    mPage.setPageDate(startDafHayomi.get(Calendar.DAY_OF_MONTH) + "/" + (startDafHayomi.get(Calendar.MONTH) + 1) + "/" + startDafHayomi.get(Calendar.YEAR));
                    mListLearning.add(mPage);
                    startDafHayomi.add(Calendar.DATE, 1);
                    id++;
                }
            }
        }
    }

    private Calendar findDateOfStartDafHayomi() {
        Calendar today = Calendar.getInstance();
       ArrayList<Calendar> startDafHyomiDates =  initListStartDafHyomiDates();
       if (today.after(startDafHyomiDates.get(0)) && today.before(startDafHyomiDates.get(1))){
           return startDafHyomiDates.get(0);
       }
       else if (today.after(startDafHyomiDates.get(1)) && today.before(startDafHyomiDates.get(2))){
            return startDafHyomiDates.get(1);
        }else {
           return startDafHyomiDates.get(2);
       }
    }

    private ArrayList<Calendar> initListStartDafHyomiDates() {
        ArrayList <Calendar> listStartDafHyomiDates = new ArrayList<>();
        listStartDafHyomiDates.add(createCalender(2012,7,3));
        listStartDafHyomiDates.add(createCalender(2020,0,5));
        listStartDafHyomiDates.add(createCalender(2027,5,8));
//        add more machzorim!!!!!!!!!!!!!!!!!!
        return listStartDafHyomiDates;
    }

    private Calendar createCalender(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        return c;
    }

    private void CreateListMasechet(String masechetName, int pages) {
        mListLearning.clear();
        int id = 1;
        for (int i = 2 ; i < (pages+2) ; i++) {
            mListLearning.add(new DafLearning1(masechetName, i,masechetName, id));
            id++;
        }
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public void CreateListTypeOfStudy(String stringTypeOfStudy , int masechetPage) {
        if (stringTypeOfStudy.equals("דף היומי")) {
            mStringTypeOfStudy = stringTypeOfStudy;
            createListAllShas();
        }else {
            mStringTypeOfStudy = stringTypeOfStudy;
            CreateListMasechet(stringTypeOfStudy, masechetPage);
        }
    }
}

