package com.prueba.firebase.pruebafirebase.CloudMessaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.prueba.firebase.pruebafirebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloudMessagingActivity extends AppCompatActivity {


    @BindView(R.id.spTopics)
    Spinner spTopics;
    @BindView(R.id.tvTopics)
    TextView tvTopics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btnSubcribir, R.id.btnDesuscribir})
    public void onViewClicked(View view) {

        String topic = getResources().getStringArray(R.array.topicsValues)[spTopics.getSelectedItemPosition()];

        switch (view.getId()) {
            case R.id.btnSubcribir:
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                break;
            case R.id.btnDesuscribir:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                break;
        }
    }
}
