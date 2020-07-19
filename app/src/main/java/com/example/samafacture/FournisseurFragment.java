package com.example.samafacture;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samafacture.Models.Fournisseur;
import com.example.samafacture.Models.MyFournisseurAdapter;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class FournisseurFragment extends Fragment implements MyFournisseurAdapter.OnFournisseurListener,NewFournisseurDialog.OnInputSelected, EditFournisseurDialog.OnUpdateFour {

    private RecyclerView ListFournisseurRecyclerView;
    private ArrayList<Fournisseur> ListFournisseur;
    private BdSamaFacture bdSamaFacture;
    private Button btnsaveFour,btnEditFour;
    private ImageView editFourImageview;
    private MyFournisseurAdapter myFournisseurAdapter;
    ProgressBar progressBar;
    public FournisseurFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fournisseur, container, false);
        bdSamaFacture = new BdSamaFacture(getActivity());
        ListFournisseurRecyclerView = view.findViewById(R.id.ListFourRecycler);
        btnsaveFour = view.findViewById(R.id.btnSaveFournisseur);
        editFourImageview = view.findViewById(R.id.imageViewEditFour);
        ListFournisseur = new ArrayList<>();

        //Progessbar
        progressBar = (ProgressBar) view.findViewById(R.id.spin_kitFour);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);
        progressBar.setVisibility(View.GONE);

        loadSpinnerDataFournisseur();

        btnsaveFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFournisseurDialog dialog = new NewFournisseurDialog();
                dialog.setTargetFragment(FournisseurFragment.this, 1);
                dialog.show(getFragmentManager(), "NewFournisseurDialog");
            }
        });


        return view;
    }
    private void initRecyclerViewFournisseur(){
        ListFournisseur = (ArrayList<Fournisseur>) bdSamaFacture.ListFournisseur();
        myFournisseurAdapter = new MyFournisseurAdapter(getActivity(),ListFournisseur,this, this);
        ListFournisseurRecyclerView.setAdapter(myFournisseurAdapter);
        ListFournisseurRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ListFournisseurRecyclerView);
        //ListFournisseurRecyclerView.notifyAll();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //ListFournisseur.remove(viewHolder.getAdapterPosition());
            System.out.println(ListFournisseur.get(viewHolder.getAdapterPosition()).getFournisseur_id());
            System.out.println(ListFournisseur.get(viewHolder.getAdapterPosition()).getLibelle());
            //ListFournisseur.n
        }
    };

    @Override
    public void OnFournisseurClick(int position) {
        System.out.println("OnClick Fournisseur");
        System.out.println(ListFournisseur.get(position).getId());
        System.out.println(ListFournisseur.get(position).getLibelle());
        System.out.println(ListFournisseur.get(position).getFournisseur_id());
    }

    private void loadSpinnerDataFournisseur() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://api-samafacture.herokuapp.com/api/fournisseur/getall";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            String error = getString(R.string.error_connection);
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    bdSamaFacture.deleteAllFournisseur();
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Fournisseur");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            //String fourniss=jsonObject1.getString("libelle");
                            Gson gson=new Gson();
                            Fournisseur f = gson.fromJson(jsonObject1.toString(), Fournisseur.class);
                            bdSamaFacture.createFournisseur(f.getLibelle(),f.getId());
                        }
                    }

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                initRecyclerViewFournisseur();
                            }
                        });
                    }

                }catch (JSONException e){e.printStackTrace();}
            }
        });

    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);
        System.out.println("send Input"+input);
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    String message = "Fournisseur crée avec succèss";
                    Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                    loadSpinnerDataFournisseur();
                }
            });
        }
    }

    @Override
    public void UpdateFour(String input) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    String message = "Fournisseur modifier avec succèss";
                    Toasty.success(getActivity(), message, Toast.LENGTH_SHORT, true).show();
                    loadSpinnerDataFournisseur();
                }
            });
        }
    }
}
