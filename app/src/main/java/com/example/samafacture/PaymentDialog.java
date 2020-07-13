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
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.MyFactureAdapter;
import com.example.samafacture.Models.Typepaiment;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class PaymentDialog extends DialogFragment implements MyFactureAdapter.OnFactureListener {
    private static final String TAG = "PaymentDialog";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onFactureClick(int position) {
        ///System.out.println("OnClick Facture");
        //System.out.println(ListFacture.get(position).getId());
        //System.out.println(ListFacture.get(position).getMontant());
        //ListFacture.get(position).getId();
        //txtMontantFact.setText(ListFacture.get(position).getMontant());
    }

    public interface OnInputSend{
        void sendBabs(String input);
    }
    public OnInputSend mOnInputSelected;

    //Constructeur
    /*public PaymentDialog(String text){
        txtMontantFact.setText(text);
    }*/

    //widgets
    private EditText txtMontantFact, txtFourFact;
    private Button btnPayBill;
    private String babs,typepaiement;
    private Spinner spinnerTypePaiement;
    private List<String> ListT;
    private List<Typepaiment>ListTypePaiement;
    private BdSamaFacture bdSamaFacture;
    private ArrayList<Facture> ListFacture;
    private int idFact = 0;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_dialog, container, false);
        txtMontantFact = view.findViewById(R.id.inputMontantFact);
        txtFourFact = view.findViewById(R.id.inputFourFact);
        btnPayBill = view.findViewById(R.id.btnPayeBill);
        bdSamaFacture = new BdSamaFacture(getActivity());
        ListTypePaiement = new ArrayList<>();
        ListT = new ArrayList<>();
        //Chargement ListFactures
        ListFacture = (ArrayList<Facture>) bdSamaFacture.getFactures();
        spinnerTypePaiement = (Spinner) view.findViewById(R.id.spinner_typepaiement);

        Bundle bundle = getArguments();
        System.out.println("Send Babs"+bundle.getString("Montant",""));
        txtMontantFact.setText(bundle.getString("Montant",""));
        txtFourFact.setText(bundle.getString("Fournisseur",""));
        idFact =  bundle.getInt("Id",0);
        //String imageLink = bundle.getString("Montant","");

        //Chargement Spinner Typepaiement from Database
        ListTypePaiement = bdSamaFacture.ListTypePaiement();
        for (int i=0;i<ListTypePaiement.size();i++){
            ListT.add(ListTypePaiement.get(i).getLibelle());
        }
        spinnerTypePaiement.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ListT));
        spinnerTypePaiement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typepaiement= spinnerTypePaiement.getItemAtPosition(spinnerTypePaiement.getSelectedItemPosition()).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                babs = "cool";
                bdSamaFacture.updateFacture(idFact,"payer",typepaiement,"payer","nonOk");
                mOnInputSelected.sendBabs(babs);
                getDialog().dismiss();

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSend) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
