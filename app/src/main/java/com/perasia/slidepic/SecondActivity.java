package com.perasia.slidepic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by xionghu on 2016/8/18.
 * Email：965705418@qq.com
 */
public class SecondActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }


    public void onclick(View view)
    {
        finish();
    }
}
