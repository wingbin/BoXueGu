package com.boxuegu.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.boxuegu.R;
import com.boxuegu.utils.MD5Utils;
import com.boxuegu.utils.OkhttpTool;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back,tv_register,tv_find_psw;
    private Button btn_login;
    private String userName,psw,spPsw;
    private EditText et_user_name,et_psw;

    //登录等待对话框
    ProgressDialog pgd;
    //登录成功标示
    public static final int OK = 0x123;
    //登录失败标示
    public static final int FAILED = 0x456;
    //WEB服务根URL
    public static final String WEB_BASE_URL = "http://192.168.45.60:8080/Boxuegu/";
    

    //处理网络访问完毕后的更新UI
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //隐藏对话框
            pgd.dismiss();
            if(msg.what==OK){
                //登录成功
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //保存登录状态
                saveLoginStatus(true, userName);
                Intent data=new Intent();
                data.putExtra("isLogin",true);
                setResult(RESULT_OK,data);
                LoginActivity.this.finish();
            }else{
                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    /**
     * 获取界面控件
     */
    private void init(){
        tv_main_title=(TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back=(TextView) findViewById(R.id.tv_back);
        tv_register=(TextView) findViewById(R.id.tv_register);
        tv_find_psw= (TextView) findViewById(R.id.tv_find_psw);
        btn_login=(Button) findViewById(R.id.btn_login);
        et_user_name=(EditText) findViewById(R.id.et_user_name);
        et_psw=(EditText) findViewById(R.id.et_psw);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        //初始化对话框
        pgd = new ProgressDialog(this);
        pgd.setIndeterminate(true);
        pgd.setTitle("提示");
        pgd.setMessage("正在登录...");

        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,FindPswActivity.class);
                startActivity(intent);
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();

                //显示登录等待
                pgd.show();
                //新开一个线程去访问网络
                new Thread(){
                    @Override
                    public void run() {
                        OkhttpTool tool = new OkhttpTool();
                        String ret = "";
                        try{
//                            ret =  tool.get(WEB_BASE_URL+"login?username="+userName+"&password="+psw);
                            ret =  tool.login(WEB_BASE_URL+"login",userName,psw);
                            Log.i(">>>>>>>>>>>>",ret);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(ret.trim().length()>2){
                            handler.sendEmptyMessage(OK);
                        }else{
                            handler.sendEmptyMessage(FAILED);
                        }
                    }
                }.start();

//                String md5Psw=MD5Utils.md5(psw);
//                spPsw=readPsw(userName);// []
//                if(TextUtils.isEmpty(userName)){
//                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(TextUtils.isEmpty(psw)){
//                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(md5Psw.equals(spPsw)){
//                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    //保存登录状态
//                    saveLoginStatus(true, userName);
//                    Intent data=new Intent();
//                    data.putExtra("isLogin",true);
//                    setResult(RESULT_OK,data);
//                    LoginActivity.this.finish();
//                    return;
//                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
//                    Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
//                    return;
//                }else{
//                    Toast.makeText(LoginActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
//                }


            }
        });
    }
    /**
     *从SharedPreferences中根据用户名读取密码
     */
    private String readPsw(String userName){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName, "");
    }
    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin", status);//存入boolean类型的登录状态
        editor.putString("loginUserName", userName);//存入登录状态时的用户名
        editor.commit();//提交修改
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从注册界面传递过来的用户名
            String userName =data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }
        }
    }

}