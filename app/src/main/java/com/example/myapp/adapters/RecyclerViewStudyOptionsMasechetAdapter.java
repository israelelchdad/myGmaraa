package com.example.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.shas_masechtot_list_models.MasechetItem;
import com.example.model.shas_masechtot_list_models.SederItem;
import com.example.myapp.R;

import java.util.List;

public class RecyclerViewStudyOptionsMasechetAdapter extends RecyclerView.Adapter<RecyclerViewStudyOptionsMasechetAdapter.Holder> {

    private List <MasechetItem> mSederItems;
    private CreateTypeOfStudy mListener;
    private Context mContext;

    public RecyclerViewStudyOptionsMasechetAdapter(List<MasechetItem> mSederItems, Context context) {
        this.mSederItems = mSederItems;
        mContext = context;
        this.mListener = (CreateTypeOfStudy) context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_masechet,parent,false);
        return new RecyclerViewStudyOptionsMasechetAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
           holder.setHolder(mSederItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mSederItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView study;
        private MasechetItem masechetItem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            study = itemView.findViewById(R.id.masechet_text);
        }

        public void setHolder (MasechetItem masechetItem){
            this.masechetItem = masechetItem;
            study.setText(masechetItem.getName());

            itemView.setOnClickListener(v -> {
                mListener.CreateListTypeOfStudy(masechetItem.getName(),masechetItem.getPages());
            });
        }

    }

    public interface CreateTypeOfStudy {
        void CreateListTypeOfStudy(String stringTypeOfStudy, int masechetPage);
    }
}
