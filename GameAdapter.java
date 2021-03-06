package com.example.scoring;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{

    private Context context;

    public GameAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        setPlayerStats(holder, position);
        doEditBtn(holder, position);
        doFinishEditBtn(holder, position);
        doRoundScoreInput(holder, position);
    }

    @Override
    public int getItemCount() {
        return Game.getInstance().getPlayers().size();
    }

    private void doEditBtn(@NonNull ViewHolder holder, int position) {

        if(Game.getInstance().getPlayers().get(position).isEditing()) {
            holder.playerEdit.setVisibility(View.GONE);
            holder.editRel.setVisibility(View.VISIBLE);
            holder.editFinish.setVisibility(View.VISIBLE);
        }
    }

    private void doFinishEditBtn(@NonNull ViewHolder holder, int position) {

        if(!Game.getInstance().isEditing()) {
            holder.editRel.setVisibility(View.GONE);
            holder.editFinish.setVisibility(View.GONE);
            holder.playerEdit.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayerStats(@NonNull ViewHolder holder, int position) {

        holder.playerTxt.setText( Game.getInstance().getPlayers().get(position).getName());
        holder.roundTxt.setText(String.valueOf(Game.getInstance().getRound()));
        holder.scoreTxt.setText(String.valueOf( Game.getInstance().getPlayers().get(position).getScore()));
        holder.editPlayerTxt.setText("");
        holder.editScoreTxt.setText("");
        holder.editLastRoundTxt.setText("");
    }

    private void doRoundScoreInput(@NonNull ViewHolder holder, int position) {

        if(Game.getInstance().getPlayers().get(position).isInputtingNextRound()) {
            holder.nextRoundRel.setVisibility(View.VISIBLE);
            holder.roundScoreTxt.setText("");
        } else {
            holder.nextRoundRel.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView playerTxt;
        private TextView roundTxt;
        private TextView scoreTxt;
        private EditText editPlayerTxt;
        private EditText editScoreTxt;
        private EditText editLastRoundTxt;
        private EditText roundScoreTxt;

        private ImageView playerEdit;
        private ImageView playerDelete;
        private ImageView editFinish;
        private ImageView roundScoreConfirmImg;

        private RelativeLayout nextRoundRel;
        private RelativeLayout editRel;
        private RelativeLayout current;

        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewSetup();
            setPlayerEditBtnListener();
            setPlayerDeleteBtnListener();
            setEditFinishBtnListener();
            setRoundScoreConfirmBtnListener();
            setRoundScoreEnterKeyHandler();
        }

        private void viewSetup() {

            parent = itemView.findViewById(R.id.parent);
            playerTxt = itemView.findViewById(R.id.playerTxt);
            roundTxt = itemView.findViewById(R.id.roundTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            editPlayerTxt = itemView.findViewById(R.id.editPlayerTxt);
            editScoreTxt = itemView.findViewById(R.id.editScoreTxt);
            editLastRoundTxt = itemView.findViewById(R.id.editLastRoundTxt);
            roundScoreTxt = itemView.findViewById(R.id.roundScoreTxt);

            playerEdit = itemView.findViewById(R.id.playerEdit);
            playerDelete = itemView.findViewById(R.id.playerDelete);
            editFinish = itemView.findViewById(R.id.editFinish);
            roundScoreConfirmImg = itemView.findViewById(R.id.roundScoreConfirmImg);

            nextRoundRel = itemView.findViewById(R.id.nextRoundRel);
            editRel = itemView.findViewById(R.id.editRel);
            current = itemView.findViewById(R.id.subParent);
        }

        private void setPlayerEditBtnListener() {

            playerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!Game.getInstance().isEditing() && !Game.getInstance().isInputtingNextRound()) {
                        Game.getInstance().startEditing();
                        Game.getInstance().getPlayers().get(getAdapterPosition()).startEditing();
                        showKeyboard(parent);
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }

        private void setPlayerDeleteBtnListener() {

            playerDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!Game.getInstance().isInputtingNextRound()) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Are you sure you want to delete " + Game.getInstance().getPlayers().get(getAdapterPosition()).getName() + "?");
                        setDeleteAlertResponses(alert);
                        alert.create().show();
                    }
                }
            });
        }

        private void setDeleteAlertResponses( AlertDialog.Builder alert) {

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Game.getInstance().getPlayers().remove(getAdapterPosition());
                    Game.getInstance().removePlayer();
                    notifyDataSetChanged();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        private void setEditFinishBtnListener() {

            editFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Game.getInstance().stopEditing();
                    Game.getInstance().getPlayers().get(getAdapterPosition()).stopEditing();
                    Player player = Game.getInstance().getPlayers().get(getAdapterPosition());
                    editName(player);
                    editLastRoundScore(player);
                    editScore(player);
                    hideKeyboard(parent);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        private void editName(Player player) {

            if(!editPlayerTxt.getText().toString().matches("")) {
                player.setName(editPlayerTxt.getText().toString());
            }
        }

        private void editScore(Player player) {

            if(!editScoreTxt.getText().toString().matches("")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Manually setting score will delete round scores for " + Game.getInstance().getPlayers().get(getAdapterPosition()).getName() + ".");
                setEditScoreAlertResponses(alert, player);
                alert.create().show();
            }
        }

        private void setEditScoreAlertResponses(AlertDialog.Builder alert, Player player) {

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    player.setScore(Integer.parseInt(editScoreTxt.getText().toString()));
                    player.clearRoundScores();
                    Game.getInstance().sortPlayers();
                    notifyDataSetChanged();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        /*
        editScoreTxt has priority and this method cannot will work when nothing is entered in editScoreTxt
         */
        private void editLastRoundScore(Player player) {

            if(!editLastRoundTxt.getText().toString().matches("") && editScoreTxt.getText().toString().matches("")) {
                if(player.hasRoundData()) {
                    player.editLastRoundScore(Integer.parseInt(editLastRoundTxt.getText().toString()));
                    Game.getInstance().sortPlayers();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "No data on previous rounds.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void setRoundScoreConfirmBtnListener() {

            roundScoreConfirmImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setRoundScore();
                }
            });
        }

        private void setRoundScoreEnterKeyHandler() {

            roundScoreTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(keyCode == 66 && event.getAction() == KeyEvent.ACTION_DOWN) {
                        setRoundScore();
                    }
                    return false;
                }
            });
        }

        private void setRoundScore() {

            if(scoreInputFilled()) {
                Game.getInstance().getPlayers().get(getAdapterPosition()).stopInputtingNextRound();
                addToPlayerScore();

                if(isLastPlayer()) {
                    Game.getInstance().stopInputtingNextRound();
                    Game.getInstance().sortPlayers();
                    hideKeyboard(parent);
                } else {
                    Game.getInstance().getPlayers().get(getAdapterPosition() + 1).startInputtingNextRound();
                }

                notifyDataSetChanged();

            } else {
                Toast.makeText(context, "Please input round score for " + Game.getInstance().getPlayers().get(getAdapterPosition()).getName() + ".", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean scoreInputFilled() {
            return !TextUtils.isEmpty(roundScoreTxt.getText().toString());
        }

        private void addToPlayerScore() {
            int roundScore = Integer.parseInt(roundScoreTxt.getText().toString());
            Game.getInstance().getPlayers().get(getAdapterPosition()).addScore(roundScore);
        }

        private boolean isLastPlayer() {
            return Game.getInstance().getNumOfPlayers() - 1 == getAdapterPosition();
        }

        private void hideKeyboard(View v) {

            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        private void showKeyboard(View v) {

            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
