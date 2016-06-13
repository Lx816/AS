package cn.it.sales.bll;

import android.content.Context;

import cn.it.sales.Result.MyResult;
import cn.it.sales.bean.ResultUser;
import cn.it.sales.bean.User;
import cn.it.sales.bean.UserGroup;
import cn.it.sales.dao.LoginDao;
import cn.it.sales.dao.UserDao;

/**
 * Created by Administrator on 2016/5/4.
 */
public class UserManager {
    LoginDao mLoginDao=new LoginDao();
    User mUser=new User();
    Context mContext;
    public MyResult register(User user){
        MyResult myResult = new MyResult();
        //1.1从库表查有无此用户
        UserDao userDao = new UserDao();
        User userTemo = userDao.userSelectByName(user);
        if (userTemo != null && userTemo.getUserId() > 0) {
            myResult.setCodeAndMessage(-1, "用户已存在，不能重复注册");
            return myResult;
        }
        //1.2有则返回重复，无则添加
        //2添加新用户
        long i=userDao.adduser(user);
        if(i<1){
            myResult.setCodeAndMessage(-1,"添加用户失败");
            return myResult;
        }
        //2.1添加组
        UserGroup userGroup=new UserGroup(-1,user.getUserId(),user.getGroupId());
        long ig=userDao.addToGroup(userGroup);
        if(ig<1){
            myResult.setCodeAndMessage(-1,"添加用户组失败");
            return  myResult;
        }
        myResult.setMessage("添加用户成功");
        return myResult;
        //2.1添加组
        //2.2 调用login 完成注册后并自动登录0
    }
    public MyResult login(User user){
        MyResult myResult=new MyResult();
        UserDao userDao=new UserDao();
        User loginTemp=userDao.SelectByLogin(user);
        if(loginTemp!=null&&!loginTemp.getGongHao().equals("")&&!loginTemp.getPassWord().equals("")){
            UserGroup loginGroup=userDao.selectByloginforGroup(loginTemp.getGroupId());
            if(loginGroup.getGroupId()!=-1){
                myResult.setCodeAndGroup(1,loginGroup.getGroupId());
            }
        }else{
            myResult.setCodeAndMessage(-1,"用户名或密码错误");
//            Log.e("xxx",myResult.getMessage());
        }
        return myResult;
    }
    public void resultToSharedPreference(ResultUser resultUser){
        UserDao userDao=new UserDao();
        userDao.addTOSharedpreFerences(resultUser);
    }
    //第一次进入app
    public boolean firstRun(Context context){

       boolean firstRun= mLoginDao.firstRun(context);
        return firstRun;
    }
    //登录首选项数据
    public User loginMessage(Context context){
        mLoginDao.loginMessage(context);
        return mUser;
    }
    //首选项注册
    public void writeRegisterMessage(Context context,User user){
        this.mContext=context;
        this.mUser=user;
        mLoginDao.writeRegisterMessage(context,user);
    }
}
