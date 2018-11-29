package com.prueba.firebase.pruebafirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    public static final String PATH_START = "start";
    public static final String PATH_MESSAGE = "message";

    @BindView(R.id.idMensaje)
    TextView idMensaje;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btEnviar)
    Button btEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        idMensaje = (TextView) findViewById(R.id.idMensaje);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference(PATH_START).child(PATH_MESSAGE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idMensaje.setText(dataSnapshot.getValue(String.class));
                etMessage.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error al consultar firebase", Toast.LENGTH_LONG).show();
            }
        });


    }

    @OnClick(R.id.btEnviar)
    public void onViewClicked() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference(PATH_START).child(PATH_MESSAGE);

        reference.setValue(etMessage.getText().toString().trim());
        etMessage.setText("");

    }
}
