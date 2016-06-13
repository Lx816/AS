package cn.it.sales.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.it.sales.application.MyApplication;
import cn.it.sales.bean.ResultUser;
import cn.it.sales.bean.User;
import cn.it.sales.bean.UserGroup;
import cn.it.sales.communal.Communals;
import cn.it.sales.db.MyOpenHelp;

/**
 * Created by Administrator on 2016/5/4.
 */
public class UserDao {
    //根据USERMAME 查找有无此用户
    private final String TABLENAME = "user";
    private final String TABLENAME_GROUP = "usergroup";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_USERNAME = "username";
    private final String COLUMN_PASSWORD = "password";
    private final String COLUMN_GROUPID = "groupId";
    private final String COLUMN_USERID = "userid";
    private MyOpenHelp myOpenHelp;

    public UserDao() {
        myOpenHelp = MyApplication.getDb1Help();

    }

    public User userSelectByName(User user) {
        String sql = String.format("select * from user where userName= '%s' ", user.getGongHao());
        List<Map<String, Object>> listmap = myOpenHelp.examine(sql);
        List<User> users = listMapToUser(listmap);
        if(users!=null && users.size()>0){
            return users.get(0);
        }
        return null;
    }

    private List<User> listMapToUser(List<Map<String, Object>> list) {
        List<User> list2 = new ArrayList<User>();
        for (int i = 0; i < list.size(); i++) {
            User user = new User();
            Map<String, Object> map = list.get(i);
            if (map.containsKey(COLUMN_ID)) {
                user.setUserId((Integer) map.get(COLUMN_ID));
                list2.add(user);
            }

        }
        return list2;
    }
    public long adduser(User user){
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",user.getGongHao());
        contentValues.put("password",user.getPassWord());
        contentValues.put("name",user.getNick());
        contentValues.put("PhoneNumber",user.getphone());
        long i=myOpenHelp.add(TABLENAME,contentValues);
        if(i>0){
            user.setUserId(i);
            return i;
        }
        return -1;
    }
    public long addToGroup(UserGroup userGroup){
        ContentValues contentValues=new ContentValues();
        contentValues.put("userid",userGroup.getGroupId());
        contentValues.put("groupid",userGroup.getGroupId());
        long i=myOpenHelp.add(TABLENAME_GROUP,contentValues);
        if(i>0){
            return  i;
        }
        return -1;
    }
    public User SelectByLogin(User user){
        String sql = String.format("select * from user where userName= '%s' and password= '%s' ",user.getGongHao(),user.getPassWord());
        List<Map<String, Object>> listmap=myOpenHelp.examine(sql);
        List<User> users=listMapTolistUserForLogin(listmap);
        if(users!=null&&users.size()>0){
            return users.get(0);
        }
        return null;
    }

    private List<User> listMapTolistUserForLogin(List<Map<String, Object>> list) {
        List<User> list2=new ArrayList<User>();
        for(int i=0;i<list2.size();i++){
            User user=new User();
            Map<String,Object> map=list.get(i);
           if(map.containsKey(COLUMN_USERNAME)&&map.containsKey(COLUMN_PASSWORD)&&map.containsKey(COLUMN_GROUPID)){
               user.setUserId((Integer) map.get(COLUMN_ID));
               user.setGongHao((String)map.get(COLUMN_USERNAME));
               user.setPassWord((String)map.get(COLUMN_PASSWORD));
               list2.add(user);
           }
        }
       return list2;
    }
    public UserGroup selectByloginforGroup(long userid){
        String  sql=String.format("select * from usergroup where userid= %d ", userid);
        List<Map<String,Object>> listmap=myOpenHelp.examine(sql);
        List<UserGroup> users=listMapTolistUserGroupForLogin(listmap);
        if(users!=null&&users.size()>0){
            return users.get(0);
        }
        return null;
    }

    private List<UserGroup> listMapTolistUserGroupForLogin(List<Map<String, Object>> listmap) {
        List<UserGroup> list2 = new ArrayList<UserGroup>();
        for(int i=0;i<listmap.size();i++){
            UserGroup  user =new UserGroup();
            Map<String , Object> map = listmap.get(i);
            if(map.containsKey(COLUMN_GROUPID)){
                long ii=(Integer) map.get(COLUMN_GROUPID)+0L;
                user.setGroupId(ii);
                list2.add(user);
            }
        }
        return list2;
    }
    public void  addTOSharedpreFerences(ResultUser resultUser){
       SharedPreferences sp= MyApplication.getMyApplication().getSharedPreferences(Communals.sharedPreferencesforlogIn, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor= sp.edit();
        editor.putString("username",resultUser.getUsername());
        editor.putString("password",resultUser.getPassword());
        editor.putLong("groupid",resultUser.getGroupid());
        editor.commit();
    }

}
