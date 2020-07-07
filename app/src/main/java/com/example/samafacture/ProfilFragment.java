package com.example.samafacture;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class ProfilFragment extends Fragment implements MyCustomDialog.OnInputSelected {

    private static final String TAG = "MainFragment";
    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);

        mInputDisplay.setText(input);
    }

    private String [] tabAnnee;
    private String annee;
    private ListView listAnnee;

    private Button mOpenDialog;
    public TextView mInputDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profil, container, false);
        listAnnee = view.findViewById(R.id.listFormation);
        mInputDisplay = view.findViewById(R.id.txtInput);
     tabAnnee =  getResources().getStringArray(R.array.tab_annee);
     ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tabAnnee);
        listAnnee.setAdapter(adapter);//chargement des données dans la liste

        listAnnee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: opening dialog");

                MyCustomDialog dialog = new MyCustomDialog();
                dialog.setTargetFragment(ProfilFragment.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        return view;

    }
}