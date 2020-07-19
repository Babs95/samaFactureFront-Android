package com.example.samafacture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewFournisseurDialog extends DialogFragment {
    private static final String TAG = "NewFournisseurDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;
    private EditText txtlibelle;
    private Button saveFour,cancelFour;
    private String libelle,babs;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fournisseur_dialog, container, false);
        saveFour = view.findViewById(R.id.BtnNewFour);
        cancelFour = view.findViewById(R.id.BtnCancelNewFour);
        txtlibelle = view.findViewById(R.id.inputLibelleFournisseur);

        saveFour.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                libelle=txtlibelle.getText().toString().trim();
                babs=libelle;
                String postBody="{\n" +
                        "    \"libelle\": \""+libelle+"\"\n" +
                        "}";
                postFournisseur(postBody);
                getDialog().dismiss();

            }

        });

        cancelFour.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getDialog().dismiss();

            }

        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (NewFournisseurDialog.OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

    public void postFournisseur(String postBody) {
        try {

            String url = "https://api-samafacture.herokuapp.com/api/fournisseur/store";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, postBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String error = getString(R.string.error_connection);
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("In onResponse");
                    //mOnInputSelected.sendInput(mInput.getText().toString());
                    try {
                        //final String babs = response.body().string();
                        if(getActivity() == null){
                            mOnInputSelected.sendInput(babs);
                        }


                    }catch (Exception e){
                        System.out.println(e);
                    }

                }
            });


        } catch (Exception e){

        }
    }
}
