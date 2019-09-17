package com.webianks.hatkemessenger.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.webianks.hatkemessenger.Groupe;
import com.webianks.hatkemessenger.Ormlite;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.adapters.GroupesAdapter;

import java.util.List;

public class Groupes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroupesAdapter groupeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRcv();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ajout d\'un groupe", Snackbar.LENGTH_LONG)
                        .setAction("adding", null).show();
                startActivity(new Intent(getApplicationContext(),AddGroupe.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        final Ormlite ormlite = new Ormlite(getApplicationContext());
        final List<Groupe> groupes = ormlite.getGroupes();
        Log.e("test***",groupes.toString());
        recyclerView = (RecyclerView) findViewById(R.id.groupesrcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        groupeAdapter = new GroupesAdapter(this, groupes);
        recyclerView.setAdapter(groupeAdapter);
        groupeAdapter.setOnItemClickListener(new GroupesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplicationContext(),GroupeDetail.class)
                        .putExtra("idGroupe",groupes.get(position).getId()));

            }
        });
    }

}
