package com.boxuegu.view;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.boxuegu.R;
import com.boxuegu.adapter.AdBannerAdapter;
import com.boxuegu.adapter.CourseAdapter;
import com.boxuegu.bean.CourseBean;
import com.boxuegu.utils.AnalysisUtils;
public class CourseView {
    private ListView lv_list;//课程数据列表
    private CourseAdapter adapter;//课程数据适配器
    private List<List<CourseBean>> cbl;//课程数据列表（两个一组）
    private FragmentActivity mContext;  //Contenxt:上下文
    private LayoutInflater mInflater;//视图渲染器
    private View mCurrentView;//当前视图
    private ViewPager adPager;// 广告Fragment的容器
    private View adBannerLay;// 广告条容器
    private AdBannerAdapter ada;// 轮播广告数据适配器
    public static final int MSG_AD_SLID = 002;// 广告自动滑动消息ID
    private ViewPagerIndicator vpi;// 小圆点
    private MHandler mHandler;// 事件捕获
    private List<CourseBean> cadl;//广告条数据列表（3个）
    public CourseView(FragmentActivity context) {
        mContext = context;
        // 为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private void createView() {
        mHandler = new MHandler();
        initAdData();//初始化广告数据
        getCourseData();//从XML获取课程章节数据
        initView();//获取UI组件，并且绑定数据适配器

        //启动子线程（每5秒通过handler发送切换广告的消息）
        new AdAutoSlidThread().start();
    }
    /**
     * 事件捕获，收到指定消息后进行切换
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }
                    break;
            }
        }
    }
    /**
     * 广告自动滑动子线程
     */
    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null)
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
            }
        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mCurrentView = mInflater.inflate(R.layout.main_view_course, null);
        //获取课程章节列表组件
        lv_list = (ListView) mCurrentView.findViewById(R.id.lv_list);
        //课程数据列表适配器
        adapter = new CourseAdapter(mContext);
        //设置课程数据
        adapter.setData(cbl);
        //设置数据列表的适配器
        lv_list.setAdapter(adapter);
        //获取ViewPager容器
        adPager = (ViewPager) mCurrentView.findViewById(R.id.vp_advertBanner);
        //禁止长按
        adPager.setLongClickable(false);

        //广告条数据适配器
        ada = new AdBannerAdapter(mContext.getSupportFragmentManager(),
                mHandler);
        //给ViewPager设置适配器
        adPager.setAdapter(ada);// 给ViewPager设置适配器
        //设置触摸监听器
        adPager.setOnTouchListener(ada);

        //导航圆点组件
        vpi = (ViewPagerIndicator) mCurrentView
                .findViewById(R.id.vpi_advert_indicator);// 获取广告条上的小圆点
        //设置圆点数量为广告条的数量
        vpi.setCount(ada.getSize());// 设置小圆点的个数
        //外层容器组件
        adBannerLay = mCurrentView.findViewById(R.id.rl_adBanner);
        //ViewPager切换监听器
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            //当切换广告的时候，处理vpi的圆点的生成
            @Override
            public void onPageSelected(int position) {
                if (ada.getSize() > 0) {
                    //由于index数据在滑动时是累加的，
                    // 因此用index % ada.getSize()来标记滑动到的当前位置
                    vpi.setCurrentPosition(position % ada.getSize());
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //重置外层容器的尺寸，使广告条外层容器的宽度充满屏幕，高度为宽度一半
        resetSize();


        //初始化广告的状态
        if (cadl != null) {
            //默认选中第一个圆点
            if (cadl.size() > 0) {
                vpi.setCount(cadl.size());
                vpi.setCurrentPosition(0);
            }
            //设置广告数据适配器
            ada.setDatas(cadl);
        }
    }
    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = getScreenWidth(mContext);
        int adLheight = sw / 2;// 广告条高度
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        adBannerLay.setLayoutParams(adlp);
    }
    /**
     * 读取屏幕宽
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
    /**
     * 初始化广告中的数据
     */
    private void initAdData() {
        cadl = new ArrayList<CourseBean>();
        for (int i = 0; i < 3; i++) {
            CourseBean bean = new CourseBean();
            bean.id=(i + 1);
            switch (i) {
                case 0:
                    bean.icon="banner_1";
                    break;
                case 1:
                    bean.icon="banner_2";
                    break;
                case 2:
                    bean.icon="banner_3";
                    break;
                default:
                    break;
            }
            cadl.add(bean);
        }
    }
    /**
     * 获取课程信息
     */
    private void getCourseData() {
        try {
            InputStream is = mContext.getResources().getAssets().open("chaptertitle.xml");
			cbl = AnalysisUtils.getCourseInfos(is);//getCourseInfos(is)方法在下面会有说明
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView() {
        if (mCurrentView == null) {
            createView();
        }
        return mCurrentView;
    }
    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView() {
        if (mCurrentView == null) {
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
}