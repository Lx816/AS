package cn.it.sales.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class User  implements Serializable{
    //在线状态
    public static  final int ONLINE_ON = 1;
    //离线状态
    public static  final int ONLINE_OFF = 2;
    //需要校验密码方可登录
    public static final int ONLINE_VERIFY=3;
    private  int LOGIN_ZHUANGTAI;

    public int getLOGIN_ZHUANGTAI() {
        return LOGIN_ZHUANGTAI;
    }

    public void setLOGIN_ZHUANGTAI(int LOGIN_ZHUANGTAI) {
        this.LOGIN_ZHUANGTAI = LOGIN_ZHUANGTAI;
    }


    public String getAddRess() {
        return addRess;
    }
    public void setAddRess(String addRess) {
        this.addRess = addRess;
    }

    private String addRess="";
    private String gongHao="";
    private String passWord="";
    private String nick="";
    private String phone="";
    private  String email="";
    private int online = ONLINE_VERIFY;
    private String message="";
    private long userId=-1;
    private long groupId=-1;
    public long getGroupId() {
        return groupId;
    }
    public void setGroupId(long groupId) {

        this.groupId = groupId;
    }
    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }

    private int resultcode;

    public User(String userName, String passWord, String nick, String phone, long groupId) {
        this.gongHao = userName;
        this.passWord = passWord;
        this.nick = nick;
        this.phone = phone;
        this.groupId = groupId;
    }

    public User(){

    }
    public User(String gongHao,String mPassword,long groupid){
        setGongHao(gongHao);
        setPassWord(mPassword);
        setGroupId(groupid);
    }
    public User(long userId, String gongHao, String passWord, String nick, String phone, String email, long online,long groupId) {
        setUserId(userId);
        setGongHao(gongHao);
        setPassWord(passWord);
        setNick(nick);
        setPhone(phone);
        setEmail(email);
        setOnline(online);
        setGroupId(groupId);

    }

    public User(String gongHao, String mPassword, String mName, String mPhone, String mAddRess, int mPosition) {
        // TODO Auto-generated constructor stub
        setGongHao(gongHao);
        setPassWord(mPassword);
        setNick(mName);
        setPhone(mPhone);
        setAddRess(mAddRess);
        setGroupId(mPosition);



    }
    public User(String gongHao, String mPassword, String name, String phone,int mPosition) {
        // TODO Auto-generated constructor stub
        setGongHao(gongHao);
        setPassWord(mPassword);
        setNick(name);
        setPhone(phone);
        setGroupId(mPosition);

    }
    public long getUserId() {
        return userId;
    }


    public String getPassWord() {
        return passWord;
    }

    public String getNick() {
        return nick;
    }

    public String getphone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public long getOnline() {
        return online;
    }

    public void setUserId(long userId) {
        if(userId>0){
            this.userId = userId;
        }else {
            userId = -1;
        }

    }

    public String getGongHao() {
        return gongHao;
    }

    public void setGongHao(String gongHao) {
        if (!TextUtils.isEmpty(gongHao)){
            this.gongHao = gongHao;
        }else {
            this.gongHao="";
        }
    }

    public void setPassWord(String passWord) {
        if(!TextUtils.isEmpty(passWord)){
            this.passWord = passWord;
        }else {
            this.passWord = "";
        }

    }

    public void setNick(String nick) {
        if(!TextUtils.isEmpty(nick)) {
            this.nick = nick;
        }else {
            this.nick= "";
        }
    }

    public void setPhone(String phone) {
        if(!TextUtils.isEmpty(phone)) {
            this.phone = phone;
        }else {
            this.phone = "";
        }
    }

    public void setEmail(String email) {
        if(!TextUtils.isEmpty(email)) {
            this.email = email;
        }else {
            this.email = "";
        }
    }

    public void setOnline(long online) {
        if(online == ONLINE_ON || online == ONLINE_OFF)  {
            this.online = (int) online;
        }else {
            this.online = ONLINE_VERIFY;
        }
    }
}
