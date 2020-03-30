package com.example.unittesting2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.unittesting2.DAOPackage.DataViewModel;
import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;
import com.example.unittesting2.ViewModelFactoryModule.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

public class NoteActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,View.OnTouchListener,View.OnClickListener, TextWatcher {

    private EditText TestBox;
    private ConstraintLayout constraintLayout;
    private EditText toolbarEdittext;
    private TextView  textviewtoolbar;
    private RelativeLayout backarrowcontainer,checkcontainr;
    ImageButton image1,image2;
    DataViewModel viewModel;
    private GestureDetector mGestureDetector;
    private static final String TAG = "NoteActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        constraintLayout = findViewById(R.id.parent);
        TestBox = findViewById(R.id.note_text);
        toolbarEdittext =findViewById(R.id.note_edit_title);
        textviewtoolbar = findViewById(R.id.note_text_title);
        backarrowcontainer =findViewById(R.id.back_arrow_container);
        checkcontainr = findViewById(R.id.check_container);
        image1 = findViewById(R.id.toolbar_back_arrow);
        image2 = findViewById(R.id.toolbar_check);

        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        subscribeObservers();
        setListeners();

        if (savedInstanceState == null)
        {
            getIncomingIntent();
            enableEditMode();
        }


    }

///////////////////////this method is responsible for inserting & updating the data into database
//                              and return inserting & updating data through dataviewmodel.class/////////////////////////////
    private void getIncomingIntent() {
        try {
            GetterSetter getterSetterObject;
            if(getIntent().hasExtra(getString(R.string.intent_note))){
                getterSetterObject = new GetterSetter((GetterSetter) getIntent().getSerializableExtra(getString(R.string.intent_note)));
                viewModel.setIsNewNote(false);
            }
            else{
                getterSetterObject = new GetterSetter(1, "Title", "This is the NoteContent");
                viewModel.setIsNewNote(true);
            }
            viewModel.setNote(getterSetterObject);
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(getString(R.string.error_intent_note));
        }
    }


////////////////////this method is observing the livedata and set dataobject into notelayout and observing the viewstate ////////////////////////
    private void subscribeObservers(){

        viewModel.observenote().observe(this, new Observer<GetterSetter>() {
            @Override
            public void onChanged(GetterSetter note) {
                setNoteProperties(note);
            }
        });

        viewModel.observeViewState().observe(this, new Observer<DataViewModel.ViewState>() {
            @Override
            public void onChanged(DataViewModel.ViewState viewState) {
                switch (viewState){
                    case EDIT:{
                        enableContentInteraction();
                        break;
                    }
                    case VIEW:{
                        disableContentInteraction();
                        break;
                    }
                }
            }
        });
    }


    //////////////////set the data into notedetailslayout///////////////////////////////////
    private void setNoteProperties(GetterSetter note) {
        try{
            textviewtoolbar.setText(note.getTitle());
            toolbarEdittext.setText(note.getTitle());
            TestBox.setText(note.getTimestamp());
        }catch (NullPointerException e){
            e.printStackTrace();
            showSnackBar("Error displaying note properties");
        }

    }

    private void enableEditMode(){
        Log.d(TAG, "enableEditMode: called.");
        viewModel.setViewState(DataViewModel.ViewState.EDIT);
    }

    /////////ths method is used to insert & update the note into database/////////////////////////
    private void disableEditMode(){
        Log.d(TAG, "disableEditMode: called.");
        viewModel.setViewState(DataViewModel.ViewState.VIEW);

        if(!TextUtils.isEmpty(TestBox.getText())){
            try {
                viewModel.updateNote1(toolbarEdittext.getText().toString(), TestBox.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar("Error setting note properties");
            }
        }

        saveNote();
    }

    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        TestBox.setOnTouchListener(this);
        image2.setOnClickListener(this);
        textviewtoolbar.setOnClickListener(this);
        image1.setOnClickListener(this);
        toolbarEdittext.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("has_started", true);
    }

    private void showSnackBar(String message){
        if(!TextUtils.isEmpty(message)) {

            Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
        }
    }


    private void saveNote(){
        Log.d(TAG, "saveNote: called.");
        try {
            viewModel.saveNote().observe(this, new Observer<Resource<Integer>>() {
                @Override
                public void onChanged(Resource<Integer> integerResource) {
                    try {
                        if(integerResource != null){
                            switch (integerResource.status){

                                case SUCCESS:{
                                    Log.e(TAG, "onChanged: save note: success..." );
                                    showSnackBar(integerResource.message);
                                    break;
                                }

                                case ERROR:{
                                    Log.e(TAG, "onChanged: save note: error..." );
                                    showSnackBar(integerResource.message);
                                    break;
                                }

                                case LOADING:{
                                    Log.e(TAG, "onChanged: save note: loading..." );
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(e.getMessage());
        }
    }


    private void disableContentInteraction(){
        hideKeyboard(this);

        backarrowcontainer.setVisibility(View.VISIBLE);
        checkcontainr.setVisibility(View.GONE);

        textviewtoolbar.setVisibility(View.VISIBLE);
        toolbarEdittext.setVisibility(View.GONE);

        TestBox.setKeyListener(null);
        TestBox.setFocusable(false);
        TestBox.setFocusableInTouchMode(false);
        TestBox.setCursorVisible(false);
        TestBox.clearFocus();
    }

    private void enableContentInteraction(){
        backarrowcontainer.setVisibility(View.GONE);
        checkcontainr.setVisibility(View.VISIBLE);

        textviewtoolbar.setVisibility(View.GONE);
        toolbarEdittext.setVisibility(View.VISIBLE);

        TestBox.setKeyListener(new EditText(this).getKeyListener());
        TestBox.setFocusable(true);
        TestBox.setFocusableInTouchMode(true);
        TestBox.setCursorVisible(true);
        TestBox.requestFocus();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textviewtoolbar.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                finish();
                break;
            }
            case R.id.toolbar_check:{
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                toolbarEdittext.requestFocus();
                toolbarEdittext.setSelection(toolbarEdittext.length());
                break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {
        if(viewModel.shouldNavigateBack()){
            super.onBackPressed();
        }
        else{
            onClick(image2);
        }
    }




}
