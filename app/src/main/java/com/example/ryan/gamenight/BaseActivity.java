package com.example.ryan.gamenight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toastShort(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
