package com.bsn.DemoPlayerA.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bsn.DemoPlayerA.R;
import com.bsn.DemoPlayerA.adapters.PreferencesAdapter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SimpleMusicPlayer {


    @SuppressLint("StaticFieldLeak")
    public static class Create extends AsyncTask<Void, Void, Void> {
        Activity actividad;
        LinearLayout inf_container;
        ImageView boton_play,boton_pausa,boton_siguiente,boton_parar;
        TextView text_title,text_duration,text_current;
        SeekBar slider_repro;
        MediaPlayer musicPlayer;
        private final Handler uiHandler = new Handler();
        NumberFormat f = new DecimalFormat("00");
        public Create(Activity actividad) {
            this.actividad = actividad;
            this.inf_container = (actividad).findViewById(R.id.container_inf);

            LayoutInflater inflater = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") LinearLayout music_screen = inflater.inflate(R.layout.simple_player_component, null).findViewById(R.id.simple_player_a);

            this.boton_play = music_screen.findViewById(R.id.mediab_play);
            this.boton_pausa = music_screen.findViewById(R.id.mediab_pause);
            this.boton_siguiente = music_screen.findViewById(R.id.mediab_next);
            this.boton_parar = music_screen.findViewById(R.id.mediab_stop);
            this.slider_repro = music_screen.findViewById(R.id.slider_media);

            this.text_title = music_screen.findViewById(R.id.title_media_text);
            text_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            text_title.setSelected(true);
            text_title.setSingleLine(true);

            this.text_duration = music_screen.findViewById(R.id.time_media_total);
            this.text_current = music_screen.findViewById(R.id.time_media_current);

            inf_container.addView(music_screen);
            try {
                loadButtons();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private void loadButtons() throws IOException {
            readTrack();
            checkPlaying();
            boton_siguiente.setOnClickListener(view -> {
                try {
                    changeTrack();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        private void checkPlaying(){

            loadSlider();
            if(!musicPlayer.isPlaying()){
                new PreferencesAdapter(actividad).setisPlaying(false);
            }
            boolean playing = new PreferencesAdapter(actividad).isPlaying();
            if(playing){
                boton_play.setColorFilter(actividad.getColor(R.color.general_greendark));
                boton_pausa.setColorFilter(actividad.getColor(R.color.general_green));

                boton_pausa.setOnClickListener(view -> {
                    pauseMusic();
                });
                boton_play.setClickable(false);
            }else{
                boton_play.setColorFilter(actividad.getColor(R.color.general_green));
                boton_pausa.setColorFilter(actividad.getColor(R.color.general_greendark));

                boton_pausa.setClickable(false);
                boton_play.setOnClickListener(view -> {
                    playMusic();
                });
            }
            boton_parar.setOnClickListener(view -> {
               stopMusic();
            });
        }
        @SuppressLint("SetTextI18n")
        private void readTrack() throws IOException {
            int track = new PreferencesAdapter(actividad).trackNumber();
            if(track == 2){
                new PreferencesAdapter(actividad).setTrackNumber(2);
                musicPlayer = MediaPlayer.create(actividad, R.raw.segundo);
                text_title.setText(R.string.track_b);
            }else{
                new PreferencesAdapter(actividad).setTrackNumber(1);
                musicPlayer = MediaPlayer.create(actividad, R.raw.primero);
                text_title.setText(R.string.track_a);
            }
            //set duration in text
            long currentLongPosition = musicPlayer.getDuration();
            text_duration.setText(
                    f.format(((currentLongPosition / 60000) % 60)) + ":" +
                            f.format((currentLongPosition / 1000) % 60));

            //check current state
            checkPlaying();
        }
        @SuppressLint("SetTextI18n")
        private void changeTrack() throws IOException {
            pauseMusic();
            int track = new PreferencesAdapter(actividad).trackNumber();
            if(track == 2){
                new PreferencesAdapter(actividad).setTrackNumber(1);
                musicPlayer = MediaPlayer.create(actividad, R.raw.primero);
                text_title.setText(R.string.track_a);
            }else{
                new PreferencesAdapter(actividad).setTrackNumber(2);
                musicPlayer = MediaPlayer.create(actividad, R.raw.segundo);
                text_title.setText(R.string.track_b);
            }

            //set duration in text
            long currentLongPosition = musicPlayer.getDuration();
            text_duration.setText(
                    f.format(((currentLongPosition / 60000) % 60)) + ":" +
                            f.format((currentLongPosition / 1000) % 60));

            //set play
            playMusic();
        }

        public void playMusic(){
            new PreferencesAdapter(actividad).setisPlaying(true);
            musicPlayer.start();
            checkPlaying();
        }
        public void pauseMusic(){
            new PreferencesAdapter(actividad).setisPlaying(false);
            musicPlayer.pause();
            checkPlaying();
        }
        public void stopMusic(){
            new PreferencesAdapter(actividad).setisPlaying(false);
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
            try {
                readTrack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        @SuppressLint("SetTextI18n")
        private void loadSlider(){
            slider_repro.setMax(musicPlayer.getDuration()/1000);
            actividad.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer != null){
                        int currentPosition = musicPlayer.getCurrentPosition() / 1000;
                        slider_repro.setProgress(currentPosition);

                        long currentLongPosition = musicPlayer.getCurrentPosition();
                        NumberFormat f = new DecimalFormat("00");
                        text_current.setText(f.format(((currentLongPosition / 60000) % 60)) + ":" + f.format((currentLongPosition / 1000) % 60));
                    }
                    uiHandler.postDelayed(this, 1000);
                }
            });
            slider_repro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    long currentLongPosition = musicPlayer.getCurrentPosition();
                    NumberFormat f = new DecimalFormat("00");
                    text_current.setText(f.format(((currentLongPosition / 60000) % 60)) + ":" + f.format((currentLongPosition / 1000) % 60));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                    long currentLongPosition = musicPlayer.getCurrentPosition();
                    NumberFormat f = new DecimalFormat("00");
                    text_current.setText(f.format(((currentLongPosition / 60000) % 60)) + ":" + f.format((currentLongPosition / 1000) % 60));
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(musicPlayer != null && fromUser){
                        musicPlayer.seekTo(progress * 1000);

                        long currentLongPosition = musicPlayer.getCurrentPosition() / 1000;
                        NumberFormat f = new DecimalFormat("00");
                        text_current.setText(f.format(((currentLongPosition / 60000) % 60)) + ":" + f.format((currentLongPosition / 1000) % 60));
                    }
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(actividad.isDestroyed()){
                musicPlayer.release();
                musicPlayer = null;
            }
            return null;
        }
    }
}
