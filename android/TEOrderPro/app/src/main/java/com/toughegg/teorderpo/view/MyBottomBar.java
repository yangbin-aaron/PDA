package com.toughegg.teorderpo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.teorderpo.R;


/**
 * Created by linda on 15/12/8.
 * email: 1564645246@qq.com
 */
public class MyBottomBar extends RelativeLayout {
    private TextView numberText;//数量
    private ImageView leftImage;//左边图标  碗
    private TextView leftTotalText;//左边总计文字
    private TextView leftPrice;//左边价格显示
    private TextView middleText;//中间文字
    private TextView rightText;//右边文字

    private View middleLine;//中间横线
    private RelativeLayout left_layout;//左边布局
    private RelativeLayout middleLayout;//中间布局
    private LinearLayout right_layout;//右边布局

    private LayoutInflater layoutInflater;
    private View myBottomBar;
    private OnBottomBarListener onBottomBarListener;//自定义点击事件监听


    public MyBottomBar (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context);
    }

    public MyBottomBar (Context context, AttributeSet attrs) {
        this (context, attrs, 0);
        init (context);
    }

    public MyBottomBar (Context context) {
        this (context, null, 0);
    }

    private void init (Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        myBottomBar = layoutInflater.inflate (R.layout.view_add_bottombar, null);
        left_layout = (RelativeLayout) myBottomBar.findViewById (R.id.left_layout);
        left_layout.setOnClickListener (myOnClickListener);
        numberText = (TextView) myBottomBar.findViewById (R.id.number);
        leftImage = (ImageView) myBottomBar.findViewById (R.id.left_img);
        leftTotalText = (TextView) myBottomBar.findViewById (R.id.left_total_text);
        leftPrice = (TextView) myBottomBar.findViewById (R.id.left_price);
        middleLayout = (RelativeLayout) findViewById (R.id.middle_layout);
        middleLine = (View) findViewById (R.id.middle_line);
        middleText = (TextView) myBottomBar.findViewById (R.id.middle_text);
        rightText = (TextView) myBottomBar.findViewById (R.id.right_text);
        right_layout = (LinearLayout) myBottomBar.findViewById (R.id.right_layout);
        right_layout.setOnClickListener (myOnClickListener);
        this.addView (myBottomBar);

    }

    /**
     * 是否显示左边的数量
     *
     * @param flag   是否显示
     * @param number 显示的数字
     */
    public void setNumber (Boolean flag, int number) {
        if (flag) {
            if (number > 0) {
                numberText.setVisibility (VISIBLE);
                numberText.setText (number + "");
            } else {
                numberText.setVisibility (INVISIBLE);
            }
        } else {
            numberText.setVisibility (INVISIBLE);
        }
    }

    /**
     * 是否显示左边图标  碗
     *
     * @param flag
     */
    public void showLeftImg (Boolean flag) {
        if (flag) {
            leftImage.setVisibility (VISIBLE);
        } else {
            leftImage.setVisibility (INVISIBLE);
        }
    }

    /**
     * 设置左边文字  （总价）
     *
     * @param flag
     */
    public void setLeftText (Boolean flag) {
        if (flag) {
            leftTotalText.setVisibility (VISIBLE);
        } else {
            leftTotalText.setVisibility (INVISIBLE);
        }
    }

    /**
     * 设置左边价格
     *
     * @param flag
     * @param price
     */
    public void setLeftPrice (Boolean flag, String price) {
        if (flag) {
            leftPrice.setVisibility (VISIBLE);
            leftPrice.setText (price);
        } else {
            leftPrice.setVisibility (INVISIBLE);
        }
    }

    /**
     * 是否显示中间布局
     *
     * @param flag
     */
    public void showMiddleLayout (Boolean flag) {
        if (flag) {
            middleLayout.setVisibility (View.VISIBLE);
            middleLine.setVisibility (VISIBLE);
        } else {
            middleLayout.setVisibility (View.GONE);
            middleLine.setVisibility (INVISIBLE);
            middleText.setVisibility (INVISIBLE);
        }
    }

    /**
     * 设置中间文字
     *
     * @param flag
     * @param string
     */
    public void setMiddleText (Boolean flag, String string) {
        if (flag) {
            middleText.setVisibility (VISIBLE);
            middleText.setText (string);
        } else {
            middleText.setVisibility (INVISIBLE);
        }
    }

    /**
     * 设置中右边文字
     *
     * @param flag
     * @param string
     */
    public void setRightText (Boolean flag, int string) {
        if (flag) {
            rightText.setVisibility (VISIBLE);
            rightText.setText (string);
        } else {
            rightText.setVisibility (INVISIBLE);
        }
    }

    //点击事件回调
    public void setOnBottomBarListener (OnBottomBarListener listener) {
        onBottomBarListener = listener;
    }

    public static abstract interface OnBottomBarListener {
        public abstract void onBottomBarLeftListener ();

        public abstract void onBottomBarRightListener ();
    }

    private OnClickListener myOnClickListener = new OnClickListener () {
        @Override
        public void onClick (View v) {
            if (onBottomBarListener != null) {
                if (v == right_layout) {
                    //右边点击事件处理
                    onBottomBarListener.onBottomBarRightListener ();
                    return;
                }
                if (v == left_layout) {
                    onBottomBarListener.onBottomBarLeftListener ();
                    return;
                }
            }
        }
    };
}
