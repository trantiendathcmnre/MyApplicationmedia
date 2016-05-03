package com.tiendat.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Player extends AppCompatActivity implements View.OnClickListener {

    ArrayList<File> mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;

    static MediaPlayer mediaPlayer;


    SeekBar seekBar;
    ImageView btnPlay, btnNxt, btnPv,btnShuffle, btnRepeat;
    ImageView imglaucher;
    private boolean isShuffle = false;
    private boolean isRepeat = false;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btnRepeat = (ImageView) findViewById(R.id.btRepeat);
        btnShuffle = (ImageView) findViewById(R.id.btShuffle);
        btnPlay = (ImageView) findViewById(R.id.btPlay);
        btnNxt = (ImageView) findViewById(R.id.btNxt);
        btnPv = (ImageView) findViewById(R.id.btPv);

        btnRepeat.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNxt.setOnClickListener(this);
        btnPv.setOnClickListener(this);

        btnPlay = (ImageView) findViewById(R.id.btPlay);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.a_litter_love);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        //xoay ImageView
        imglaucher = (ImageView) findViewById(R.id.imageViewlaucher);
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(60000);
        rotateAnimation.setFillAfter(true);
        imglaucher.startAnimation(rotateAnimation);
        rotateAnimation.setRepeatCount ( 10000 );


        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };



        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }


        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        int position = b.getInt("pos",0);

        u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        updateSeekBar.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if(mediaPlayer.isPlaying()){
                    btnPlay.setImageResource(R.drawable.ic_player_pause);
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setImageResource(R.drawable.ic_player_play);
                    mediaPlayer.start();
                }
                break;
            case R.id.btNxt:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1);
                u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
            case R.id.btPv:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ( position-1<0)? mySongs.size()-1: position-1;
                u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
            /*case R.id.btRepeat:
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.ic_player_repeat_off);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    btnRepeat.setImageResource(R.drawable.ic_player_repeat_all);
                    btnShuffle.setImageResource(R.drawable.ic_player_shuffle_off);
                }
                break;
            case R.id.btShuffle:
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.ic_player_shuffle_off);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.ic_player_shuffle);
                    btnRepeat.setImageResource(R.drawable.ic_player_repeat_off);
                }
                break;*/

        }
    }

}
