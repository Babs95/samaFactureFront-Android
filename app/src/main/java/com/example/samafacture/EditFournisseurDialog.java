package com.example.samafacture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import okhttp3.MediaType;

public class EditFournisseurDialog extends DialogFragment {
    private static final String TAG = "EditFournisseurDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;
    private EditText txtlibelle;
    private Button editFour;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_facture_dialog, container, false);
        editFour = view.findViewById(R.id.BtnEnregistreFact);
        //mActionCancel = view.findViewById(R.id.action_cancel);
        txtlibelle = view.findViewById(R.id.txt_input_libelle);




        editFour.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //libelle=txtlibelle.getText().toString().trim();
                //babs=libelle;
                //mOnInputSelected.sendInput(babs);
                getDialog().dismiss();

            }

        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (EditFournisseurDialog.OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
