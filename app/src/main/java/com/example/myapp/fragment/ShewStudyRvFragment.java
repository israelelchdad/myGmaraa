package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.model.DafLearning1;
import com.example.myapp.activity.SplashActivity;
import com.example.myapp.adapters.AllMasechtotAdapter;
import com.example.myapp.adapters.OneDafAdapter;
import com.example.myapp.databinding.FragmentShewStudyRvBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShewStudyRvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShewStudyRvFragment extends Fragment implements AllMasechtotAdapter.NameMasechet {
    public static final String TAG = ShewStudyRvFragment.class.getSimpleName();
    private FragmentShewStudyRvBinding binding;
    private ArrayList<DafLearning1> myList1 = new ArrayList<>();
    private OneDafAdapter myAdapter;


    public ShewStudyRvFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShewStudyRvFragment newInstance(ArrayList<DafLearning1>myList1 ) {
        ShewStudyRvFragment fragment = new ShewStudyRvFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SplashActivity.KEY_EXTRA_List1,myList1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myList1 = getArguments().getParcelableArrayList(SplashActivity.KEY_EXTRA_List1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShewStudyRvBinding.inflate(inflater, container, false);
        initTabLayout();
        initReciclerviewMasechtot();
        binding.showStudyRVMasechtot.setVisibility(View.VISIBLE);
        initReciclerviewDapim();
        return binding.getRoot();
    }

    private void initTabLayout() {
        TabLayout tabLayout = binding.tabLayoutTypeList;
        TabLayout.Tab tab1 = tabLayout.newTab();
        TabLayout.Tab tab2 = tabLayout.newTab();
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab1.setText("הכל");
        tab2.setText("למדתי");
        tab3.setText("דילגתי");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        setListenerOfTabLayout(tabLayout);

    }

    private void setListenerOfTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabSelected = tab.getPosition();
                if(tabSelected==0){
                    binding.showStudyRVMasechtot.setVisibility(View.VISIBLE);
                }
                if(tabSelected==1) {
                    binding.showStudyRVMasechtot.setVisibility(View.GONE);
                    myAdapter.filterLearnet();
                }
                if(tabSelected==2) {
                    binding.showStudyRVMasechtot.setVisibility(View.GONE);
                    myAdapter.filterSkipt();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initReciclerviewDapim() {

        RecyclerView recyclerView = binding.showStudyRVDapim;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new OneDafAdapter(getContext(),myList1);
        recyclerView.setAdapter(myAdapter);
    }

    private void initReciclerviewMasechtot() {
        RecyclerView recyclerViewMasechtot = binding.showStudyRVMasechtot;
        recyclerViewMasechtot.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        ArrayList<String> allMasechtot = new ArrayList<>();
        for (int i = 1; i <myList1.size() ; i++) {
            if(i == myList1.size()-1){
                allMasechtot.add(myList1.get(i).getMasechet());
                AllMasechtotAdapter myAdapter2 = new AllMasechtotAdapter(getContext(),allMasechtot,this);
                recyclerViewMasechtot.setAdapter(myAdapter2);
                return;
            }
            if(!myList1.get(i).getMasechet().equals(myList1.get(i-1).getMasechet())){
                allMasechtot.add(myList1.get(i-1).getMasechet());
            }

        }

    }

    @Override
    public void nameMasechet(String nameMasechet) {
        myAdapter.filterAllMasechtot(nameMasechet);
    }
}