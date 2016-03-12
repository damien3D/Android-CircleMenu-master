package com.szugyi.circlemenusample;

/*
 * Copyright 2013 Csaba Szugyiczki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout;
import com.szugyi.circlemenu.view.CircleLayout.OnCenterClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemSelectedListener;
import com.szugyi.circlemenu.view.CircleLayout.OnRotationFinishedListener;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import java.util.ArrayList;
import java.util.List;


public class SampleActivity extends AppCompatActivity implements OnItemSelectedListener,
        OnItemClickListener, OnRotationFinishedListener, OnCenterClickListener {
    public static final String ARG_LAYOUT = "layout";

    private final List<ImageView> mImageViews = new ArrayList<ImageView>();
    private final List<Point> mPositions = new ArrayList<Point>();

    private int mActiveIndex;
    private int mPadding;


    protected CircleLayout circleLayout;
    protected TextView selectedTextView;

    private PopupWindow popupwindow;
    private  LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view by passed extra
        Bundle extras = getIntent().getExtras();
        int layoutId = extras.getInt(ARG_LAYOUT);
        setContentView(layoutId);

        // Set listeners
        circleLayout = (CircleLayout) findViewById(R.id.circle_layout);
        circleLayout.setOnItemSelectedListener(this);
        circleLayout.setOnItemClickListener(this);
        circleLayout.setOnRotationFinishedListener(this);
        circleLayout.setOnCenterClickListener(this);

        selectedTextView = (TextView) findViewById(R.id.selected_textView);

        String name = null;
        View view = circleLayout.getSelectedItem();
        if (view instanceof CircleImageView) {
            name = ((CircleImageView) view).getName();
        }
        selectedTextView.setText(name);
    }

    @Override
    public void onItemSelected(View view) {
        final String name;
        if (view instanceof CircleImageView) {
            name = ((CircleImageView) view).getName();
        } else {
            name = null;
        }


        switch (view.getId()) {
            case R.id.main_calendar_image:
                // Handle calendar selection
                Log.v("TEST", "main_calendar_image");

                RelativeLayout relativeLayout;
                relativeLayout = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.examplelayout, null);

                popupwindow = new PopupWindow(container, 400, 400, true);
                popupwindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 500,500);

                container.setOnTouchListener(new View.OnTouchListener () {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });

                break;

            case R.id.main_cloud_image:
                // Handle cloud selection
                Log.v("TEST", "main_cloud_image");

                RelativeLayout relativeLayoutCloud;
                relativeLayoutCloud = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containerCloud = (ViewGroup) layoutInflater.inflate(R.layout.examplelayoutcloud, null);

                popupwindow = new PopupWindow(containerCloud, 400, 400, true);
                popupwindow.showAtLocation(relativeLayoutCloud, Gravity.NO_GRAVITY, 500,500);

                containerCloud.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });

                break;
            case R.id.main_key_image:
                // Handle key selection

/*
                RelativeLayout relativeLayoutkey;
                relativeLayoutkey = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containerkey = (ViewGroup) layoutInflater.inflate(R.layout.examplelayouttag, null);

                popupwindow = new PopupWindow(containerkey, 400, 400, true);
                popupwindow.showAtLocation(relativeLayoutkey, Gravity.NO_GRAVITY, 500,500);

                containerkey.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });
*/

                Log.v("TEST", "main_key_image");
                break;
            case R.id.main_mail_image:
                Log.v("TEST", "main_mail_image");

