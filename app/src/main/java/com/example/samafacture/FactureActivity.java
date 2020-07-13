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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class FactureActivity extends Fragment implements PaymentDialog.OnInputSend,NewFactureDialog.OnInputSelected, MyFactureAdapter.OnFactureListener {
    private RecyclerView ListFactureRecyclerView;
    private ArrayList<Facture> ListFacture;
    private List<Facture> ListFacturePayer;
    private BdSamaFacture bdSamaFacture;
    private Button btnsave,btnpaye;
    ProgressBar progressBar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
        ListFacturePayer = new ArrayList<>();
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFactureDialog dialog = new NewFactureDialog();
                dialog.setTargetFragment(FactureActivity.this, 1);
                dialog.show(getFragmentManager(), "NewFactureDialog");
            }
        });
        //Initialisation RecyclerViewFacture
        initRecyclerView();
        return view;
    }
    private void initRecyclerView(){
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        MyFactureAdapter myFactureAdapter = new MyFactureAdapter(getActivity(),ListFacture,this, (MyFactureAdapter.OnFactureListener) this);
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

    @Override
    public void onFactureClick(int position) {
        /*System.out.println("OnClick Facture");
        System.out.println(ListFacture.get(position).getId());
        System.out.println(ListFacture.get(position).getMontant());*/
    }

    @Override
    public void sendBabs(String input) {
        Log.d(TAG, "sendBabs: found incoming input: " + input);
        System.out.println("send Babs"+input);
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                String message = "Facture payé avec succèss";
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                initRecyclerView();
                GetFacturePayer();
            }
        });
    }

    public void GetFacturePayer() {
        ListFacturePayer = bdSamaFacture.getFacturesPayer();
        for (int i=0;i<ListFacturePayer.size();i++){
            String postBody="{\n" +
                    "    \"libelle\": \""+ListFacturePayer.get(i).getLibelle()+"\",\n" +
                    "    \"datePaiement\": \""+ListFacturePayer.get(i).getDatePaiement()+"\",\n" +
                    "    \"montant\": \""+ListFacturePayer.get(i).getMontant()+"\",\n" +
                    "    \"etat\": \""+ListFacturePayer.get(i).getEtat()+"\",\n" +
                    "    \"user_id\": \""+ListFacturePayer.get(i).getUser_id()+"\",\n" +
                    "    \"fournisseur\": \""+ListFacturePayer.get(i).getFournisseur()+"\",\n" +
                    "    \"typepaiement\": \""+ListFacturePayer.get(i).getTypepaiement()+"\",\n" +
                    "    \"mois\": \""+ListFacturePayer.get(i).getMois()+"\"\n" +
                    "}";
            SynchroniserFacture(postBody);
        }
        initRecyclerView();
    }

    public void SynchroniserFacture(String postBody) {
        try {

            String url = "https://api-samafacture.herokuapp.com/api/facture/store";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, postBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
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
                    System.out.println("In onResponse");
                    try {
                        //final String babs = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("In response.isSuccessful()");
                                String message = "Facture synchroniser avec succèss";
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        });


                    }catch (Exception e){
                        System.out.println(e);
                    }

                }
            });

        } catch (Exception e){

        }
    }
}
