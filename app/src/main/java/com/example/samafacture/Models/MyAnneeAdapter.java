package com.example.samafacture.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samafacture.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MyAnneeAdapter extends RecyclerView.Adapter<MyAnneeAdapter.MyViewHolder> {

    // String data1[],data2[];
    ArrayList<Annee> data;
    Context context;
    private OnAnneeListener mOnAnneeListener;

    public MyAnneeAdapter(Context ct, ArrayList<Annee> tabAnnee, OnAnneeListener onAnneeListener) {
        context = ct;
        data = tabAnnee;
        // data2=tabEtat;
        this.mOnAnneeListener = onAnneeListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_rows, parent, false);
        return new MyViewHolder(view, mOnAnneeListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(data.get(position).getLibelle());
        holder.myText2.setText(data.get(position).getEtat());
        if (data.get(position).getEtat().equalsIgnoreCase("Non-Actif")) {
            holder.myText2.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myText1, myText2;
        OnAnneeListener onAnneeListener;

        public MyViewHolder(@NonNull View itemView, OnAnneeListener onAnneeListener) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.name_annne);
            myText2 = itemView.findViewById(R.id.annee_etat);
            this.onAnneeListener = onAnneeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAnneeListener.onAnneeClick(getAdapterPosition());
        }
    }

    public interface OnAnneeListener {
        void onAnneeClick(int position);
    }
}
