package com.example.samafacture.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.samafacture.MainActivity;
import com.example.samafacture.Models.Fournisseur;
import com.example.samafacture.Models.User;
import com.example.samafacture.R;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private BdSamaFacture bdSamaFacture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bdSamaFacture = new BdSamaFacture(getActivity());
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText(R.string.home_welcome);
        loadSpinnerDataFournisseur();
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });*/
        return root;
    }
    private void loadSpinnerDataFournisseur() {
        String url = "https://api-samafacture.herokuapp.com/api/fournisseur/getall";
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
                        String error = getString(R.string.error_connection);
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
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
}
