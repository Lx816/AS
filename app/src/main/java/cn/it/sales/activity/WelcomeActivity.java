package cn.it.sales.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import cn.it.sales.R;
import cn.it.sales.Service.MyService;
import cn.it.sales.Service.SalesBinder;
import cn.it.sales.bean.User;
import cn.it.sales.bll.UserManager;

public class WelcomeActivity extends BaseActivity {

    Boolean mIsConnection = false;
    User mUser = new User();
    SalesBinder mBinder;
    ServiceConnection mSC = null;
    UserManager mUserManager=new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        initLogin();
    }

    private void initLogin() {


        if (mUserManager.firstRun(this)) {
            return;
        }
        ValidationLoginStatus();
    }

    private void ValidationLoginStatus() {
        mUser =mUserManager.loginMessage(this);
        String userName = mUser.getUserName();
        String password = mUser.getPassWord();
       // mBinder.selectUserNameAndPassword(mUser);

        //用TextUtils.isEmpty替换
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            //从服务器效验账户密码
            //暂时屏蔽
           initBinder();
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
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
        if(mSC!=null) {
            this.unbindService(mSC);
        }
        super.onStop();
    }
}
