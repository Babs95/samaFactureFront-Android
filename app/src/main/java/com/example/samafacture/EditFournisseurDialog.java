package com.example.samafacture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EditFournisseurDialog extends DialogFragment {
    private static final String TAG = "EditFournisseurDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public interface OnUpdateFour{
        void UpdateFour(String input);
    }
    public OnUpdateFour mOnInputSelected;
    private EditText txtlibelleUpdate;
    private Button updateFour,cancelupdateFour;
    private String libelle,babs;
    private Integer idFour = 0;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fournisseur_dialog, container, false);
        updateFour = view.findViewById(R.id.BtnUpdateFour);
        cancelupdateFour = view.findViewById(R.id.BtnCancelUpdateFour);
        txtlibelleUpdate = view.findViewById(R.id.inputLibelleFourUpdate);

        Bundle bundle = getArguments();
        txtlibelleUpdate.setText(bundle.getString("Libelle",""));
        idFour =  bundle.getInt("Id",0);

        updateFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libelle=txtlibelleUpdate.getText().toString().trim();
                babs=libelle;
                updateFournisseur();
                getDialog().dismiss();

            }

        });

        cancelupdateFour.setOnClickListener(new View.OnClickListener() {
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
            mOnInputSelected = (EditFournisseurDialog.OnUpdateFour) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

    public void updateFournisseur() {
        try {

            String url = "https://api-samafacture.herokuapp.com/api/fournisseur/UpdateFournisseur/"+idFour+"/"+libelle;
            OkHttpClient client = new OkHttpClient();
            //RequestBody body = RequestBody.create(JSON, postBody);
            Request request = new Request.Builder()
                    .url(url)
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
                            mOnInputSelected.UpdateFour(babs);
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
