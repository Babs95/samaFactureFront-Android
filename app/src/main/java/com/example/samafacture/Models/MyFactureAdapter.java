package com.example.samafacture.Models;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samafacture.FactureActivity;
import com.example.samafacture.PaymentDialog;
import com.example.samafacture.R;

import java.util.ArrayList;

import static com.example.samafacture.R.*;

public class MyFactureAdapter extends RecyclerView.Adapter<MyFactureAdapter.MyViewHolder> {
    ArrayList<Facture> dataList;
    Context context;
    FactureActivity fragmentFacture;
    int pos;
    private OnFactureListener mOnFactureListener;
    public MyFactureAdapter(Context ctx, ArrayList<Facture> ListFacture,FactureActivity fragment,OnFactureListener onFactureListener){
        dataList = ListFacture;
        context = ctx;
        fragmentFacture=fragment;
        this.mOnFactureListener = onFactureListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(layout.my_rows2, parent, false);
        return new MyFactureAdapter.MyViewHolder(view, mOnFactureListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myTextLibelle.setText(dataList.get(position).getLibelle());
        holder.myTextMois.setText(dataList.get(position).getMois());
        holder.myTextDate.setText(dataList.get(position).getDatePaiement());
        holder.myTextMontant.setText(dataList.get(position).getMontant()+"f");
        //montant =dataList.get(getItemViewType(position)).getMontant();


        if(dataList.get(position).getEtat().equalsIgnoreCase("Payer")){
            holder.btnPayer.setVisibility(View.INVISIBLE);

        }
        if(dataList.get(position).getSyncOnLine().equalsIgnoreCase("nonOk")){
            holder.myTextSynchro.setText("Non-synchroniser");
            holder.myTextSynchro.setTextColor(context.getResources().getColor(color.colorRed));
        }else {
            holder.myTextSynchro.setText("Synchroniser");
            holder.myTextSynchro.setTextColor(context.getResources().getColor(color.colorPrimaryDark));
        }
        /*holder.btnPayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDialog paymentDialog=new PaymentDialog();
                Bundle bundle = new Bundle();
                bundle.putString("Montant",montant);
                paymentDialog.setArguments(bundle);
                paymentDialog.setTargetFragment(fragmentFacture, 1);
                paymentDialog.show(fragmentFacture.getFragmentManager(), "PaymentDialog");
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextLibelle, myTextMois, myTextDate,myTextMontant,myTextSynchro;
        Button btnPayer;
        OnFactureListener onFactureListener;
        public MyViewHolder(@NonNull View itemView, OnFactureListener onFactureListener) {
            super(itemView);
            myTextLibelle = itemView.findViewById(id.textViewLibFact);
            myTextMois = itemView.findViewById(id.textViewMois);
            myTextDate = itemView.findViewById(id.textViewDateFact);
            myTextMontant = itemView.findViewById(id.textViewMontant);
            myTextSynchro = itemView.findViewById(id.textViewSynchro);
            btnPayer = itemView.findViewById(id.btnPayeFact);
            this.onFactureListener = onFactureListener;
            btnPayer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onFactureListener.onFactureClick(getAdapterPosition());
            pos = getAdapterPosition();

            //montant
            PaymentDialog paymentDialog=new PaymentDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("Id",dataList.get(pos).getId());
            bundle.putString("Montant",dataList.get(pos).getMontant());
            bundle.putString("Fournisseur",dataList.get(pos).getFournisseur());
            paymentDialog.setArguments(bundle);
            paymentDialog.setTargetFragment(fragmentFacture, 1);
            paymentDialog.show(fragmentFacture.getFragmentManager(), "PaymentDialog");
        }
    }
    public interface OnFactureListener {
        void onFactureClick(int position);
    }

    public void filterList(ArrayList<Facture> filteredList) {
        dataList = filteredList;
        notifyDataSetChanged();
    }
}
