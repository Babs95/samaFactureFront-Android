package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samafacture.Models.Annee;
import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.MyFactureAdapter;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class FactureActivity extends Fragment implements NewFactureDialog.OnInputSelected {
    private RecyclerView ListFactureRecyclerView;
    private ArrayList<Facture> ListFacture;
    private BdSamaFacture bdSamaFacture;
    private Button btnsave,btnpaye;
    ProgressBar progressBar;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_facture, container, false);
        //setContentView(R.layout.activity_facture);
        bdSamaFacture = new BdSamaFacture(getActivity());
        //Progessbar
        /*progressBar = (ProgressBar) view.findViewById(R.id.spin_kit2);
        CubeGrid cubeGrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubeGrid);
        progressBar.setVisibility(View.GONE);*/
        ListFactureRecyclerView = view.findViewById(R.id.ListFactRecycler);
        btnsave = view.findViewById(R.id.btnSaveFacture);
        btnpaye = view.findViewById(R.id.btnPayeFact);
        ListFacture = new ArrayList<>();
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        for (int i=0;i<ListFacture.size();i++){
            System.out.println("Facture"+i);
            System.out.println(ListFacture.get(i).getId());
            System.out.println(ListFacture.get(i).getLibelle());
            System.out.println(ListFacture.get(i).getDatePaiement());
            System.out.println(ListFacture.get(i).getMontant());
            System.out.println(ListFacture.get(i).getEtat());
            System.out.println(ListFacture.get(i).getUser_id());
            System.out.println(ListFacture.get(i).getFournisseur());
            System.out.println(ListFacture.get(i).getTypepaiement());
            System.out.println(ListFacture.get(i).getAnnee());
            System.out.println(ListFacture.get(i).getMois());
            System.out.println(ListFacture.get(i).getLocalState());
            System.out.println(ListFacture.get(i).getSyncOnLine());
            System.out.println(ListFacture.get(i).getIdFacture());

        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFactureDialog dialog = new NewFactureDialog();
                dialog.setTargetFragment(FactureActivity.this, 1);
                dialog.show(getFragmentManager(), "NewFactureDialog");
            }
        });

        /*Facture facture = new Facture();
        facture.setId(1);
        facture.setLibelle("Internet");
        facture.setMontant("12.900");
        facture.setDatePaiement("");
        facture.setMois("Juin");
        facture.setEtat("Non-payer");
        facture.setSyncOnLine("nonOk");
        Facture facture2 = new Facture();
        facture2.setId(1);
        facture2.setLibelle("Senelec");
        facture2.setMontant("150.000");
        facture2.setDatePaiement("15/07/2020");
        facture2.setMois("Juillet");
        facture2.setEtat("Payer");
        facture2.setSyncOnLine("Ok");
        ListFacture.add(facture);
        ListFacture.add(facture2);*/
        //Initialisation RecyclerViewFacture
        initRecyclerView();
        return view;
    }
    private void initRecyclerView(){
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        MyFactureAdapter myFactureAdapter = new MyFactureAdapter(getActivity(),ListFacture,this);
        ListFactureRecyclerView.setAdapter(myFactureAdapter);
        ListFactureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);
        System.out.println("send Input"+input);
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                System.out.println("In response.isSuccessful()");
                String message = "facture crée avec succèss";
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }
        });

    }
}
