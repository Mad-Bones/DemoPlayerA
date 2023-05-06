package com.bsn.DemoPlayerA;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bsn.DemoPlayerA.tools.SimpleMusicPlayer;
import com.bsn.DemoPlayerA.tools.ViewButtons;
import com.bsn.DemoPlayerA.tools.ViewText;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        //Set text on the screen
        new ViewText.Set(this,
                getString(R.string.title_app),
                getString(R.string.description_app)).execute();
        //Set title and the button screen
        new ViewButtons.Set(this,getString(R.string.buttons_title_text)).execute();
        //Activate the SimpleMusicPlayer
        new SimpleMusicPlayer.Create(this).execute();


    }
}