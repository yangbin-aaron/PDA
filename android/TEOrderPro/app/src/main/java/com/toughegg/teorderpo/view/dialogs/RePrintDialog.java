package com.toughegg.teorderpo.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.adapters.DialogRePrintAdapter;

/**
 * Created by toughegg on 16/2/26.
 */
public class RePrintDialog extends Dialog {

    private Context mContext;
    private RelativeLayout mRelativeLayout;// 最外层的布局
    private ListView mListView;
    private TextView mReprintTextView, mCancelTextView;

    private TEOrderPoApplication mApplication;
    private DialogRePrintAdapter mDialogRePrintAdapter;

    private OnDialogClickListener mOnDialogClickListener = null;

    public RePrintDialog (Context context) {
        super (context, R.style.styles_dialog);
        this.mContext = context;
        mApplication = (TEOrderPoApplication) context.getApplicationContext ();
    }

    public void setOnDialogClickListener (OnDialogClickListener onDialogClickListener) {
        mOnDialogClickListener = onDialogClickListener;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from (mContext);
        View view = inflater.inflate (R.layout.dialog_reprint_layout, null);
        setContentView (view);
        setCancelable (false);
        mRelativeLayout = (RelativeLayout) findViewById (R.id.dialog_util_loading_relativelayout);
        mListView = (ListView) findViewById (R.id.dialog_btn_listview);
        mDialogRePrintAdapter = new DialogRePrintAdapter (mContext, mApplication.printLog);
        mListView.setAdapter (mDialogRePrintAdapter);
        mListView.setEnabled (false);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams (SystemTool.dip2px (mContext, 230),
                        SystemTool.dip2px (mContext, 40 * mApplication.printLog.size () + 95));
        mRelativeLayout.setLayoutParams (layoutParams);

        mCancelTextView = (TextView) findViewById (R.id.dialog_left_textview);
        mCancelTextView.setOnClickListener (mClickListener);
        mReprintTextView = (TextView) findViewById (R.id.dialog_right_textview);
        mReprintTextView.setOnClickListener (mClickListener);

        Window dialogWindow = getWindow ();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes ();
        DisplayMetrics d = mContext.getResources ().getDisplayMetrics (); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        lp.height = d.heightPixels;
        dialogWindow.setAttributes (lp);
    }

    public static abstract interface OnDialogClickListener {
        public abstract void onLeftClicked ();

        public abstract void onRightClicked ();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener () {
        @Override
        public void onClick (View v) {
            if (mOnDialogClickListener != null) {
                if (v == mCancelTextView) {
                    mOnDialogClickListener.onLeftClicked ();
                    return;
                } else if (v == mReprintTextView) {
                    mOnDialogClickListener.onRightClicked ();
                    return;
                }
            }
        }
    };
}
