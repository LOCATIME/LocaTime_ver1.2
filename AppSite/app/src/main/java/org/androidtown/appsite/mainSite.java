package org.androidtown.appsite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by K on 2015-11-02.
 */
public class mainSite extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView topTitle = (TextView) findViewById(R.id.topTitle);
        topTitle.setText(R.string.main_name);

        ImageButton top_arrow_left = (ImageButton) findViewById(R.id.top_arrow_left);

        top_arrow_left.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ImageButton top_arrow_right = (ImageButton) findViewById(R.id.top_arrow_right);
        top_arrow_right.setVisibility(View.INVISIBLE);

        mainTabListenerOn();
        mainContentToggle(findViewById(R.id.mainMenu1));

    }

    public void mainContentToggle(View v){
        findViewById(R.id.main_tab1).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_tab2).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_tab3).setVisibility(View.INVISIBLE);

        if(v.getId() == R.id.mainMenu2){
            findViewById(R.id.main_tab2).setVisibility(View.VISIBLE);
        }else if(v.getId() == R.id.mainMenu3){
            findViewById(R.id.main_tab3).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.main_tab1).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.mainMenu1).setBackgroundResource(R.drawable.main_menu1);
        findViewById(R.id.mainMenu2).setBackgroundResource(R.drawable.main_menu2);
        findViewById(R.id.mainMenu3).setBackgroundResource(R.drawable.main_menu3);

        if(v.getId() == R.id.mainMenu2){
            v.setBackgroundResource(R.drawable.main_menu2_on);
        }else if(v.getId() == R.id.mainMenu3){
            v.setBackgroundResource(R.drawable.main_menu3_on);
        }else {
            v.setBackgroundResource(R.drawable.main_menu1_on);
        }

    }

    public void mainTabListenerOn(){
        findViewById(R.id.mainMenu1).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });

        findViewById(R.id.mainMenu2).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });

        findViewById(R.id.mainMenu3).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });


    }

}


