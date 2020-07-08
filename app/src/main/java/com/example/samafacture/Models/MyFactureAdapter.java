package com.example.samafacture.Models;

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

public class MyFactureAdapter extends RecyclerView.Adapter<MyFactureAdapter.MyViewHolder> {
    ArrayList<Facture> dataList;
    Context context;
    public MyFactureAdapter(Context ctx, ArrayList<Facture> ListFacture){
        dataList = ListFacture;
        context = ctx;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_rows2, parent, false);
        return new MyFactureAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myTextLibelle.setText(dataList.get(position).getLibelle());
        holder.myTextMois.setText(dataList.get(position).getMois());
        holder.myTextDate.setText(dataList.get(position).getDateFacture());
        holder.myTextMontant.setText(dataList.get(position).getMontant());
        if(dataList.get(position).getEtat().equalsIgnoreCase("Payer")){
            holder.btnPayer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView myTextLibelle, myTextMois, myTextDate,myTextMontant;
        Button btnPayer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextLibelle = itemView.findViewById(R.id.textViewLibFact);
            myTextMois = itemView.findViewById(R.id.textViewMois);
            myTextDate = itemView.findViewById(R.id.textViewDateFact);
            myTextMontant = itemView.findViewById(R.id.textViewMontant);
            btnPayer = itemView.findViewById(R.id.btnPayeFact);

        }
    }
}
