package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.fliter.PhotoProcessing;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;

    public class TuneListFragment extends BaseEditFragment implements View.OnClickListener {
        public static final int INDEX = 7;
        public static final String TAG = TuneListFragment.class.getName();
        private View mainView;
        private View backBtn;
        public static int MODE = -1;

        private Bitmap fliterBit;
        public static final int BRIGHTNESS = 0;
        public static final int CONTRAST = 1;
        public static final int HUE = 2;
        public static final int SATURATION = 3;
        public static final int TEMPERATURE = 4;
        public static final int VIGNETTE = 5;
        public static final int SHARPNESS = 6;
        public static final int BLUR = 7;
        public static final int TINT = 8;

        private Bitmap currentBitmap;
        private LinearLayout mTuneGroup;
        private String[] tuningOptions;

        public static TuneListFragment newInstance() {
            TuneListFragment fragment = new TuneListFragment();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mainView = inflater.inflate(R.layout.fragment_edit_image_tune, null);
            return mainView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            backBtn = mainView.findViewById(R.id.back_to_main);
            mTuneGroup = (LinearLayout) mainView.findViewById(R.id.tune_group);

            backBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToMain();
                }
            });
            setUpTuningOptions();
        }

        @Override
       public  void onShow() {
            activity.mode = EditImageActivity.MODE_NONE;
            activity.mainImage.setImageBitmap(activity.mainBitmap);
            activity.mTuneListFragment.setCurrentBitmap(activity.mainBitmap);
            activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            activity.mainImage.setScaleEnabled(false);
            activity.bannerFlipper.setVisibility(View.INVISIBLE);
        }

        public void backToMain() {
            fliterBit = null;
            activity.changeMainBitmap(getCurrentBitmap());
            //activity.mainImage.setImageBitmap(getCurrentBitmap());// 返回原图
            activity.mode = EditImageActivity.MODE_NONE;
            activity.bottomGallery.setCurrentItem(0);
            activity.mainImage.setScaleEnabled(true);
        }


        private void setUpTuningOptions() {
            tuningOptions = getResources().getStringArray(R.array.tuneOptions);
            if (tuningOptions == null)
                return;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.leftMargin = 20;
            params.rightMargin = 20;
            mTuneGroup.removeAllViews();
            for (int i = 0, len = tuningOptions.length; i < len; i++) {
                TextView text = new TextView(activity);
                text.setTextColor(Color.WHITE);
                text.setTextSize(20);
                text.setText(tuningOptions[i]);
                mTuneGroup.addView(text, params);
                text.setTag(i);
                text.setOnClickListener(this);
            }
        }

        void setMODE(int mode) {
            MODE = mode;
        }


        public Bitmap getCurrentBitmap() {
            if (currentBitmap==null)
                setCurrentBitmap(activity.mainBitmap);
            return currentBitmap;
        }

        public void setCurrentBitmap(Bitmap currentBitmap) {
            this.currentBitmap = currentBitmap;
        }

        @Override
        public void onClick(View v) {
            switch ((Integer)v.getTag()){
                case BRIGHTNESS:
                    setMODE(BRIGHTNESS);
                    break;
                case CONTRAST:
                    setMODE(CONTRAST);
                    break;
                case HUE:
                    setMODE(HUE);
                    break;
                case SATURATION:
                    setMODE(SATURATION);
                    break;
                case TEMPERATURE:
                    setMODE(TEMPERATURE);
                    break;
                case VIGNETTE:
                    setMODE(VIGNETTE);
                    break;
                case SHARPNESS:
                    setMODE(SHARPNESS);
                    break;
                case BLUR:
                    setMODE(BLUR);
                    break;
                case TINT:
                    setMODE(TINT);
                    break;
            }
            activity.bottomGallery.setCurrentItem(TuningFragment.INDEX);
            activity.mTuningFragment.onShow();
        }
    }
