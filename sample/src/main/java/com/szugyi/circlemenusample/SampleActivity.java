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
import android.util.TypedValue;
import android.view.LayoutInflater;
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
        OnItemClickListener, OnRotationFinishedListener, OnCenterClickListener, SpringListener {
    public static final String ARG_LAYOUT = "layout";

    private final List<ImageView> mImageViews = new ArrayList<ImageView>();
    private final List<Point> mPositions = new ArrayList<Point>();
    private final SpringChain mSpringChain = SpringChain.create();
    private final Spring mSpring = SpringSystem
            .create()
            .createSpring()
            .addListener(this)
            .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(40, 6));

    private int mActiveIndex;
    private int mPadding;


    protected CircleLayout circleLayout;
    protected TextView selectedTextView;

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
        LayoutInflater popupInflater = (LayoutInflater)this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = popupInflater.inflate(R.layout.displayfullglifpage, null);

        int popupWidth = 1000;
        int popupHeight = (int)(popupWidth * 1.2f);
        PopupWindow popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth(popupWidth);
        popupWindow.setHeight(popupHeight);

        popupWindow.showAsDropDown(view, 0, -(20 / 2));
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        selectedTextView.setText(name);

        // Create the View.
        final ImageView imageView = new ImageView(this);
        mImageViews.add(imageView);
        //addView(imageView);
        imageView.setAlpha(0f);
        imageView.setBackgroundColor(Util.randomColor());
        imageView.setLayerType(2, null);

        // Add an image for each view.
        int res = getResources().getIdentifier("d" + (1), "drawable", this.getPackageName());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(res);


        selectedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int endValue = mSpring.getEndValue() == 0 ? 1 : 0;
                popupView.bringToFront();
                mActiveIndex = 0;
                mSpring.setEndValue(endValue);
            }
        });

        // Add a spring to the SpringChain to do an entry animation.
        mSpringChain.addSpring(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                render();
            }
        });

        // Wait for layout.
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout();
                /*getViewTreeObserver().removeOnGlobalLayoutListener(this);

                postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringChain.setControlSpringIndex(0).getControlSpring().setEndValue(1);
                    }
                }, 500);*/
            }
        });


        switch (view.getId()) {
            case R.id.main_calendar_image:
                // Handle calendar selection
                break;
            case R.id.main_cloud_image:
                // Handle cloud selection
                break;
            case R.id.main_key_image:
                // Handle key selection
                break;
            case R.id.main_mail_image:
                // Handle mail selection
                break;
            case R.id.main_profile_image:
                // Handle profile selection
                break;
            case R.id.main_tap_image:
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

                break;
            case R.id.main_cloud_image:
                // Handle cloud click
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



    private void render() {
        for (int i = 0; i < mImageViews.size(); i++) {
            ImageView imageView = mImageViews.get(i);
            if (mSpring.isAtRest() && mSpring.getCurrentValue() == 0) {
                // Performing the initial entry transition animation.
                Spring spring = mSpringChain.getAllSprings().get(i);
                float val = (float) spring.getCurrentValue();
                imageView.setScaleX(val);
                imageView.setScaleY(val);
                imageView.setAlpha(val);
                Point pos = mPositions.get(i);
                imageView.setTranslationX(pos.x);
                imageView.setTranslationY(pos.y);
            } else {
                // Scaling up a photo to fullscreen size.
                Point pos = mPositions.get(i);
                if (i == mActiveIndex) {
                    float ww = imageView.getWidth();
                    float hh = imageView.getHeight();
                    float sx = 1200 / ww;
                    float sy = 1900 / hh;
                    float s = sx > sy ? sx : sy;
                    float xlatX = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, pos.x, 0);
                    float xlatY = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, pos.y, 0);
                    imageView.setPivotX(0);
                    imageView.setPivotY(0);
                    imageView.setTranslationX(xlatX);
                    imageView.setTranslationY(xlatY);

                    float ss = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, 1, s);
                    imageView.setScaleX(ss);
                    imageView.setScaleY(ss);
                } else {
                    float val = (float) Math.max(0, 1 - mSpring.getCurrentValue());
                    imageView.setAlpha(val);
                }
            }
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

    @Override
    public void onSpringUpdate(Spring spring) {

    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }


}
