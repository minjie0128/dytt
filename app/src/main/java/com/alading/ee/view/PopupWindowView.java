package com.alading.ee.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alading.ee.R;

public class PopupWindowView extends PopupWindow {

	private View mRegionSelectView;

	private Context mContext;
	private TextView mContentsView;

	private static final String TAG = "Alading-RegionSelectView";

	public PopupWindowView(Context context, OnClickListener listener) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRegionSelectView = inflater.inflate(R.layout.city_select_view, null);
		mRegionSelectView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = mRegionSelectView.findViewById(R.id.t_contents)
						.getTop();
				int bottom = mRegionSelectView.findViewById(R.id.t_contents)
						.getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height || y > bottom) {
						dismiss();
					}
				}
				return true;
			}
		});
		setContentView(mRegionSelectView);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
	}

	public PopupWindowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PopupWindowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setText(String contents) {
		mContentsView = (TextView) mRegionSelectView
				.findViewById(R.id.t_contents);
		mContentsView.setText(contents);
	}

}
