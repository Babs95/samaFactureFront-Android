package com.example.samafacture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samafacture.Models.Annee;
import com.example.samafacture.Models.MyAnneeAdapter;
import com.example.samafacture.Models.User;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProfilFragment extends Fragment implements MyCustomDialog.OnInputSelected, MyAnneeAdapter.OnAnneeListener {

    private static final String TAG = "MainFragment";


    //private String tabAnnee[], tabEtat[];
    private String annee;
    private ListView listAnnee;
    private ArrayList<Annee> ListAnnee;
    private BdSamaFacture bdSamaFacture;
    ProgressBar progressBar;

    private Button mOpenDialog;
    public TextView mInputDisplay,nom,prenom,email,login;
    private RecyclerView ListAnneRecyclerView;

    public ProfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bdSamaFacture = new BdSamaFacture(getActivity());
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profil, container, false);
        nom = view.findViewById(R.id.txtProfilNom);
        prenom = view.findViewById(R.id.txtProfilPrenom);
        email = view.findViewById(R.id.txtProfilEmail);
        login = view.findViewById(R.id.txtProfilLogin);
        mOpenDialog = view.findViewById(R.id.btnSaveYear);
        ListAnneRecyclerView = view.findViewById(R.id.ListAnneRecycler);
        ListAnnee=new ArrayList<>();
        //Progessbar
        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit2);
        CubeGrid cubeGrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubeGrid);
        progressBar.setVisibility(View.GONE);

        loadRecyclerViewData();
        List<User> ListUserConnected = bdSamaFacture.ListUser();
        for (int i=0;i<ListUserConnected.size();i++){
            nom.setText(ListUserConnected.get(i).getNom());
            prenom.setText(ListUserConnected.get(i).getPrenom());
            email.setText(ListUserConnected.get(i).getEmail());
            login.setText(ListUserConnected.get(i).getLogin());
        }
        mOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog dialog = new MyCustomDialog();
                dialog.setTargetFragment(ProfilFragment.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");

            }
        });



        return view;

    }
    private void initRecyclerView(){
        MyAnneeAdapter myAnneeAdapter = new MyAnneeAdapter(getActivity(),ListAnnee,this);
        ListAnneRecyclerView.setAdapter(myAnneeAdapter);
        ListAnneRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    //Listener for modal
    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);
        System.out.println("send Input"+input);
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                System.out.println("In response.isSuccessful()");
                String message = "Annee crée avec succèss";
                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                loadRecyclerViewData();
            }
        });

    }

    //Listener for RecyclerView
    @Override
    public void onAnneeClick(int position) {
        UpdateYearState(ListAnnee.get(position).getId());
        /*System.out.println("OnClick Annee");
        System.out.println(ListAnnee.get(position).getId());
        System.out.println(ListAnnee.get(position).getLibelle());*/
    }


    public void loadRecyclerViewData() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://api-samafacture.herokuapp.com/api/annee/getall";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String error = getString(R.string.error_connection);
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ListAnnee.clear();
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Annee");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String Libannee=jsonObject1.getString("libelle");
                            String Etat_annee=jsonObject1.getString("etat");
                            System.out.println("Annee et Etat");
                            System.out.println(Libannee);
                            System.out.println(Etat_annee);
                            Gson gson=new Gson();
                            Annee annee = gson.fromJson(jsonObject1.toString(), Annee.class);
                            ListAnnee.add(annee);
                            /*for (Typepaiment typ: ){
                                ListTypePaiement.add(b);
                            }*/
                        }
                        //ListAnneRecyclerView.notify();
                        /*System.out.println(ListAnnee);
                        for (Annee typ: ListAnnee){
                            System.out.println(typ.getId()+typ.getLibelle()+typ.getEtat());
                        }*/

                    }
                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                initRecyclerView();
                            }
                        });
                    }

                }catch (JSONException e){e.printStackTrace();}
            }
        });
    }

    public void  UpdateYearState(int id) {
        try {
            String url ="https://api-samafacture.herokuapp.com/api/annee/UpdateAnneeEtat/"+id;
            OkHttpClient  client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String error = getString(R.string.error_connection);
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                    dialog.setIcon(R.mipmap.ic_launcher);
                                    dialog.setTitle("Année Etat");
                                    dialog.setMessage("Etat année modifier avec succès");
                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadRecyclerViewData();
                                        }
                                    });
                                    dialog.show();
                                }
                            });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}