package com.cataldo.chris.memorychallenge;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    int[] gameArray = new int[20];
    int[] matchedItems = new int[20];
    int[] computeryMemory = new int[20]; // contains revealed items
    String[] imageArray = {"bear","dinosaur","elephant","hippo","jellyfish","seahorse","snake","tiger","turtle","whale"};
    int humanPickCount = 0; // count of current human player picks
    int[] humanPicks = new int[2]; //  current human picks
    String playerTurn; // current turn
    Map<String, Integer> playerScores = new HashMap<String, Integer>();
    ImageView imageView1, imageView2; // current chosen items
    TextView currentPlayer; // current player display
    GridView gameGrid;
    int computerFirstPick, computerSecondPick;
    int skillLevel;
    boolean rememberMatch; // if the computer remembers a match
    boolean gameOver = false;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        skillLevel = i.getIntExtra("skillLevel", 1);

        setupGame();
        gameGrid = (GridView) findViewById(R.id.gridView);
        // Instance of GridViewAdapter Class
        gameGrid.setAdapter(new GridViewAdapter(this));

        /**
         * On Click event for Single Gridview Item
         * */
        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View imgView,
                                    int position, long id) {
                //Log.d("pselectioncounthere", "playerselectioncount: " + humanPickCount);
                //Log.d("playerTurn", "playerTurn: " + playerTurn);
                //Log.d("player position", "position: " + position);
                if(playerTurn.equals("human") && humanPickCount != 2 && (! contains(matchedItems, gameArray[position]))
                        && !(humanPickCount == 1 && position == humanPicks[0])) {

                    playSound("click");
                    addToComputerMemory(position, gameArray[position]);

                    int drawableId = getDrawableIdForItem(position);
                    //Log.d("identifier", "drawableId: " + drawableId);
                    humanPickCount++;
                    if(humanPickCount == 1) {
                        imageView1 = (ImageView) imgView;
                        imageView1.setImageResource(drawableId);
                    } else {
                        imageView2 = (ImageView) imgView;
                        imageView2.setImageResource(drawableId);
                    }

                    humanPicks[humanPickCount-1] = position;
                    //Log.d("playerselectioncount", "playerselectioncount: " + humanPickCount);
                    //Log.d("playerselection1", "playerselection1: " + humanPicks[0]);
                    if(humanPickCount == 2) {
                        boolean isMatch = checkForMatch(humanPicks[0], humanPicks[1]);
                        if(isMatch) {
                            playSound("bell");
                            resetSelectionCount();
                        } else {
                            noMatch();
                        }
                    }
                }
            }
        });
    }



    private void setupGame()
    {
        int i;
        for (i=0; i<10; i++) {
            gameArray[i] = i+1;
        }
        for (i=10; i<20; i++) {
            gameArray[i] = i-9;
        }
        //Log.d("this is my array", "gameArray: " + Arrays.toString(gameArray));
        ShuffleArray(gameArray);
        //Log.d("this is my array", "shuffled: " + Arrays.toString(gameArray));

        //initialize scores to zero
        playerScores.put("human", 0);
        playerScores.put("computer", 0);

        playerTurn = "human";
        currentPlayer = (TextView) findViewById(R.id.currentPlayer);
        currentPlayer.setText(capitalizeWord(playerTurn));
    }

    private boolean checkForMatch(int pickOne, int pickTwo) {
        if(gameArray[pickOne] == gameArray[pickTwo]) {
            matchedItems[pickOne] = gameArray[pickOne];
            matchedItems[pickTwo] = gameArray[pickTwo];
            //Log.d("match!!!", "matchedItems: " + Arrays.toString(matchedItems));
            int currentScore = playerScores.get(playerTurn);
            playerScores.put(playerTurn, currentScore+1);
            updateScores();
            return true;
        }

        return false;
    }

    private void ShuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void resetSelectionCount() {
        humanPickCount = 0;
    }

    private void updateScores() {
        TextView humanScore = (TextView) findViewById(R.id.humanScore);
        humanScore.setText(String.valueOf(playerScores.get("human")));

        TextView computerScore = (TextView) findViewById(R.id.computerScore);
        computerScore.setText(String.valueOf(playerScores.get("computer")));

        if(playerScores.get("human") + playerScores.get("computer") == 10) {
            gameOver = true;
            gameOver();
        }
    }

    private String capitalizeWord(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void gameOver() {
        String gameWinner;
        if(playerScores.get("human").equals(playerScores.get("computer"))) {
            gameWinner = "It's a tie!";
        } else {
            String winner = (playerScores.get("human") > playerScores.get("computer") ? "Human" : "Computer");
            gameWinner = winner + " wins!";
        }

        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.textview_dialog_title);
        tvTitle.setText(R.string.game_over);

        TextView tvText = (TextView) dialog.findViewById(R.id.textview_dialog_text);
        tvText.setText(gameWinner);

        Button buttonDialogYes = (Button) dialog.findViewById(R.id.button_dialog_yes);
        buttonDialogYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(((Dialog) dialog).getContext(), MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        Button buttonDialogNo = (Button) dialog.findViewById(R.id.button_dialog_no);
        buttonDialogNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

/*        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(R.string.game_over);
        dialogBuilder.setMessage(gameWinner);
        dialogBuilder.setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(((Dialog) dialog).getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();*/
    }

    private void computerTurn() {
        // check for any known matches
        rememberMatch = checkForKnownMatches();
        // get first pick
        getComputerFirstPick();
    }

    private void getComputerFirstPick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!rememberMatch) {
                            final Random rand = new Random();
                            do {
                                computerFirstPick = rand.nextInt(gameArray.length);
                            } while (contains(matchedItems, gameArray[computerFirstPick]));
                            addToComputerMemory(computerFirstPick, gameArray[computerFirstPick]);
                        }
                        //Log.d("computer first pick", "first pick: "+computerFirstPick);
                        int drawableId = getDrawableIdForItem(computerFirstPick);
                        playSound("click");
                        imageView1 = (ImageView) gameGrid.getChildAt(computerFirstPick);
                        imageView1.setImageResource(drawableId);

                        // check for this item in memory
                        final Random random = new Random();
                        int remember = random.nextInt(5);
                        if(remember < skillLevel) {
                            // attempt to pull second pick from memory
                            for (int pickTwo = 0; pickTwo < computeryMemory.length; pickTwo++) {
                                if(pickTwo != computerFirstPick &&
                                        computeryMemory[pickTwo] == gameArray[computerFirstPick]) {
                                    computerSecondPick = pickTwo;
                                    rememberMatch = true;
                                }
                            }
                        }

                        getComputerSecondPick();
                    }
                }, 2000);
            }
        });
    }

    private void getComputerSecondPick() {
        // second pick
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!rememberMatch) {
                            final Random rand = new Random();
                            do {
                                computerSecondPick = rand.nextInt(gameArray.length);
                            }
                            while ((computerSecondPick == computerFirstPick) || contains(matchedItems, gameArray[computerSecondPick]));
                            addToComputerMemory(computerSecondPick, gameArray[computerSecondPick]);
                        }
                        //Log.d("computer second pick", "second pick: " + computerSecondPick);

                        int drawableId = getDrawableIdForItem(computerSecondPick);
                        playSound("click");
                        imageView2 = (ImageView) gameGrid.getChildAt(computerSecondPick);
                        imageView2.setImageResource(drawableId);

                        checkForComputerMatch();
                    }
                }, 2000);
            }
        });
    }

    private void checkForComputerMatch() {
        boolean isMatch = checkForMatch(computerFirstPick, computerSecondPick);

        if(isMatch) {
            playSound("bell");
            resetSelectionCount();
            if(!gameOver) {
                computerTurn();
            }
        } else {
            noMatch();
        }
    }



    public static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }

    private void playSound(String sound) {
        int soundId = getResources().getIdentifier(sound, "raw", getPackageName());
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), soundId);
        int maxVolume = 50;
        float log1=(float)(Math.log(maxVolume-40)/Math.log(maxVolume));
        mp.setVolume(1-log1,1-log1);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mMediaPlayer) {
                mMediaPlayer.release();
            }
        });
    }

    private void noMatch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int drawableId = getResources().getIdentifier("cover", "drawable", getPackageName());
                        imageView1.setImageResource(drawableId);
                        imageView2.setImageResource(drawableId);
                        playerTurn = (playerTurn.equals("computer") ? "human" : "computer");
                        currentPlayer.setText(capitalizeWord(playerTurn));
                        resetSelectionCount();
                        if(playerTurn.equals("computer")) {
                            computerTurn();
                        }
                    }
                }, 3000);
            }
        });

    }

    private boolean checkForKnownMatches() {
        boolean foundKnownMatch = false;
        Random random = new Random();
        int remember = random.nextInt(5);
        if(remember < skillLevel) {
            // parse memory to look for any matches
            for (int pickOne = 0; pickOne < computeryMemory.length; pickOne++) {
                if(computeryMemory[pickOne] != 0 && (! contains(matchedItems, computeryMemory[pickOne]))) {
                    for (int pickTwo = 0; pickTwo < computeryMemory.length; pickTwo++) {
                        if((computeryMemory[pickTwo] != 0) && (pickOne != pickTwo) && (computeryMemory[pickOne] == computeryMemory[pickTwo])) {
                            computerFirstPick = pickOne;
                            computerSecondPick = pickTwo;
                            //Log.d("match from memory", "match from memory");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void addToComputerMemory(int position, int value) {
        computeryMemory[position] = value;
    }

    private int getDrawableIdForItem(int position) {
        String image = imageArray[gameArray[position]-1];
        return getResources().getIdentifier(image, "drawable", getPackageName());
    }




}

