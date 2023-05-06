package com.bsn.DemoPlayerA.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsn.DemoPlayerA.R;
import com.bsn.DemoPlayerA.anim.text.Typewriter;

public class ViewText {

    @SuppressLint("StaticFieldLeak")
    public static class Set extends AsyncTask<Void, Void, Void> {
        Activity actividad;
        TextView text_title;
        Typewriter text_content;
        LinearLayout sup_container;

        public Set(Activity actividad,String title,String content) {
            this.actividad = actividad;

            this.sup_container = (actividad).findViewById(R.id.container_general);

            LayoutInflater inflater = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout content_text = inflater.inflate(R.layout.textfields_component, null).findViewById(R.id.text_comp);

            text_title = content_text.findViewById(R.id.text_title);
            text_content = content_text.findViewById(R.id.content_typed_text);
            text_title.setText(title);
            text_content.setCharacterDelay(50);
            text_content.animateText(content);

            //#1
            sup_container.addView(content_text);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

    }
}
