package com.prueba.firebase.pruebafirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prueba.firebase.pruebafirebase.dummy.DummyContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a list of Comidas. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ComidaDetailActivity} representing
 * item precio. On tablets, the activity presents the list of items and
 * item precio side-by-side using two vertical panes.
 */
public class ComidaListActivity extends AppCompatActivity {

    private static final String PATH_FOOD = "food";
    private static final String PATH_PROFILE = "profile";
    private static final String PATH_CODE = "code";

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.btnSave)
    Button btnSave;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.comida_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.comida_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_FOOD);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // toma los valores del objeto con sus valores  para agregarlo a una clase
                // e insertarlo el id insertandolo tomando el key

                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                comida.setId(dataSnapshot.getKey());

                // verificar si el objeto existe si no se agrega

                if (!DummyContent.ITEMS.contains(comida)) {
                    DummyContent.addItem(comida);
                }

                // actualizar el adaptador

                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                comida.setId(dataSnapshot.getKey());

                // verificar si el objeto existe si no se agrega

                if (DummyContent.ITEMS.contains(comida)) {
                    DummyContent.addItem(comida);
                }

                // actualizar el adaptador

                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                comida.setId(dataSnapshot.getKey());

                // verificar si el objeto existe si no se agrega

                if (DummyContent.ITEMS.contains(comida)) {
                    DummyContent.deleteItem(comida);
                }

                // actualizar el adaptador

                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(ComidaListActivity.this, "Moved", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ComidaListActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick(R.id.btnSave)
    public void onViewClicked() {
        DummyContent.Comida comida = new DummyContent.Comida(
                etName.getText().toString().trim(),
                etPrice.getText().toString().trim()
        );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_FOOD);

        reference.push().setValue(comida);
        etName.setText("");
        etPrice.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_info:
                //Toast.makeText(this,"funciona!",Toast.LENGTH_LONG).show();
                final TextView tvCode = new TextView(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                tvCode.setLayoutParams(params);
                tvCode.setGravity(Gravity.CENTER_HORIZONTAL);
                tvCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);

                FirebaseDatabase databse = FirebaseDatabase.getInstance();
                DatabaseReference reference = databse.getReference(PATH_PROFILE).child(PATH_CODE);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvCode.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ComidaListActivity.this, "No se puede cargar el codigo.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.comidaList_dialog_title)
                        .setPositiveButton(R.string.comidaList_dialog_ok,null);
                builder.setView(tvCode);
                builder.show();


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ComidaListActivity mParentActivity;
        private final List<DummyContent.Comida> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.Comida item = (DummyContent.Comida) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ComidaDetailFragment.ARG_ITEM_ID, item.getId());
                    ComidaDetailFragment fragment = new ComidaDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.comida_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ComidaDetailActivity.class);
                    intent.putExtra(ComidaDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ComidaListActivity parent,
                                      List<DummyContent.Comida> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comida_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mIdView.setText("$" + mValues.get(position).getPrecio());
            holder.mContentView.setText(mValues.get(position).getNombre());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference(PATH_FOOD);
                    reference.child(mValues.get(position).getId()).removeValue();

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @OnClick(R.id.btnDelete)
        public void onViewClicked() {
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            @BindView(R.id.btnDelete)
            Button btnDelete;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
