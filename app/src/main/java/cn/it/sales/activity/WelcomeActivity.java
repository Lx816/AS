package cn.it.sales.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import cn.it.sales.R;
import cn.it.sales.Service.MyService;
import cn.it.sales.Service.SalesBinder;
import cn.it.sales.bean.User;
import cn.it.sales.dao.LoginDao;
//111111

public class WelcomeActivity extends BaseActivity {
    Handler mHandler;

    String mCallbackData, mAcceptCallbackData;

    Boolean mIsConnection = false;
    User mUser = new User();
    SalesBinder mBinder;
    ServiceConnection mSC = null;
    LoginDao mLoginDao = new LoginDao();
    LoginActivity mMainActivity = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                initLogin();

                super.handleMessage(msg);
            }
        };
        mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    private void initLogin() {


        if (mLoginDao.firstRun(this)) {
            return;
        }
        ValidationLoginStatus();
    }

    private void ValidationLoginStatus() {
        mUser = mLoginDao.loginMessage(this);
        String gongHao = mUser.getGongHao();
        String password = mUser.getPassWord();
       // mBinder.selectUserNameAndPassword(mUser);

        //用TextUtils.isEmpty替换
        if (!TextUtils.isEmpty(gongHao) && !TextUtils.isEmpty(password)) {
            //从服务器效验账户密码
            //暂时屏蔽
          //  initBinder();
            Intent intent = new Intent(WelcomeActivity.this, SalesMainActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
    //接收传过来的user对象，通过服务传给网络
    private void initBinder() {
        Intent serviceIntent=new Intent(WelcomeActivity.this,MyService.class);
        mSC=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder= (SalesBinder) service;
                mIsConnection=true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsConnection=false;
            }
        };
        this.bindService(serviceIntent,mSC, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
//        this.unbindService(mSC);
        super.onStop();
    }
}
