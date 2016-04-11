
package com.alading.ee.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alading.ee.R;
import com.alading.ee.fusion.FusionField;


public class AladingAlertDialog extends Dialog {

    private ImageView mEndTag;
    private LinearLayout mDownloatProgressLayout;
    private LinearLayout mButtonLayout;
    private TextView mTitleText;
    private TextView mContentsText;
    private TextView mPositiveText;
    private TextView mNegativeText;
    private TextView mDividerText;
    private TextView mTvProgress;
    private ProgressBar mPbProgress;
	private TextView mTvDownload;

    public static final int SIZE_SMALL = 1;
    public static final int SIZE_NORMAL = 2;
    public static final int SIZE_LARGE = 3;

    public AladingAlertDialog(Context context) {
        super(context, R.style.alading_dialog);
        setCustomView(SIZE_NORMAL, true,false);
    }

    public AladingAlertDialog(Context context, int size) {
        super(context, R.style.alading_dialog);
        setCustomView(size, true,false);
    }

    public AladingAlertDialog(Context context, int size, boolean animated) {
        super(context, R.style.alading_dialog);
        setCustomView(size, animated,false);
    }
    
    public AladingAlertDialog(Context context, int size, boolean animated, boolean isShowPreference) {
        super(context, R.style.alading_dialog);
        setCustomView(size, animated,isShowPreference);
    }

    private void setCustomView(int size, boolean animated, boolean isShowPreference) {
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.alading_alert_dialog,null);
        mEndTag = (ImageView) customView.findViewById(R.id.r_end_tag);
        mTitleText = (TextView) customView.findViewById(R.id.t_dialog_title);
        mContentsText = (TextView) customView.findViewById(R.id.t_dialog_content);
        mPositiveText = (TextView) customView.findViewById(R.id.t_positive_text);
        mNegativeText = (TextView) customView.findViewById(R.id.t_negative_text);
        mDividerText=(TextView) customView.findViewById(R.id.t_divider_text);
        mDownloatProgressLayout=(LinearLayout) customView.findViewById(R.id.t_downloat_progress_layout);
        mTvProgress=(TextView) customView.findViewById(R.id.t_tvProcess);
        mPbProgress=(ProgressBar) customView.findViewById(R.id.t_pbDownload);
        mTvDownload=(TextView) customView.findViewById(R.id.t_tvDownLoad);
        mButtonLayout=(LinearLayout) customView.findViewById(R.id.t_button_laytou);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (animated) {
            // getWindow().setWindowAnimations(R.style.AnimBottom);
        }
        
//        if(isShowPreference){
//            customView.findViewById(R.id.ch_save).setVisibility(View.VISIBLE);
//        }

        setCancelable(true);

        setCanceledOnTouchOutside(false);

        int width = (int) (FusionField.devicePixelsWidth - 15 * FusionField.deviceDensity);
        int height = 0;
        if (size == SIZE_NORMAL) {
            height = LayoutParams.WRAP_CONTENT;
        } else if (size == SIZE_LARGE) {
            height = (int) (FusionField.devicePixelsHeight - 100 * FusionField.deviceDensity);
        }

        super.setContentView(customView, new LayoutParams(width, height));
    }

    public AladingAlertDialog setOnPositiveListener(View.OnClickListener listener) {
        mPositiveText.setOnClickListener(listener);
        return this;
    }

    public AladingAlertDialog setOnNegativeListener(View.OnClickListener listener) {
        mNegativeText.setOnClickListener(listener);
        return this;
    }

    public AladingAlertDialog setTitleText(String title) {
        mTitleText.setText(title);

        return this;
    }

    public AladingAlertDialog setContentText(String content) {
        mContentsText.setText(content);
        return this;
    }

    public AladingAlertDialog setContentLayoutParam(LinearLayout.LayoutParams params) {
        mContentsText.setLayoutParams(params);
        return this;
    }
    public AladingAlertDialog setContentText(Spanned content) {
        mContentsText.setText(content);
        return this;
    }

    public AladingAlertDialog setPositiveText(String positive) {
        mPositiveText.setText(positive);
        return this;
    }

    public AladingAlertDialog setNegativeText(String negative) {
        mNegativeText.setText(negative);
        return this;
    }

    public AladingAlertDialog setDismissOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    
    public AladingAlertDialog setContentTextGravity(int gravity) {
        mContentsText.setGravity(gravity);
        return this;
    }

    public AladingAlertDialog hideNegative() {
        mNegativeText.setVisibility(View.GONE);
        mDividerText.setVisibility(View.VISIBLE);
        return this;
    }
    public AladingAlertDialog hidePositive() {
        mPositiveText.setVisibility(View.GONE);
        mDividerText.setVisibility(View.VISIBLE);
        return this;
    }
    public AladingAlertDialog hideButtonLyaout(){
    	mButtonLayout.setVisibility(View.GONE);
    	return this;
    }
    
    public AladingAlertDialog setAnimation(int anim) {
        getWindow().setWindowAnimations(anim);
        return this;
    }

    public AladingAlertDialog setShowEndTag(boolean show) {
        if (show) {
            mEndTag.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public AladingAlertDialog hideEndtag()
    {
    	mEndTag.setVisibility(View.GONE);
    	return this;
    }
    public AladingAlertDialog hideProgressLayout()
    {
    	mDownloatProgressLayout.setVisibility(View.GONE);
    	return this;
    }
    public AladingAlertDialog showProgressLayout()
    {
    	mDownloatProgressLayout.setVisibility(View.VISIBLE);
    	return this;
    }
    
    public AladingAlertDialog hideContentTv()
    {
    	mContentsText.setVisibility(View.GONE);
    	return this;
    }
    public AladingAlertDialog showContentTv()
    {
    	mContentsText.setVisibility(View.VISIBLE);
    	return this;
    }
    
    public AladingAlertDialog setProgress(int progress)
    {
    	mTvProgress.setText(progress+"%");
    	mPbProgress.setProgress(progress);
    	return this;
    }
    public AladingAlertDialog setDownLoadText(String s)
    {
    	mTvDownload.setText(s);
    	return this;
    }
    
    public AladingAlertDialog showDownLoadText()
    {
    	mTvDownload.setVisibility(View.VISIBLE);
    	return this;
    }
    
    public AladingAlertDialog hideDownLoadText()
    {
    	mTvDownload.setVisibility(View.GONE);
    	return this;
    }
}
