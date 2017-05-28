package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.fliter.PhotoProcessing;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


public class TuningFragment extends BaseEditFragment implements OnClickListener {
    public static final int INDEX = 8;
    public static final String TAG = TuningFragment.class.getName();
    private View mainView;
    private View backToMenu;
    public SeekBar mSeekBar;
    private Bitmap currentBitmap;
    private Bitmap fliterBit;


    public static TuningFragment newInstance() {
        TuningFragment fragment = new TuningFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_rotate, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backToMenu = mainView.findViewById(R.id.back_to_main);
        mSeekBar = (SeekBar) mainView.findViewById(R.id.rotate_bar);

        backToMenu.setOnClickListener(new BackToMenuClick());
        mSeekBar.setOnSeekBarChangeListener(new SeekValueChange());
        mSeekBar.setProgress(50);
        mSeekBar.setMax(100);
        switch (TuneListFragment.MODE){
            case TuneListFragment.BRIGHTNESS:
                mSeekBar.setProgress(50);
                break;
        }

    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_TUNE;
        activity.mTuningFragment.setCurrentBitmap(activity.mainBitmap);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);
        activity.bannerFlipper.showNext();

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (position == 0) {
            activity.mainImage.setImageBitmap(activity.mainBitmap);
            currentBitmap = activity.mainBitmap;
            return;
        }

        TuneImage task = new TuneImage();
        task.execute(position);
    }

    private final class SeekValueChange implements OnSeekBarChangeListener {
        int counter = 0;
        @Override
        public void onProgressChanged(SeekBar seekBar, int angle,
                                      boolean fromUser) {
           if ((counter++)%8==0) {
               counter = 0;/*
               TuneImage task = new TuneImage();
               task.execute(seekBar.getProgress());*/
           }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            TuneImage task = new TuneImage();
            task.execute(seekBar.getProgress());
        }
    }

    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fliterBit != null && (!fliterBit.isRecycled())) {
            fliterBit.recycle();
        }
    }

    public void backToMain() {
        currentBitmap = activity.mainBitmap;
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(TuneListFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        activity.bannerFlipper.showPrevious();
    }


    public void applyFilterImage() {
        if (currentBitmap == activity.mainBitmap) {
            backToMain();
            return;
        } else {
            SaveImageTask saveTask = new SaveImageTask();
            saveTask.execute(fliterBit);
        }
    }

    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            //return saveBitmap(params[0], activity.saveFilePath);
            return true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result) {
                activity.changeMainBitmap(fliterBit);
                backToMain();
            }// end if
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = EditImageActivity.getLoadingDialog(getActivity(),
                    R.string.saving_image, false);
            dialog.show();
        }
    }// end inner class


    private final class TuneImage extends AsyncTask<Integer, Void, Bitmap> {
        private Bitmap srcBitmap;

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int val = params[0];
            if (srcBitmap != null && !srcBitmap.isRecycled()) {
                srcBitmap.recycle();
            }

            srcBitmap = Bitmap.createBitmap(activity.mainBitmap.copy(
                    Bitmap.Config.RGB_565, true));
            return PhotoProcessing.tunePhoto(srcBitmap, TuneListFragment.MODE,val);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
         }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result == null)
                return;
            if (fliterBit != null && (!fliterBit.isRecycled())) {
                fliterBit.recycle();
            }
            fliterBit = result;
            activity.mainImage.setImageBitmap(fliterBit);
            currentBitmap = fliterBit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }// end inner class

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }
}// end class
