package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
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
    MyFactureAdapter myFactureAdapter;
    private EditText SearchText;
    boolean VerifConnection;
    //private
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
        SearchText = view.findViewById(R.id.EditSearch);
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        ListFacture = new ArrayList<>();
        ListFacturePayer = new ArrayList<>();
        //ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        /*for (int i=0; i<ListFacture.size();i++){
            System.out.println("Liste Facture"+i);
            System.out.println(ListFacture.get(i).getLibelle());
        }*/
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
        //Synchronisation des factures
        //VerifConnection = isNetworkConnected();
        //if(VerifConnection == )
        Timer_Synchronisation();
        return view;
    }
    private void filter(String text) {
        ArrayList<Facture> filteredListFacture = new ArrayList<>();
        for (Facture item : ListFacture) {
            if (item.getLibelle().toLowerCase().contains(text.toLowerCase()) || item.getEtat().toLowerCase().contains(text.toLowerCase())) {
                filteredListFacture.add(item);
            }
        }
        myFactureAdapter.filterList(filteredListFacture);
    }
    private void initRecyclerView(){
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        myFactureAdapter = new MyFactureAdapter(getActivity(),ListFacture,this, (MyFactureAdapter.OnFactureListener) this);
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
                String message = "Facture crée avec succèss";
                Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }
        });

    }

    @Override
    public void onFactureClick(int position) {
        /*System.out.println("OnClick Facture");
        tets
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
                Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                GetFacturePayer();
                initRecyclerView();
            }
        });
    }

    public void Timer_Synchronisation(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("Synchronisation Facture");
                boolean connection = isNetworkConnected();
                if(connection == true){
                    System.out.println("Connected");
                    GetFacturePayer();
                }else {
                    System.out.println("Not Connected");
                    //cancel();
                }
            }
        };
        Timer timer = new Timer("Timer");

        long delay  = 30000L;
        long period = 30000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);

    }

    public void GetFacturePayer() {
        ListFacturePayer = bdSamaFacture.getFacturesPayer();
        if(ListFacturePayer!=null && !ListFacturePayer.isEmpty()){
            for (int i=0;i<ListFacturePayer.size();i++){
                System.out.println("Liste FacturePayer"+i);
                System.out.println(ListFacturePayer.get(i).getId());
                System.out.println(ListFacturePayer.get(i).getUser_id());
                System.out.println(ListFacturePayer.get(i).getLibelle());
                System.out.println(ListFacturePayer.get(i).getEtat());
                System.out.println(ListFacturePayer.get(i).getSyncOnLine());
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
                boolean CheckConnection = isNetworkConnected();
                if (CheckConnection == true){
                    SynchroniserFacture(postBody,ListFacturePayer.get(i).getId(),"payer",ListFacturePayer.get(i).getTypepaiement(),"payer","Ok");
                }

            }
            //initRecyclerView();
        }
    }
    private boolean isNetworkConnected() throws NullPointerException {
        boolean connection = false;
        //if(getActivity() !=null){
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()){
                connection = true;
            }else {
                connection = false;
            }

        }catch (Exception e){
            //System.out.println(e);
        }

        //}
        return connection;
    }

    public void SynchroniserFacture(String postBody, final int id, final String etat, final String typepaiement, final String LocalState, final String SynchroOnline ) {
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
                    bdSamaFacture.updateFacture(id,etat,typepaiement,LocalState,SynchroOnline);
                    System.out.println("In onResponse");
                    try {
                        //final String babs = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView();
                                String message = "Facture synchroniser avec succèss";
                                Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
