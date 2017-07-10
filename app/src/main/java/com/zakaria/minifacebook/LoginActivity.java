package com.zakaria.minifacebook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;

public class LoginActivity extends FragmentActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



}
