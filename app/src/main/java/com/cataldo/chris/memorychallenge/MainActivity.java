package com.cataldo.chris.memorychallenge;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        if (button1 != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound("click");
                    launchGame(1);
                }
            });
        }

        Button button2 = (Button) findViewById(R.id.button2);
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound("click");
                    launchGame(2);
                }
            });
        }

        Button button3 = (Button) findViewById(R.id.button3);
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound("click");
                    launchGame(3);
                }
            });
        }

        Button button4 = (Button) findViewById(R.id.button4);
        if (button4 != null) {
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound("click");
                    launchGame(4);
                }
            });
        }

        Button button5 = (Button) findViewById(R.id.button5);
        if (button5 != null) {
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound("click");
                    launchGame(5);
                }
            });
        }
    }


    private void launchGame(int skillLevel) {
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("skillLevel", skillLevel);
        startActivity(i);
        // close this activity
        finish();
    }

    private void playSound(String sound) {
        int soundId = getResources().getIdentifier(sound, "raw", getPackageName());
        mp = MediaPlayer.create(getApplicationContext(), soundId);
        int maxVolume = 50;
        float log1=(float)(Math.log(maxVolume-40)/Math.log(maxVolume));
        mp.setVolume(1-log1,1-log1);
        mp.start();
    }

}
