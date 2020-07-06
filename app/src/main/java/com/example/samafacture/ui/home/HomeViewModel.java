package com.example.samafacture.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samafacture.Models.User;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Sama Facture est une application qui vous permettra de gérer toutes vos factures en fontion de vos différents fournisseurs afin de vous faciliter la vie.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}