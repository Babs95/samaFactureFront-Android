package com.example.samafacture;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ProfilFragment extends Fragment {


    private String [] tabAnnee;
    private String annee;
    private ListView listAnnee;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profil, container, false);
        listAnnee = view.findViewById(R.id.listFormation);
     tabAnnee =  getResources().getStringArray(R.array.tab_annee);
     ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tabAnnee);
        listAnnee.setAdapter(adapter);//chargement des données dans la liste




        return view;

    }
}