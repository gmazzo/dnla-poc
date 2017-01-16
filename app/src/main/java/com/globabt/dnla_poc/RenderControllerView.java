package com.globabt.dnla_poc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * Created by yamil.marques on 16/01/2017.
 */

public class RenderControllerView extends LinearLayout {

    private LinearLayout baseLayout;
    private ImageButton ibBack,ibPlay,ibNext;
    private SeekBar seekBar;

    public RenderControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        inflate(context,R.layout.render_controller_layout,this);

        baseLayout = (LinearLayout) findViewById(R.id.master_layout);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibPlay = (ImageButton) findViewById(R.id.ib_play);
        ibNext = (ImageButton) findViewById(R.id.ib_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //TODO
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO
            }
        });
    }
}
