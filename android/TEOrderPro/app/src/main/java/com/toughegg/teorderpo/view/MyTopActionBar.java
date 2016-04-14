package com.toughegg.teorderpo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.toughegg.teorderpo.R;

/**
 * Created by toughegg on 15/8/5.
 */
public class MyTopActionBar extends RelativeLayout {

    private LayoutInflater mLayoutInflater;// 用于引入布局
    private ViewGroup mViewGroup;// 接收布局

    private TextView mTitleTextView;// 标题TextView
    private Spinner mTitleSpinner;// 标题下拉列表
    private RelativeLayout mLeftRelativeLayout;// 左边布局
    private TextView mLeftTextView;// 左边TextView
    private ImageView mLeftImageView;// 左边ImageView
    private RelativeLayout mRightRelativeLayout;// 右边布局
    private TextView mRightTextView;// 右边TextView
    private ImageView mRightImageView;// 右边ImageView

    private EditText mEditText;
    private Context mContext;

    private OnTopActionBarClickListener mOnTopActionBarClickListener;
    private OnTopActionBarTitleItemListener mOnTopActionBarTitleItemListener;

    public MyTopActionBar(Context context) {
        super(context);
        init(context);
    }

    public MyTopActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTopActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) mLayoutInflater.inflate(R.layout.view_add_actionbar_top, null);
        mTitleTextView = (TextView) mViewGroup.findViewById(R.id.actionbar_top_center_title_textView);
        mTitleSpinner = (Spinner) mViewGroup.findViewById(R.id.actionbar_top_center_title_spinner);
        mTitleSpinner.setOnItemSelectedListener(mItemListener);
        mLeftRelativeLayout = (RelativeLayout) mViewGroup.findViewById(R.id.actionbar_top_left_layout);
        mLeftTextView = (TextView) mViewGroup.findViewById(R.id.actionbar_top_left_textView);
        mLeftImageView = (ImageView) mViewGroup.findViewById(R.id.actionbar_top_left_imageView);
        mRightRelativeLayout = (RelativeLayout) mViewGroup.findViewById(R.id.actionbar_top_right_layout);
        mRightTextView = (TextView) mViewGroup.findViewById(R.id.actionbar_top_right_textView);
        mRightImageView = (ImageView) mViewGroup.findViewById(R.id.actionbar_top_right_imageView);
        mEditText = (EditText) mViewGroup.findViewById(R.id.find_edit);
        mLeftRelativeLayout.setOnClickListener(mClickListener);
        mRightRelativeLayout.setOnClickListener(mClickListener);
        addView(mViewGroup);
    }

    /**
     * 设置标题
     *
     * @param stringResId
     */
    public void setTitleTextView(int stringResId) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setText(stringResId);
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setTitleTextView(String text) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setText(text);
    }

    public void setTitleSpinnerAdapter(ArrayAdapter adapter, int selection) {
        mTitleSpinner.setVisibility(View.VISIBLE);
        //设置下拉列表的风格
        mTitleSpinner.setAdapter(adapter);
        mTitleSpinner.setSelection(selection);
    }

    /**
     * 设置左边文字
     *
     * @param stringResId
     */
    public void setLeftTextView(int stringResId) {
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText(stringResId);
    }

    /**
     * 设置左边文字
     *
     * @param text
     */
    public void setLeftTextView(String text) {
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText(text);
    }

    /**
     * 设置左边可否点击
     * @param isCanClick
     */
    public void setLeftClick(boolean isCanClick) {
        mLeftRelativeLayout.setClickable(isCanClick);
    }

    /**
     * 设置左边图片
     *
     * @param imageRsdId
     */
    public void setLeftImageView(int imageRsdId) {
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setBackgroundResource(imageRsdId);
    }

    /**
     * 设置右边文字
     *
     * @param stringResId
     */
    public void setRightTextView(int stringResId) {
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(stringResId);
    }

    /**
     * 设置右边文字
     *
     * @param text
     */
    public void setRightTextView(String text) {
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(text);
    }

    /**
     * 设置右边图片
     *
     * @param imageRsdId
     */
    public void setRightImageView(int imageRsdId) {
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setBackgroundResource(imageRsdId);
    }

    /**
     * 设置右边是否可以点击
     *
     * @param isCanClick
     */
    public void setRightClick(boolean isCanClick) {
        mRightRelativeLayout.setClickable(isCanClick);
    }


    /**
     * 左右点击事件回调
     *
     * @param onTopActionBarClickListener
     */
    public void setOnTopActionBarClickListener(OnTopActionBarClickListener onTopActionBarClickListener) {
        mOnTopActionBarClickListener = onTopActionBarClickListener;
    }

    /**
     * 下拉列表回调
     *
     * @param onTopActionBarTitleItemListener
     */
    public void setOnTopActionBarTitleItemListener(OnTopActionBarTitleItemListener onTopActionBarTitleItemListener) {
        mOnTopActionBarTitleItemListener = onTopActionBarTitleItemListener;
    }

    /**
     * 左右按钮点击接口
     */
    public static abstract interface OnTopActionBarClickListener {
        public abstract void onTopActionBarLeftClicked();

        public abstract void onTopActionBarRightClicked();
    }

    /**
     * 标题下拉列表接口
     */
    public static abstract interface OnTopActionBarTitleItemListener {
        public abstract void onTopActionBarTitleItemSelected(int position);
    }


    /**
     * animation
     */
    public void setAnimation() {
        mEditText.setVisibility(VISIBLE);
        Animation translateAnimation = new TranslateAnimation(0.1f, 100, 0.1f, .01f);
//初始化 Alpha动画
//        AlphaAnimation  alphaAnimation = new AlphaAnimation(0.1f, 1.0f);

//动画集
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
//        set.addAnimation(alphaAnimation);
//设置动画时间 (作用到每个动画)
        mEditText.startAnimation(translateAnimation);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnTopActionBarClickListener != null) {
                switch (v.getId()) {
                    case R.id.actionbar_top_left_layout:
                        mOnTopActionBarClickListener.onTopActionBarLeftClicked();
                        break;
                    case R.id.actionbar_top_right_layout:
                        mOnTopActionBarClickListener.onTopActionBarRightClicked();
                        break;
                }
            }
        }
    };

    private OnItemSelectedListener mItemListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mOnTopActionBarTitleItemListener.onTopActionBarTitleItemSelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
