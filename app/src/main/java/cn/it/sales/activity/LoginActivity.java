package cn.it.sales.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cn.it.sales.R;
import cn.it.sales.Service.MyService;
import cn.it.sales.Service.SalesBinder;
import cn.it.sales.bean.ResultUser;
import cn.it.sales.bean.User;
import cn.it.sales.dao.LoginDao;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity {
    String mGongHao, mPassWord;
    ArrayList<String> mIsEmptylogin;
    Context mContext;
    EditText mEditTextUserName, mEditTextPassWord;
    User mUser=new User();
    long mGroupId;
    LoginDao mLoginDao=new LoginDao();
    SalesBinder mBinder;
    ServiceConnection mServiceConnection = null;
    RadioGroup mRadioGroup;
    RadioButton mRadioButtonXiaoShou,mRadioButtonZhuGuan,mRadioButtonKuGuan;
    int mRadioGroupId=1;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerEventBus();
        mContext = this;

        initEditText();
        //选择职位
       initRadioGroup();
        //跳转登录界面
        initImageView1();
        //跳转注册界面
        initImageView2();
        //绑定一个服务
        initBinder();
    }

    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    private void initBinder() {
        Intent intent = new Intent(LoginActivity.this, MyService.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (SalesBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private void initRadioGroup() {
        mRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup1);
        mRadioButtonXiaoShou = (RadioButton) findViewById(R.id.XiaoShou);
        mRadioButtonZhuGuan = (RadioButton) findViewById(R.id.ZhuGuan);
        mRadioButtonKuGuan = (RadioButton) findViewById(R.id.KuGuan);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRadioButtonXiaoShou.getId()) {
                    mRadioGroupId = 1;
                }
                if (checkedId == mRadioButtonZhuGuan.getId()) {
                    mRadioGroupId = 2;
                }
                if (checkedId == mRadioButtonKuGuan.getId()) {
                    mRadioGroupId = 3;
                }
            }
        });
    }

    private void initImageView1() {
        ImageView imageView= (ImageView) findViewById(R.id.bt_lon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    private void initImageView2() {
        ImageView imageView= (ImageView) findViewById(R.id.bt_login);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查登录输入规则
                if(checkInputDengLu()){
                    //提交数据到服务器
                    tiJiaoDengLu();
                }

            }

            private void tiJiaoDengLu() {
                mUser = new User(mGongHao,mPassWord,mGroupId);
                if(mBinder!=null) {
                    mBinder.selectUserNameAndPassword(mUser);
                }
            }
        });
    }
    public void onEventMainThread(ResultUser resultUser) {
        Toast.makeText(LoginActivity.this, resultUser.getMessage(), Toast.LENGTH_SHORT).show();
        if (resultUser.getResult() == 1) {
            //存入首选项，根据groupid跳转界面
            mLoginDao.writeRegisterMessage(this, mUser);
            mUser.setLOGIN_ZHUANGTAI(mUser.ONLINE_VERIFY);
            Intent intent = new Intent(LoginActivity.this, SalesMainActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkInputDengLu() {
        getInputMessage();
        if (mGongHao.isEmpty() || mPassWord.isEmpty()) {
            if (mGongHao.isEmpty()) {
                mIsEmptylogin.add("用户名不能为空");
            }
            if (mPassWord.isEmpty()) {
                mIsEmptylogin.add("密码不能为空");
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < mIsEmptylogin.size(); i++) {
                stringBuffer.append(mIsEmptylogin.get(i) + "\t");
            }
            String line2 = stringBuffer.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("温馨提示");
            builder.setMessage(line2);
            DialogInterface.OnClickListener listener;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
            builder.setNegativeButton("取消", listener);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        //错误信息为0
        if(mIsEmptylogin.size()==0){
            return true;
        }
        return false;
    }

    private void getInputMessage() {
        mIsEmptylogin = new ArrayList<String>();
        mGongHao = mEditTextUserName.getText().toString();
        mPassWord = mEditTextPassWord.getText().toString();
    }

    private void initEditText() {
        mEditTextUserName = (EditText) findViewById(R.id.editText1);
        mEditTextPassWord = (EditText) findViewById(R.id.editText2);
    }

    @Override
    protected void onStop() {
        if (mServiceConnection!=null) {
            this.unbindService(mServiceConnection);
            EventBus.getDefault().unregister(this);
            super.onStop();
        }
    }
}
