package com.developer.mechanicrooms.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

private MutableLiveData<String> mText;

public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Main page");
        }

public LiveData<String> getText() {
        return mText;
        }
        }