package com.example.scoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private EditText startValueTxt;
    private EditText playerNumTxt;
    private CheckBox startValueCheck;
    private Spinner sortTypeSpinner;

    private ArrayList<String> sortTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAttributesAndViews();
        initializeSpinner();

        setStartButtonListener();
        playerNumEnterKeyHandler();

        //default theme
        Game.getInstance().setTheme(Theme.LIGHT);
    }

    private void setAttributesAndViews() {

        startBtn = findViewById(R.id.startBtn);
        startValueTxt = findViewById(R.id.startValueTxt);
        playerNumTxt = findViewById(R.id.playerNumTxt);
        startValueCheck = findViewById(R.id.startValueCheck);
        sortTypeSpinner = findViewById(R.id.sortTypeSpinner);

        sortTypes = new ArrayList<>();
    }

    private void setStartButtonListener() {

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToStart();
            }
        });
    }

    private void playerNumEnterKeyHandler() {

        playerNumTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == 66 && event.getAction() == KeyEvent.ACTION_DOWN) {
                    attemptToStart();
                }
                return false;
            }
        });
    }

    private void attemptToStart() {

        hideKeyboard();
        if(setupFilled()) {
            if(Integer.parseInt(playerNumTxt.getText().toString()) > 0) {
                setupGame();
                Intent intent = new Intent(this, PlayerSetupActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Number of players must be lager than 0.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Fill all starting fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private boolean setupFilled() {

        if(startValueCheck.isChecked()) {
            if(TextUtils.isEmpty(startValueTxt.getText().toString()) ||
                    TextUtils.isEmpty(playerNumTxt.getText().toString())) {
                return false;
            }
        } else {
            if(TextUtils.isEmpty(playerNumTxt.getText().toString())) {
                return false;
            }

        }
        return true;
    }

    private void setupGame() {

        Game.getInstance().clearPlayers();
        Game.getInstance().setNumOfPlayers(getEditTxtValue(playerNumTxt));
        if(startValueCheck.isChecked()) {
            Game.getInstance().setDefaultValue(getEditTxtValue(startValueTxt));
        }
    }

    private int getEditTxtValue(EditText txt) {
        return Integer.parseInt(txt.getText().toString());
    }

    private void initializeSpinner() {

        addSortTypes();
        setSpinnerAdapter();
        sortTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Game.getInstance().setSortType(SortType.NOSORT);
                        break;
                    case 1:
                        Game.getInstance().setSortType(SortType.LOWEST_FIRST);
                        break;
                    case 2:
                        Game.getInstance().setSortType(SortType.HIGHEST_FIRST);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerAdapter() {

        ArrayAdapter<String> spinnerSortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortTypes);
        sortTypeSpinner.setAdapter(spinnerSortAdapter);
    }

    private void addSortTypes() {

        sortTypes.add("Not sorted");
        sortTypes.add("Lowest first");
        sortTypes.add("Highest first");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.light_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Game.getInstance().setTheme(Theme.LIGHT);
                return true;
            case R.id.dark_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Game.getInstance().setTheme(Theme.DARK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}