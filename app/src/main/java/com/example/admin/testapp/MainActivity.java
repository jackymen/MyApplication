package com.example.admin.testapp;

import android.app.ActivityManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    Configuration mconfig = new Configuration();
    @InjectView(R.id.fz_16)
    Button fz16;
    @InjectView(R.id.fz_24)
    Button fz24;
    @InjectView(R.id.fz_32)
    Button fz32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
/*
    @OnClick({R.id.fz_16, R.id.fz_24, R.id.fz_32})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.fz_16:
                mconfig.fontScale = 0.75f;
                break;
            case R.id.fz_24:
                mconfig.fontScale = 1.0f;
                break;
            case R.id.fz_32:
                mconfig.fontScale = 1.25f;
                break;
        }
        setFontSize();
    }
    */

    public void setFontSize() {
        Method method;

        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            try {
                Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
                method = am.getClass().getMethod("updatePersistentConfiguration", Configuration.class);
                method.invoke(am, mconfig);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick({R.id.fz_16, R.id.fz_24, R.id.fz_32})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fz_16:
                mconfig.fontScale = 0.75f;
                break;
            case R.id.fz_24:
                mconfig.fontScale = 1.0f;
                break;
            case R.id.fz_32:
                mconfig.fontScale = 1.25f;
                break;
        }
        setFontSize();
    }
}
