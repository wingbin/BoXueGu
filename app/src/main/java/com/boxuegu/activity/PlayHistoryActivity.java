package com.boxuegu.activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.boxuegu.R;
import com.boxuegu.adapter.PlayHistoryAdapter;
import com.boxuegu.bean.VideoBean;
import com.boxuegu.utils.AnalysisUtils;
import com.boxuegu.utils.DBUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class PlayHistoryActivity extends AppCompatActivity{
    private TextView tv_main_title, tv_back,tv_none;//标题、返回图标、无播放记录时显示的文本框
    private RelativeLayout rl_title_bar;//标题栏
    private ListView lv_list;//播放列表
    private PlayHistoryAdapter adapter;//播放记录适配器
    private List<VideoBean> vbl;//播放记录数据列表
    private DBUtils db;//DB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_history);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //获取工具类
        db= DBUtils.getInstance(this);
        vbl=new ArrayList<VideoBean>();
        vbl=db.getVideoHistory(AnalysisUtils.readLoginUserName(this));//从数据库中获取播放记录信息
        init();
    }
    /**
     * 初始化UI控件
     */
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("播放记录");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));//导航栏加背景颜色
        tv_back = (TextView) findViewById(R.id.tv_back);
        lv_list=(ListView) findViewById(R.id.lv_list);
        tv_none=(TextView) findViewById(R.id.tv_none);
        //无播放记录则隐藏List,显示tv_none
        if(vbl.size()==0){
            tv_none.setVisibility(View.VISIBLE);
        }
        //初始化播放记录的适配器
        adapter=new PlayHistoryAdapter(this);
        adapter.setData(vbl);
        lv_list.setAdapter(adapter);
        // 后退键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHistoryActivity.this.finish();
            }
        });
    }
}