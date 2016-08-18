package com.perasia.slidepic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.perasia.slidepic.view.CircleIndicator;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private Context mContext;



    private AutoScrollViewPager viewPager;

    private ArrayList<picData> datas;



    private int oldPosition, index;
    private int pos =1;
    private CircleIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager_default);
        indicator = (CircleIndicator) findViewById(R.id.indicator_default);

        init();
    }

    private void init() {
        datas = new ArrayList<>();

        picData data1 = new picData();
        data1.setTitle("标题1");
        data1.setPicUrl("http://source.jisuoping.com/image/20160120152355188.jpg");
        data1.setGoUrl("http://www.baidu.com");
        datas.add(data1);

        picData data2 = new picData();
        data2.setTitle("标题2");
        data2.setPicUrl("http://imgsrc.baidu.com/forum/pic/item/cf8cdb22720e0cf39b7f28510a46f21fbc09aaea.jpg");
        data2.setGoUrl("http://www.baidu.com");
        datas.add(data2);
//
//        picData data3 = new picData();
//        data3.setTitle("标题3");
//        data3.setPicUrl("http://img2.cache.netease.com/ent/2012/8/3/2012080303380055dd4.jpg");
//        data3.setGoUrl("http://www.baidu.com");
//        datas.add(data3);
//
//        picData data4 = new picData();
//        data4.setTitle("标题4");
//        data4.setPicUrl("http://i1.sinaimg.cn/edu/2014/1203/U12216P42DT20141203165538.jpeg");
//        data4.setGoUrl("http://www.baidu.com");
//        datas.add(data4);



        viewPager.setAdapter(new ImagePagerAdapter(mContext, datas, new ImagePagerAdapter.EventClick() {
            @Override
            public void eventClick() {
                Toast.makeText(mContext, "onclick=" + datas.get(index).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }));

         viewPager.addOnPageChangeListener(new MyOnPageChangeListener());



        if(datas.size()==1) {
            Log.d("99999","8888");
            viewPager.setCurrentItem(1);
            indicator.setViewPager(viewPager,datas.size());
            viewPager.stopAutoScroll();
        }else{
            viewPager.setInterval(3000);
            viewPager.startAutoScroll();
            viewPager.setCurrentItem(10000 * datas.size());
            indicator.setViewPager(viewPager,datas.size());
        }


    }



    private View setDaoHangText(int id) {
        View text = new View(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(MainActivity.this, 8), dip2px(MainActivity.this, 8));
        params.setMargins(5, 5, 5, 5);
        text.setLayoutParams(params);
        text.setBackgroundResource(id);
        return text;
    }

    public void Jump(View view)
    {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("5555","6667");
        viewPager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("5555","666");

        if(datas.size()> 1){
            viewPager.startAutoScroll();
        }

    }

    public class                     MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            index = (position) % datas.size();
//            mTitleTv.setText(datas.get((position) % datas.size()).getTitle());
//
//            mainLayout.getChildAt(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//            mainLayout.getChildAt((position) % datas.size()).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = (position) % datas.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
