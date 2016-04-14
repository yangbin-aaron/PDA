package com.toughegg.teorderpo.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;

/**
 * Created by toughegg on 15/12/11.
 */
public class MyDialogTwoBtn extends Dialog {

    private Context mContext;
    private TextView titleTextView;
    private TextView messageTextView, messageListTextView;
    private TextView leftTextView, rightTextView;
    private OnDialogClickListener mOnDialogClickListener = null;
    private int messageId = 0;
    private String message = null, messageList = null;
    private int leftId = 0, rightId = 0;
    private String titleStr = null;
    private String leftStr = null;
    private String rightStr = null;
    private boolean isCancelable = true;

    private RelativeLayout mRelativeLayout;
    private int mHeight = 150;

    private boolean isBackDiss = true;// 返回键是否可以关闭对话框

    public void setIsBackDiss (boolean isBackDiss) {
        this.isBackDiss = isBackDiss;
    }

    public void setIsCancelable (boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public void setMessage (int messageId) {
        this.messageId = messageId;
    }

    public void setMessageList (String messageList) {
        this.messageList = messageList;
    }

    // 设置dialog高度
    public void setHeight (int height) {
        mHeight = SystemTool.dip2px (mContext, height);
    }

    public void setLeftRightBtnId (int leftId, int rightId) {
        this.leftId = leftId;
        this.rightId = rightId;
    }

    public void setLeftRightBtnStr (String leftStr, String rightStr) {
        this.leftStr = leftStr;
        this.rightStr = rightStr;

    }


    public void setTitleLeftRightBtnStr (String titleStr, String leftStr, String rightStr) {
        this.titleStr = titleStr;
        this.leftStr = leftStr;
        this.rightStr = rightStr;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public void setOnDialogClickListener (OnDialogClickListener onDialogClickListener) {
        mOnDialogClickListener = onDialogClickListener;
    }

    public MyDialogTwoBtn (Context context) {
        super (context, R.style.styles_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from (mContext);
        View view = inflater.inflate (R.layout.dialog_2_btn_layout, null);
        setContentView (view);
        setCancelable (isCancelable);

        if (mHeight != 150) {
            mRelativeLayout = (RelativeLayout) findViewById (R.id.dialog_util_loading_layout);
            ViewGroup.LayoutParams layoutParams =  mRelativeLayout.getLayoutParams ();
            layoutParams.height = mHeight;
            mRelativeLayout.setLayoutParams (layoutParams);
        }

        messageTextView = (TextView) findViewById (R.id.dialog_message_textview);
        if (messageId != 0) {
            messageTextView.setText (messageId);
        }

        if (message != null) {
            messageTextView.setText (message);
        }

        messageListTextView = (TextView) findViewById (R.id.dialog_messagelist_textview);
        if (messageList != null) {
            messageTextView.setVisibility (View.GONE);
            findViewById (R.id.dialog_messagelist_scrollview).setVisibility (View.VISIBLE);
            messageListTextView.setText (messageList);
        }

        titleTextView = (TextView) findViewById (R.id.dialog_title_textView);
        if (titleStr != null) {
            titleTextView.setText (titleStr);
        }

        leftTextView = (TextView) findViewById (R.id.dialog_left_textview);
        if (leftId != 0) {
            leftTextView.setText (leftId);
        } else if (leftStr != null) {
            leftTextView.setText (leftStr);
        }
        leftTextView.setOnClickListener (mClickListener);
        rightTextView = (TextView) findViewById (R.id.dialog_right_textview);
        if (rightId != 0) {
            rightTextView.setText (rightId);
        } else if (rightStr != null) {
            rightTextView.setText (rightStr);
        }
        rightTextView.setOnClickListener (mClickListener);

        Window dialogWindow = getWindow ();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes ();
        DisplayMetrics d = mContext.getResources ().getDisplayMetrics (); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        lp.height = d.heightPixels;
        dialogWindow.setAttributes (lp);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener () {
        @Override
        public void onClick (View v) {
            if (mOnDialogClickListener != null) {
                if (v == leftTextView) {
                    mOnDialogClickListener.onLeftClicked ();
                    return;
                } else if (v == rightTextView) {
                    mOnDialogClickListener.onRightClicked ();
                    return;
                }
            }
        }
    };

    /**
     * 返回键关闭对话框
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBackDiss) {
                dismiss ();
            }
        }
        return super.onKeyDown (keyCode, event);
    }

    public static abstract interface OnDialogClickListener {
        public abstract void onLeftClicked ();

        public abstract void onRightClicked ();
    }
}
