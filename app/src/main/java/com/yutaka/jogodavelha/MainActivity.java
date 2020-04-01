package com.yutaka.jogodavelha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for(int c1 = 0; c1<=2; c1++){  // automatizado poderia fazer 1 por 1
            for(int c2 = 0; c2<=2; c2++){
                String buttonID = "button_" + c1 + c2;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[c1][c2] = findViewById(resID);
                buttons[c1][c2].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9){
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private Boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int c1 = 0; c1 <= 2; c1++){
            for (int c2 = 0; c2 <= 2; c2++) {
                field[c1][c2] = buttons[c1][c2].getText().toString();
            }
        }

        for (int c = 0; c <= 2; c++) { // VERIFICA LINHAS
            if (field[c][0].equals(field[c][1]) && field[c][0].equals(field[c][2]) // VERIFICA SE SÃO IGUAIS
                     && !field[c][0].equals("")) {//VERIFICA SE NÃO SÃO 3 VAZIOS
                return true;
            }
        }

        for (int c = 0; c <= 2; c++) { // VERIFICA COLUNAS
            if (field[0][c].equals(field[1][c]) && field[0][c].equals(field[2][c]) // VERIFICA SE SÃO IGUAIS
                    && !field[0][c].equals("")) {//VERIFICA SE NÃO SÃO 3 VAZIOS
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])  // VERIFICA DUAS DIAGONAIS
                && !field[0][0].equals("")
            || field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
                return true;
        }

        return false;
    }

    private void player1Wins () {
        player1Points++;
        Toast.makeText(this, "Player 1 venceu!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins () {
        player2Points++;
        Toast.makeText(this, "Player 2 venceu!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Empate!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points );
        textViewPlayer2.setText("Player 2: " + player2Points );
    }

    private void resetBoard() {
        for (int c1 = 0; c1 <= 2; c1++) {
            for (int c2 = 0; c2 <= 2; c2++) {
                buttons[c1][c2].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override      // EVITA RESETAR AO VIRAR A TELA
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    } 
}
