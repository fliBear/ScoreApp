package com.example.scoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private RecyclerView recView;
    GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        recView = findViewById(R.id.recView);
        adapter = new GameAdapter(this);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.nextRound).setVisible(true);
        menu.findItem(R.id.addPlayer).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.nextRound:
                startNextRound();
                return true;
            case R.id.light_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Game.getInstance().setTheme(Theme.LIGHT);
                return true;
            case R.id.dark_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Game.getInstance().setTheme(Theme.DARK);
                return true;
            case R.id.addPlayer:
                addPlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startNextRound() {

        if(Game.getInstance().getNumOfPlayers() > 0) {
            Game.getInstance().startNextRound();
            adapter.notifyDataSetChanged();
            showKeyboard();
        } else {
            Toast.makeText(this, "No players in game.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void addPlayer() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_add_player, (ViewGroup) findViewById(R.id.content));

        EditText nameInput = inflate.findViewById(R.id.addPlayerNameTxt);
        EditText scoreInput = inflate.findViewById(R.id.addPlayerScoreTxt);

        builder.setView(inflate);
        builder.setTitle("Add player");
        setAlertResponses(builder, nameInput, scoreInput);
        builder.create().show();
    }

    private void setAlertResponses(AlertDialog.Builder builder, EditText nameInput, EditText scoreInput) {

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(addPlayerDataFilled(nameInput, scoreInput)) {

                    Game.getInstance().createPlayer(getNewPlayerName(nameInput), getNewPlayerScore(scoreInput));
                    Game.getInstance().addPlayer();
                    Game.getInstance().sortPlayers();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GameActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private boolean addPlayerDataFilled(EditText nameInput, EditText scoreInput) {
        return !TextUtils.isEmpty(nameInput.getText().toString()) && !TextUtils.isEmpty(scoreInput.getText().toString());
    }

    private String getNewPlayerName(EditText nameInput) {
        return nameInput.getText().toString();
    }

    private int getNewPlayerScore(EditText scoreInput) {
        return Integer.parseInt(scoreInput.getText().toString());
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    private void confirmExit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }
}
