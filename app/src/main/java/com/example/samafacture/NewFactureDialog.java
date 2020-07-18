package com.example.samafacture;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.Fournisseur;
import com.example.samafacture.Models.Mois;
import com.example.samafacture.Models.User;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.google.gson.Gson;


public class NewFactureDialog extends DialogFragment  {
    private static final String TAG = "NewFactureDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private EditText txtlibelle,txtmontant;
    private Button save;
    private TextView mActionOk, mActionCancel;
    private String etat,babs,libelle,montant,mois,fournisseur;
    private List<Fournisseur>ListFournisseur;
    private List<String>ListM;
    private List<String>ListF;
    private List<Mois>ListMois;
    private BdSamaFacture bdSamaFacture;
    private Spinner spinnerMois;
    private Spinner spinnerFour;
    private RecyclerView ListFactureRecyclerView;
    private ArrayList<Facture> ListFacture;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_facture_dialog, container, false);
        bdSamaFacture = new BdSamaFacture(getActivity());
        save = view.findViewById(R.id.BtnEnregistreFact);
        mActionCancel = view.findViewById(R.id.action_cancel);
        txtlibelle = view.findViewById(R.id.txt_input_libelle);
        ListFournisseur = new ArrayList<>();
        ListMois = new ArrayList<>();
        ListM = new ArrayList<>();
        ListF = new ArrayList<>();
        txtmontant = view.findViewById(R.id.txt_input_montant);
        spinnerMois = (Spinner) view.findViewById(R.id.txt_input_mois);
        spinnerFour = (Spinner) view.findViewById(R.id.spinner_typepaiement);
        //loadSpinnerDataMois();
        //loadSpinnerDataFournisseur();
        List<User> userList = bdSamaFacture.ListUser();
        for (int i=0;i<userList.size();i++) {
            System.out.println(userList.get(i).getId());
            System.out.println(userList.get(i).getLogin());
            System.out.println(userList.get(i).getPassword());
            System.out.println(userList.get(i).getUser_id());
        }


        ListFacture = new ArrayList<>();
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        //Chargement Spinner Mois from Database
        ListMois = bdSamaFacture.ListMois();
        for (int i=0;i<ListMois.size();i++){
            ListM.add(ListMois.get(i).getLibelle());
        }
        spinnerMois.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ListM));

        //Chargement Spinner Fournisseur from Database
        ListFournisseur = bdSamaFacture.ListFournisseur();
        for (int i=0;i<ListFournisseur.size();i++){
            ListF.add(ListFournisseur.get(i).getLibelle());
        }
        spinnerFour.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ListF));

        spinnerFour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             fournisseur= spinnerFour.getItemAtPosition(spinnerFour.getSelectedItemPosition()).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
         spinnerMois.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               mois=spinnerMois.getItemAtPosition(spinnerMois.getSelectedItemPosition()).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                libelle=txtlibelle.getText().toString().trim();
                babs=libelle;
                montant=txtmontant.getText().toString().trim();

                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                //LocalDateTime now = LocalDateTime.now(); dtf.format(now)
                String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                List<User> ListUserConnected = bdSamaFacture.ListUser();
                int IdUser=0;
                for (int i=0;i<ListUserConnected.size();i++) {
                    IdUser=ListUserConnected.get(i).getUser_id();
                }
                System.out.println("Id User Connected"+IdUser);
                System.out.println("Datez now"+currentDate);
              bdSamaFacture.createFacture(libelle,currentDate,montant,"non-payer",IdUser,fournisseur,null,null,mois,"non-payer","nonOk",null);
              mOnInputSelected.sendInput(babs);
                getDialog().dismiss();

            }

        });

        return view;
    }


   private void loadSpinnerDataMois() {
       String url = "https://api-samafacture.herokuapp.com/api/mois/getall";
       OkHttpClient  client = new OkHttpClient();
       Request request = new Request.Builder()
               .url(url)
               .build();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
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
               try{
                   JSONObject jsonObject=new JSONObject(response.body().string());
                   if(jsonObject.getInt("success")==1){
                       JSONArray jsonArray=jsonObject.getJSONArray("Mois");

                       for(int i=0;i<jsonArray.length();i++){
                           JSONObject jsonObject1=jsonArray.getJSONObject(i);
                           //String moisLib=jsonObject1.getString("libelle");
                           //ListM.add(type);
                           Gson gson=new Gson();
                           Mois mois = gson.fromJson(jsonObject1.toString(), Mois.class);
                           //ListMois.add(mois);
                           bdSamaFacture.createMois(mois.getLibelle(),mois.getId());
                       }
                   }
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           //ListM
                           spinnerMois.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ListM));
                       }
                   });
               }catch (JSONException e){e.printStackTrace();}
           }
       });
   }
    private void loadSpinnerDataFournisseur() {
        String url = "https://api-samafacture.herokuapp.com/api/fournisseur/getall";
        OkHttpClient  client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Fournisseur");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String type=jsonObject1.getString("libelle");
                            System.out.println(type);
                            //ListF.add(type);
                            Gson gson=new Gson();
                            Fournisseur f = gson.fromJson(jsonObject1.toString(), Fournisseur.class);
                            ListFournisseur.add(f);
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //spinnerFour.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ListF));
                        }
                    });
                }catch (JSONException e){e.printStackTrace();}
            }
        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (NewFactureDialog.OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
