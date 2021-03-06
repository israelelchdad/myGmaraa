package com.example.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.ArrayList;

public class AllMasechtotAdapter extends RecyclerView.Adapter<AllMasechtotAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private Context context;
        private ArrayList<String> allMasechtot = new ArrayList<>();
        private NameMasechet myNameMasechet;

        public AllMasechtotAdapter(Context context, ArrayList<String> allMasechtot, NameMasechet myNameMasechet ) {
            this.context = context;
            this.myNameMasechet = myNameMasechet;
            this.mInflater = LayoutInflater.from(context);
            this.allMasechtot =allMasechtot;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_one_masechet, parent, false);
            return new AllMasechtotAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setHolder(allMasechtot.get(position));

        }

        @Override
        public int getItemCount() {
            return allMasechtot.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder  {

            TextView masechet;
            String myMasechet;

            ViewHolder(View itemView) {
                super(itemView);
                masechet = itemView.findViewById(R.id.masechet_text);
            }

            public void setHolder(String nameMasechet) {
                masechet.setText(nameMasechet);
                myMasechet = nameMasechet;
                masechet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myNameMasechet!=null){
                            myNameMasechet.nameMasechet(myMasechet);
                        }

                    }
                });
            }
        }
        public interface NameMasechet {
            void nameMasechet(String nameMasechet);
        }




    }