/*

                RelativeLayout relativeLayoutmail;
                relativeLayoutmail = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containermail = (ViewGroup) layoutInflater.inflate(R.layout.examplelayouttag, null);

                popupwindow = new PopupWindow(containermail, 400, 400, true);
                popupwindow.showAtLocation(relativeLayoutmail, Gravity.NO_GRAVITY, 500,500);

                containermail.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });

*/

                // Handle mail selection
                break;
            case R.id.main_profile_image:
                Log.v("TEST", "main_profile_image");

                /*

                RelativeLayout relativeLayoutprofile;
                relativeLayoutprofile = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containerprofile = (ViewGroup) layoutInflater.inflate(R.layout.examplelayouttag, null);

                popupwindow = new PopupWindow(containerprofile, 400, 400, true);
                popupwindow.showAtLocation(relativeLayoutprofile, Gravity.NO_GRAVITY, 500,500);

                containerprofile.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });

                */


                // Handle profile selection
                break;
            case R.id.main_tap_image:
                Log.v("TEST", "main_tap_image");

                /*

                RelativeLayout relativeLayouttap;
                relativeLayouttap = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containerTap = (ViewGroup) layoutInflater.inflate(R.layout.examplelayouttag, null);

                popupwindow = new PopupWindow(containerTap, 400, 400, true);
                popupwindow.showAtLocation(relativeLayouttap, Gravity.NO_GRAVITY, 500,500);

                containerTap.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });

                */

                // Handle tap selection
                break;
        }
    }

    @Override
    public void onItemClick(View view) {
        String name = null;
        if (view instanceof CircleImageView) {
            name = ((CircleImageView) view).getName();
        }

        String text = getResources().getString(R.string.start_app, name);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        switch (view.getId()) {
            case R.id.main_calendar_image:
               // LayoutInflater inflater = getLayoutInflater();
               // RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.displayfullglifpage, null);
              //  ((ViewGroup) view).addView(layout);
/*
                Log.v("Test2","main calendar image 2");

                RelativeLayout relativeLayoutCloud;
                relativeLayoutCloud = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup containerCloud = (ViewGroup) layoutInflater.inflate(R.layout.examplelayoutcloud, null);

                popupwindow = new PopupWindow(containerCloud, 400, 400, true);
                popupwindow.showAtLocation(relativeLayoutCloud, Gravity.NO_GRAVITY, 500,500);

                containerCloud.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });
*/


                break;
            case R.id.main_cloud_image:
                // Handle cloud click

/*
                RelativeLayout relativeLayout;
                relativeLayout = (RelativeLayout) findViewById(R.id.relative1);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.examplelayout, null);

                popupwindow = new PopupWindow(container, 400, 400, true);
                popupwindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 500,500);

                container.setOnTouchListener(new View.OnTouchListener () {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popupwindow.dismiss();

                        return true;
                    }
                });
*/


                break;
            case R.id.main_key_image:
                // Handle key click
                break;
            case R.id.main_mail_image:
                // Handle mail click
                break;
            case R.id.main_profile_image:
                // Handle profile click
                break;
            case R.id.main_tap_image:
                // Handle tap click
                break;
        }
    }




    private void layout() {
        float width = 1200;
        float height = 1900;

        // Determine the size for each image given the screen dimensions.
        Resources res = getResources();
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, res.getDisplayMetrics());
        int colWidth = (int) Math.ceil((width - 2 * mPadding) / 1) - 2 * mPadding;
        int rowHeight = (int) Math.ceil((height - 2 * mPadding) / 1) - 2 * mPadding;

        // Determine the resting position for each view.
        int k = 0;
        int py = 0;
        for (int i = 0; i < 1; i++) {
            int px = 0;
            py += mPadding * 2;
            for (int j = 0; j < 1; j++) {
                px += mPadding * 2;
                ImageView imageView = mImageViews.get(k);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(colWidth, rowHeight));
                mPositions.add(new Point(px, py));
                px += colWidth;
                k++;
            }
            py += rowHeight;
        }
    }


    @Override
    public void onRotationFinished(View view) {
        Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2, view.getHeight() / 2);
        animation.setDuration(250);
        view.startAnimation(animation);
    }

    @Override
    public void onCenterClick() {
        Toast.makeText(getApplicationContext(), R.string.center_click, Toast.LENGTH_SHORT).show();
    }

    public void onAddClick(View view) {
        CircleImageView newMenu = new CircleImageView(this);
        newMenu.setBackgroundResource(R.drawable.circle);
        newMenu.setImageResource(R.drawable.ic_voice);
        newMenu.setName(getString(R.string.voice_search));
        circleLayout.addView(newMenu);
    }

    public void onRemoveClick(View view) {
        if (circleLayout.getChildCount() > 0) {
            circleLayout.removeViewAt(circleLayout.getChildCount() - 1);
        }
    }


}
