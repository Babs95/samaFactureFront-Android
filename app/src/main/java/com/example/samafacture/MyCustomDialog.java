package com.example.samafacture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.example.samafacture.InscriptionActivity.JSON;

public class MyCustomDialog extends DialogFragment {
    private static final String TAG = "MyCustomDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private EditText mInput;
    private CheckBox actif,inactif;
    private TextView mActionOk, mActionCancel;
    private String etat;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_custom_dialog, container, false);
        mActionOk = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.inputAnnee);
        actif = view.findViewById(R.id.inputActive);
        inactif = view.findViewById(R.id.inputInactive);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    etat="";
                if (actif.isChecked()) {
                    etat= actif.getText().toString().trim()+"";
                }
                if (inactif.isChecked()) {
                    etat= inactif.getText().toString().trim()+"";
                }
                String postBody="{\n" +
                        "    \"libelle\": \""+mInput.getText().toString().trim()+"\",\n" +
                        "    \"etat\": \""+etat+"\"\n" +
                        "}";
                System.out.println(postBody);
                postAnnee(postBody);



                getDialog().dismiss();

            }
        });

        return view;
    }
    public void postAnnee(String postBody) {
        try {

            String url = "https://api-samafacture.herokuapp.com/api/annee/store";
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
                                String message = "Annee crée avec succèss";
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                mOnInputSelected.sendInput(mInput.getText().toString());
                              //  EraseFields();
                               // wave.stop();
                            }
                        });


                    }catch (Exception e){
                        System.out.println(e);
                    }
                    /*try {
                        System.out.println("In try catch");



                    }catch (Exception e){

                    }*/
                }
            });

        } catch (Exception e){

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
