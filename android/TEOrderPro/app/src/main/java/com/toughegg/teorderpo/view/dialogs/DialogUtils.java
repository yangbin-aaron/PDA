package com.toughegg.teorderpo.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toughegg.teorderpo.R;

/**
 * Created by toughegg on 15/8/6.
 */
public class DialogUtils {
    /**
     * 加载对话框
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog (Context context, int textResId, boolean setCancelable) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from (context);
        View v = inflater.inflate (R.layout.dialog_util_loading, null);
        LinearLayout layout = (LinearLayout) v.findViewById (R.id.dialog_util_loading_layout);
        TextView textView = (TextView) v.findViewById (R.id.dialog_util_loading_textview);
        textView.setText (textResId);
        ImageView imageView = (ImageView) v.findViewById (R.id.dialog_util_loading_imageview);
        // 开始旋转
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground ();
        animationDrawable.start ();

        Dialog loadingDialog = new Dialog (context, R.style.styles_dialog); // 创建自定义样式dialog
        loadingDialog.setCancelable (setCancelable);
        loadingDialog.setContentView (layout);
        WindowManager windowManager = ((Activity) context).getWindowManager ();
        Display display = windowManager.getDefaultDisplay ();
        WindowManager.LayoutParams lp = loadingDialog.getWindow ().getAttributes ();
        lp.width = (int) (display.getWidth ()); // 设置宽度
        lp.height = (int) (display.getHeight ());
        loadingDialog.getWindow ().setAttributes (lp);
        return loadingDialog;
    }

    public static Dialog createLoadingDialog (Context context, int textResId) {
        return createLoadingDialog (context, textResId, true);
    }

    /**
     * 创建没有点击事件的错误提示dialog
     *
     * @param context
     */
    private static void createErrorDialog (Context context, String message, int messageId) {
        MyDialogOneBtn myDialogOneBtn = new MyDialogOneBtn (context);
        if (message != null) {
            myDialogOneBtn.setMessage (message);
        }
        if (messageId != 0) {
            myDialogOneBtn.setMessage (messageId);
        }
        myDialogOneBtn.show ();
    }

    public static void createErrorDialog (Context context, String message) {
        createErrorDialog (context, message, 0);
    }

    public static void createErrorDialog (Context context, int messageResId) {
        createErrorDialog (context, null, messageResId);
    }

}
