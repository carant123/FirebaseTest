package com.prueba.firebase.pruebafirebase.CloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.prueba.firebase.pruebafirebase.R;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloudMessagingActivity extends AppCompatActivity {

    private static final String SP_TOPICS = "sharePreferencesTopics";

    @BindView(R.id.spTopics)
    Spinner spTopics;
    @BindView(R.id.tvTopics)
    TextView tvTopics;


    private Set<String> mTopicsSet;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);
        ButterKnife.bind(this);
        configSharedPreferences();

        if(FirebaseInstanceId.getInstance().getToken() != null){
            Log.i("Token MainActivity", FirebaseInstanceId.getInstance().getToken());
        }

    }

    private void configSharedPreferences() {
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mTopicsSet = mSharedPreferences.getStringSet(SP_TOPICS, new HashSet<String>());
        showTipics();
    }

    private void showTipics() {
        tvTopics.setText(mTopicsSet.toString());
    }

    @OnClick({R.id.btnSubcribir, R.id.btnDesuscribir})
    public void onViewClicked(View view) {

        String topic = getResources().getStringArray(R.array.topicsValues)[spTopics.getSelectedItemPosition()];

        switch (view.getId()) {
            case R.id.btnSubcribir:

                if (!mTopicsSet.contains(topic)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    mTopicsSet.add(topic);
                    saveSharedPreferences();
                }

                break;
            case R.id.btnDesuscribir:

                if (mTopicsSet.contains(topic)) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    mTopicsSet.remove(topic);
                    saveSharedPreferences();
                }

                break;
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.putStringSet(SP_TOPICS, mTopicsSet);
        editor.apply();
        showTipics();
    }
}
