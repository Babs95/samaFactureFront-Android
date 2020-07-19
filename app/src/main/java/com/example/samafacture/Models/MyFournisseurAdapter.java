package com.example.samafacture.Models;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samafacture.EditFournisseurDialog;
import com.example.samafacture.FournisseurFragment;
import com.example.samafacture.R;

import java.util.ArrayList;

public class MyFournisseurAdapter extends RecyclerView.Adapter<MyFournisseurAdapter.MyViewHolder> {
    ArrayList<Fournisseur> ListFournisseur;
    Context context;
    FournisseurFragment fragmentFournisseur;
    int pos;
    private OnFournisseurListener monFournisseurListener;
    public MyFournisseurAdapter(Context ctx, ArrayList<Fournisseur> ArrayListFour,FournisseurFragment fragment, OnFournisseurListener onFournisseurListener){
        ListFournisseur = ArrayListFour;
        context = ctx;
        fragmentFournisseur=fragment;
        this.monFournisseurListener = onFournisseurListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_rows3, parent, false);
        return new MyFournisseurAdapter.MyViewHolder(view, monFournisseurListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myTextLibelleFournisseur.setText(ListFournisseur.get(position).getLibelle());
    }

    @Override
    public int getItemCount() {
        return ListFournisseur.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextLibelleFournisseur;
        ImageView editImageView;
        OnFournisseurListener onFournisseurListener;
        public MyViewHolder(@NonNull View itemView, OnFournisseurListener onFournisseurListener) {
            super(itemView);
            myTextLibelleFournisseur = itemView.findViewById(R.id.textLibelleFournisseur);
            editImageView = itemView.findViewById(R.id.imageViewEditFour);
            this.onFournisseurListener = onFournisseurListener;
            editImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFournisseurListener.OnFournisseurClick(getAdapterPosition());
            pos = getAdapterPosition();

            EditFournisseurDialog editFournisseurDialog=new EditFournisseurDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("Id",ListFournisseur.get(pos).getFournisseur_id());
            bundle.putString("Libelle",ListFournisseur.get(pos).getLibelle());
            editFournisseurDialog.setArguments(bundle);
            editFournisseurDialog.setTargetFragment(fragmentFournisseur, 1);
            editFournisseurDialog.show(fragmentFournisseur.getFragmentManager(), "EditFournisseurDialog");
        }
    }
    public interface OnFournisseurListener {
        void OnFournisseurClick(int position);
    }


}
