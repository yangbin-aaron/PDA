package com.toughegg.andytools;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.simpleDraweeView);

        GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = genericDraweeHierarchyBuilder
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER)
                .setPlaceholderImage(getResources().getDrawable(R.mipmap.ic_launcher), ScalingUtils.ScaleType.CENTER)
                .setFailureImage(getResources().getDrawable(R.mipmap.ic_launcher), ScalingUtils.ScaleType.CENTER)
                .build();
        simpleDraweeView.setHierarchy(hierarchy);
        simpleDraweeView.setImageURI(Uri.parse("http://www.5068.com/u/faceimg/20140806174845.jpg"));


//        http://app-uat.toughegg.sg/api/Menu/Detail?data={id:517}&token={'lan':'en','memberid':'12','key':'pklSMxaygNDANGnVGZJkqBLlNhZdPECCx3BOnoIBE34%3d'}


    }

}
