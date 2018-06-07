package com.boxuegu.activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.boxuegu.R;
import com.boxuegu.utils.Contants;
import com.boxuegu.view.MyVideoView;

public class VideoPlayActivity extends AppCompatActivity {
    private MyVideoView videoView;
    private MediaController controller;
    private String videoPath;//本地视频地址
    private int position;//传递视频详情界面点击的视频位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置界面全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //获取从播放记录界面传递过来的视频地址
        videoPath = getIntent().getStringExtra("videoPath");
        position=getIntent().getIntExtra("position",0);

        init();
    }
    /**
     * 初始化UI控件
     */
    private void init() {
        videoView = (MyVideoView) findViewById(R.id.videoView);
        controller = new MediaController(this);
        videoView.setMediaController(controller);

        play();
//        playWebVideo();
    }
    /**
     * 播放视频
     */
    private void play() {
        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(this, "本地没有此视频，暂无法播放", Toast.LENGTH_SHORT).show();
            return;
        }
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.video11;
        videoView.setVideoPath(uri);
        videoView.start();
    }

    /**
     * 播放网络视频
     */
    private void playWebVideo() {
        //网络视频
        String videoUrl2 = Contants.WEB_VIDEO_URL;
        Uri uri = Uri.parse( videoUrl2 );
        videoView = (MyVideoView) this.findViewById(R.id.videoView );
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));
        //播放完成回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
        //设置视频路径
        videoView.setVideoURI(uri);
        //开始播放视频
        videoView.start();
    }

    /**
     * 点击后退键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //把视频详情界面传递过来的被点击视频的位置传递回去
        Intent data=new Intent();
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        return super.onKeyDown(keyCode, event);
    }
    //自定义视频播放完成后的回调
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( VideoPlayActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
