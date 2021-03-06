package com.example.scoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerSetupActivity extends AppCompatActivity {

    private TextView infoTxt;
    private EditText enterPlayerTxt;
    private EditText enterScoreTxt;
    private ImageView createPlayerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setup);

        setViews();
        setCreatePlayerListener();
        createPlayerEnterKeyHandler();
    }

    private void setViews() {

        infoTxt = findViewById(R.id.infoTxt);
        enterPlayerTxt = findViewById(R.id.enterPlayerTxt);
        enterScoreTxt = findViewById(R.id.enterScoreTxt);
        createPlayerImg = findViewById(R.id.createPlayerImg);
    }

    private void setCreatePlayerListener() {

        createPlayerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPlayerInSetup();
            }
        });
    }

    private void createPlayerEnterKeyHandler() {

        if(Game.getInstance().hasDefaultValue()) {
            enterPlayerTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(keyCode == 66 && event.getAction() == KeyEvent.ACTION_DOWN) {
                        createPlayerInSetup();
                    }
                    return false;
                }
            });

        } else {
            enterScoreTxt.setVisibility(View.VISIBLE);
            enterPlayerTxt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            enterScoreTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(keyCode == 66) {
                        createPlayerInSetup();
                    }
                    return false;
                }
            });
        }

    }

    /*
    create player if user entered data and reset edit texts
     */
    private void createPlayerInSetup() {

        if(createPlayerFieldsFiled()) {
            createPlayer();
            enterPlayerTxt.setText("");
            enterScoreTxt.setText("");
            checkForSetupCompleted();
        }
    }

    private boolean createPlayerFieldsFiled() {

        if(Game.getInstance().hasDefaultValue()) {
            if(TextUtils.isEmpty(enterPlayerTxt.getText().toString())) {
                return false;
            }
        } else {
            if(TextUtils.isEmpty(enterPlayerTxt.getText().toString()) ||
                    TextUtils.isEmpty(enterScoreTxt.getText().toString())) {
                return false;
            }
        }
        return true;
    }

    private void createPlayer() {

        if(Game.getInstance().hasDefaultValue()) {
            Game.getInstance().createPlayer(getEditTxtString(enterPlayerTxt));
        } else {
            Game.getInstance().createPlayer(getEditTxtString(enterPlayerTxt), getEditTxtValue(enterScoreTxt));
        }
    }

    /*
    start next activity and sort players by game sort type
     */
    private void checkForSetupCompleted() {

        if(Game.getInstance().isSetupFinished()) {
            Game.getInstance().sortPlayers();
            Intent intent = new Intent(this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            enterPlayerTxt.requestFocus();
            showKeyboard();
        }
    }

    private String getEditTxtString(EditText txt) {
        return txt.getText().toString();
    }

    private int getEditTxtValue(EditText txt) {
        return Integer.parseInt(txt.getText().toString());
    }

    private void showKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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

