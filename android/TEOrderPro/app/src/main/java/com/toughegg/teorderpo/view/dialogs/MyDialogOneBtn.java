package com.toughegg.teorderpo.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.toughegg.teorderpo.R;

/**
 * Created by toughegg on 15/12/11.
 */
public class MyDialogOneBtn extends Dialog{

    private Context mContext;
    private TextView messageTextView;
    private TextView confirmTextView;
    private OnDialogClickListener mOnDialogClickListener = null;
    private int messageId = 0;
    private String message = null;
    private int confirmId = 0;
    private boolean isCancelable = true;

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

    public void setConfirmId (int confirmId) {
        this.confirmId = confirmId;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public void setOnDialogClickListener (OnDialogClickListener onDialogClickListener) {
        mOnDialogClickListener = onDialogClickListener;
    }

    public MyDialogOneBtn (Context context) {
        super (context, R.style.styles_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from (mContext);
        View view = inflater.inflate (R.layout.dialog_1_btn_layout, null);
        setContentView (view);
        setCancelable (isCancelable);
        messageTextView = (TextView) findViewById (R.id.dialog_message_textview);
        if (messageId != 0) {
            messageTextView.setText (messageId);
        }

        if (message != null) {
            messageTextView.setText (message);
        }

        confirmTextView = (TextView) findViewById (R.id.dialog_confirm_textview);
        if (confirmId != 0) {
            confirmTextView.setText (confirmId);
        }
        confirmTextView.setOnClickListener (mClickListener);

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
            if (v == confirmTextView) {
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onConfirmClicked ();
                    return;
                } else {
                    dismiss ();
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
        public abstract void onConfirmClicked ();
    }
}
