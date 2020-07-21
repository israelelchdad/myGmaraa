package com.example.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.DafLearning1;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.utils.ConvertIntToPage;

import java.util.ArrayList;

public class OneDafAdapter extends RecyclerView.Adapter<OneDafAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<DafLearning1> myListALLDaf = new ArrayList<>();
    private ArrayList<DafLearning1> myListDaf = new ArrayList<>();

    private ArrayList<DafLearning1> myListFilterDaf = new ArrayList<>();

    public OneDafAdapter(Context context, ArrayList<DafLearning1> myListDaf) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.myListALLDaf = myListDaf;
        this.myListDaf.addAll(myListALLDaf);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv_daf, parent, false);
        return new OneDafAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setHolder(myListDaf.get(position));

    }

    @Override
    public int getItemCount() {
        return myListDaf.size();
    }

    public void filterAllMasechtot(String myNameMasechet){
        myListFilterDaf.clear();
        for (int i = 0; i <myListALLDaf.size() ; i++) {
            if (myListALLDaf.get(i).getMasechet().equals(myNameMasechet)) {
                myListFilterDaf.add(myListALLDaf.get(i));
            }
        }
        myListDaf.clear();
        myListDaf.addAll(myListFilterDaf);
        notifyDataSetChanged();
    }
    public void filterLearnet(){
        myListFilterDaf.clear();
        for (int i = 0; i <myListALLDaf.size() ; i++) {
            if (myListALLDaf.get(i).isLearning()) {
                myListFilterDaf.add(myListALLDaf.get(i));
            }
        }
        myListDaf.clear();
        myListDaf.addAll(myListFilterDaf);
        notifyDataSetChanged();
    }
    public void filterSkipt(){
        myListFilterDaf.clear();
        for (int i = 0; i <myListALLDaf.size() ; i++) {
            if (!myListALLDaf.get(i).isLearning()) {
                myListFilterDaf.add(myListALLDaf.get(i));
            }
        }
        myListDaf.clear();
        myListDaf.addAll(myListFilterDaf);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        CheckBox ifLernning;
        TextView masechet;
        TextView numDaf;
        TextView date;
        CheckBox chazara1;
        CheckBox chazara2;
        CheckBox chazara3;
        DafLearning1 mDaf;


        ViewHolder(View itemView) {
            super(itemView);
            ifLernning = itemView.findViewById(R.id.one_daf_checkbox);
            masechet = itemView.findViewById(R.id.one_daf_Tv_masechet);
            numDaf = itemView.findViewById(R.id.one_daf_Tv_numPage);
            date = itemView.findViewById((R.id.one_daf_Tv_date));
            chazara1 = itemView.findViewById((R.id.chazara_1_CB));
            chazara2 = itemView.findViewById((R.id.chazara_2_CB));
            chazara3 = itemView.findViewById((R.id.chazara_3_CB));

            ifLernning.setOnClickListener(v -> {
                AppDataBase.getInstance(context).daoLearning1().updateIsLearning(ifLernning.isChecked(), mDaf.getMasechet(),mDaf.getPageNumber());
            });

            chazara1.setOnClickListener(v -> {
                if (chazara1.isChecked()){
                    ListenerChazara(1);
                }else {
                    ListenerChazara(0);
                }
            });
            chazara2.setOnClickListener(v -> {
            if (chazara2.isChecked()){
                ListenerChazara(2);
            }else {
                ListenerChazara(1);
            }
        });
            chazara3.setOnClickListener(v -> {
                if (chazara3.isChecked()){
                    ListenerChazara(3);
                }else {
                    ListenerChazara(2);
                }
            });
        }

        private void ListenerChazara(int chazara) {
            switch(chazara) {
                case 0:
                    UpdateListAndDB(0);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 1:
                    UpdateListAndDB(1);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 2:
                    chazara1.setChecked(true);
                    UpdateListAndDB(2);
                    chazara3.setChecked(false);
                    break;
                case 3:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    UpdateListAndDB(3);
                    break;
            }
        }

        private void UpdateListAndDB(int chazara) {
            AppDataBase.getInstance(context).daoLearning1().updateNumOfChazara(chazara,mDaf.getMasechet(), mDaf.getPageNumber());
//            updataArrey
        }


        public void setHolder(DafLearning1 mDaf) {
            this.mDaf = mDaf;
            ifLernning.setChecked(mDaf.isLearning());
            masechet.setText(mDaf.getMasechet());
            numDaf.setText(ConvertIntToPage.intToPage(mDaf.getPageNumber()));
            initChazara(mDaf.getChazara());
            date.setText(mDaf.getPageDate());
        }
        private void initChazara(int chazara) {
            if (chazara == 1){
                chazara1.setChecked(true);
            }
            if (chazara == 2){
                chazara1.setChecked(true);
                chazara2.setChecked(true);
            }
            if (chazara == 3){
                chazara1.setChecked(true);
                chazara2.setChecked(true);
                chazara3.setChecked(true);
            }
        }
    }
}

