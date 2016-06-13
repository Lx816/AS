package cn.it.sales.util;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import cn.it.sales.application.MyApplication;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ServerUtil {
    public static final int POST = 1;
    public static final int GET = 2;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //{
    // "userName":"wwww" ,
    // "passWord":"123456"
    // "phone":"11111111"
    // }
    //methodName=0 params=josn
    // www.baidu.com?methodName=0&params=json
    //http://172.16.12.3:8080/cms/RequestJsonServlet?methodName=0&params={}
    // }
    public static  void upJsonStringByPost(String url,String selectCode,String json,OnOKHttpListener listener) throws Exception {
        final OnOKHttpListener mOnOKHttpListener = listener;
//            String urlStr="http://www.cnblogs.com/ct2011/p/400170     8.html";
//            String json= "{ 'name': 'cxh', 'gender': 'man' }";
        //RequestBody：请求的数据实体（Body,Headers）
        //根据指定的数据类型JSON把json字符串转换为body
        Log.d("xq", url+selectCode+json);
        if (TextUtils.isEmpty(json) || listener == null) {
            throw new Exception("参数不能为空");
        }
                JSONObject jsona=new JSONObject(json);
        final JSONObject jsonObject=new JSONObject();
                jsonObject.put("mode","login");
                jsonObject.put("user",jsona);
        String jsonString=jsonObject.toString();
        RequestBody formBody = new FormEncodingBuilder()
                .add("methodName", selectCode)
                .add("params", jsonString)
                .build();
        Log.d("houxiao",""+jsonString);
//        String url1 = url+json;
        //RequestBody body = RequestBody.create(JSON, json);
//        Log.e("body", formBody + "");
        Request request = new Request.Builder()
                .url(url)       //URL
                .post(formBody)     //非明文的参数部分，
                .build();
        OkHttpClient okHttpClient = MyApplication.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        Callback callback = new Callback(){

            @Override
            public void onFailure(Request request, IOException e) {
                //3.8
                if(mOnOKHttpListener!=null) {
                    mOnOKHttpListener.onFail(false);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(mOnOKHttpListener!=null) {
                    //3.5
                    String text = response.body().string();
                    mOnOKHttpListener.onSuccess(text);
                  // ResultUser mResultUser=new ResultUser(1,"注册成功");
                  // EventBus.getDefault().post(mResultUser);
//                    try {
//                        JSONObject jsonObject1=new JSONObject(text);
//                        int result=jsonObject1.getInt("result");
//                        if(result==1){
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        };
        call.enqueue(callback);

    }

    public static void downJsonString(boolean isGet, String url, Map<String, String> parmas, OnOKHttpListener listener)
            throws Exception {
        final OnOKHttpListener mOnOKHttpListener = listener;
        String urlText = null;
        String a = null;
        if (TextUtils.isEmpty(url) || listener == null) {
            throw new Exception("参数不能为空");
        }
        if (parmas != null) {
            if (isGet) {
                StringBuffer sb = new StringBuffer();

                for (Map.Entry<String, String> item : parmas.entrySet()) {
                    sb.append(item.getKey() + item.getValue()+"&");
                }
                urlText = url+"?"+sb.toString();
                a = urlText.substring(0,urlText.length()-1);
            }else{

            }
            OkHttpClient okHttpClient = MyApplication.getOkHttpClient();
            final Request request = new Request.Builder().url(a).build();
            Call call = okHttpClient.newCall(request);
            Callback callback = new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    // Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String text = response.body().string();

                    //利用GSON解析JSON字符串
                    // 第一步，创建一个GSON的实例
//                    Gson gson = new Gson();
                    // 第二步，利用成员方法fromJson，
                    // 把JSON字符串，解析为指定类对应的实例，然后通过实例操作
                    // 注意：字符串和类应该是一致的，否则没法解析。
//                    final JhsjMeiShiBean bean =
//                            gson.fromJson(text, JhsjMeiShiBean.class);

                    mOnOKHttpListener.onSuccess(text);

                }
            };
            call.enqueue(callback);
        }
    }




//    public static void gethttp(String url ,final  okhttplistener mOkhttplistener) {
//        if (url != null) {
//            OkHttpClient okHttpClient = Myapplication.mMyapplication.getmOkHttpClient();
//
//            final Request request = new Request.Builder().url(url).build();
//            Call call = okHttpClient.newCall(request);
//            Callback callback = new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    // Log.d(TAG, "onFailure: " + e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    String text = response.body().string();
//
//                    //利用GSON解析JSON字符串
//                    // 第一步，创建一个GSON的实例
//                    Gson gson = new Gson();
//                    // 第二步，利用成员方法fromJson，
//                    // 把JSON字符串，解析为指定类对应的实例，然后通过实例操作
//                    // 注意：字符串和类应该是一致的，否则没法解析。
//                    final JhsjMeiShiBean bean =
//                            gson.fromJson(text, JhsjMeiShiBean.class);
//
//                    mOkhttplistener.success(bean);
//
//                }
//            };
//            call.enqueue(callback);
//        }
//
//    }

    public interface OnOKHttpListener{
        void onSuccess(String jsonString);
        void onFail(boolean b);


    }
}
