package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.MyFactureAdapter;

import java.util.ArrayList;

public class FactureActivity extends Fragment {
    private RecyclerView ListFactureRecyclerView;
    private ArrayList<Facture> ListFacture;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_facture, container, false);
        //setContentView(R.layout.activity_facture);
        ListFactureRecyclerView = view.findViewById(R.id.ListFactRecycler);
        ListFacture = new ArrayList<>();
        Facture facture = new Facture();
        facture.setId(1);
        facture.setLibelle("Internet");
        facture.setMontant("12.900");
        facture.setDateFacture("08/07/2020");
        facture.setMois("Juin");
        facture.setEtat("Non-payer");
        facture.setSyncOnLine("nonOk");
        Facture facture2 = new Facture();
        facture2.setId(1);
        facture2.setLibelle("Senelec");
        facture2.setMontant("150.000");
        facture2.setDateFacture("15/07/2020");
        facture2.setMois("Juillet");
        facture2.setEtat("Payer");
        facture2.setSyncOnLine("Ok");
        ListFacture.add(facture);
        ListFacture.add(facture2);
        //Initialisation RecyclerViewFacture
        initRecyclerView();
        return view;
    }
    private void initRecyclerView(){
        MyFactureAdapter myFactureAdapter = new MyFactureAdapter(getActivity(),ListFacture);
        ListFactureRecyclerView.setAdapter(myFactureAdapter);
        ListFactureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
