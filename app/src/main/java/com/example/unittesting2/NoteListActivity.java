package com.example.unittesting2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;

import com.example.unittesting2.Adapter.NoteAdapter;
import com.example.unittesting2.DAOPackage.DataViewModel;
import com.example.unittesting2.DAOPackage.NotelistViewModel;
import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;
import com.example.unittesting2.UI.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NoteListActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.OnNoteListener {

    //**** Constructor is used assign the value of our class variables..............

    private static final String TAG = "NotesListActivity";

    // ui components
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CoordinatorLayout parent;

    // vars
    private NotelistViewModel viewModel;
    private NoteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview1);
        fab = findViewById(R.id.fab1);
        parent = findViewById(R.id.parent);

        fab.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(NotelistViewModel.class);

        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribeObservers();
    }


    private void subscribeObservers(){

        Log.d(TAG, "subscribeObservers: called.");

        viewModel.ObserveAllNotes().observe(this, new Observer<List<GetterSetter>>() {
            @Override
            public void onChanged(List<GetterSetter> getterSetters) {
                if(getterSetters!= null){
                    adapter.setNotes(getterSetters);
                }
            }
        });

        viewModel.getNotes();
    }


    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpacingItemDecorator(10));
        adapter = new NoteAdapter(this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(GetterSetter note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(getString(R.string.intent_note), note);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:{
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void showSnackBar(String message){
        if(!TextUtils.isEmpty(message)) {

            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    GetterSetter note = adapter.getNote(viewHolder.getAdapterPosition());
                    adapter.removeNote(note);

                    try {
                        final LiveData<Resource<Integer>> deleteAction = viewModel.deleteNote(note);
                        deleteAction.observe(NoteListActivity.this, new Observer<Resource<Integer>>() {
                            @Override
                            public void onChanged(Resource<Integer> integerResource) {
                                if(integerResource != null){
                                    showSnackBar(integerResource.message);
                                }
                                deleteAction.removeObserver(this);
                            }

                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar(e.getMessage());
                    }

                }

    };



}
