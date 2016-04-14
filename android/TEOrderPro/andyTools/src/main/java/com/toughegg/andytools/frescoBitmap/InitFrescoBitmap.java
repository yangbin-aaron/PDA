package com.toughegg.andytools.frescoBitmap;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeView;

/**
 * Created by Andy on 15/11/3.
 */
public class InitFrescoBitmap {

    private static InitFrescoBitmap mInitFrescoBitmap;
    /**
     * 初始化Fresco
     *
     * @param context
     */
    public void InitBitmap(Context context) {
        Fresco.initialize(context);
    }


    public static InitFrescoBitmap getInstance(){
        if(mInitFrescoBitmap==null){
            mInitFrescoBitmap=new InitFrescoBitmap();
        }
        return mInitFrescoBitmap;
    }


    /**
     * 设置默认图片属性
     * @param context
     * @param draweeView
     * @param defaultImage
     * @param errorImage
     * @param scaleType
     * @return
     */
    public  void setHierarchy(Context context,DraweeView draweeView,int defaultImage, int errorImage, ScalingUtils.ScaleType scaleType) {
        GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = genericDraweeHierarchyBuilder
                .setActualImageScaleType(scaleType)
                .setPlaceholderImage(context.getResources().getDrawable(defaultImage), scaleType)
                .setFailureImage(context.getResources().getDrawable(errorImage), scaleType)
                .build();
        draweeView.setHierarchy(hierarchy);
    }



}
