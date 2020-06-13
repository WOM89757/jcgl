package com.wm.jcgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class getGeomapInfo {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 60, TimeUnit.SECONDS))
            .retryOnConnectionFailure(true)
            .build();

    public static void main(String[] args) {
        String data[] ={"天度初中","召公初中","召首初中","天度中心小学","灵护小学","西张小学 ","三头小学","聚粮小学 ","吕宅小学","新庄小学 ","袁新小学  ","后董小学","官道小学","召首小学","作里小学","召公中心小学  ","吴家小学","召光小学"};
        Map<String, String> schoolMap = new HashMap<String, String>();

        for (String name:data) {

            Request request = new Request.Builder()
                    .url("https://api.map.baidu.com/?qt=s&c=171&wd='"+name+"'&rn=10&ie=utf-8&oue=1&fromproduct=jsapi&res=api&ak=E4805d16520de693a3fe707cdc962045")
                    .get()
                    .build();
            //通过URL下载内容
            try (Response response = client.newCall(request).execute()) {
                String responseText = response.body().string();
//            System.out.println(responseText);
                //转为json数组
                JSONObject objects = JSON.parseObject(responseText);
                JSONArray content = JSON.parseArray(objects.getString("content"));

                //拿到经纬度
                JSONObject jsonObject = content.getJSONObject(0);
                String x = jsonObject.getString("navi_x");
                String y = jsonObject.getString("navi_y");

                schoolMap.put(name,"["+x+","+y+"]");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(schoolMap);
        System.out.println(schoolMap.toString().replace("=",":"));
        System.out.println(JSON.toJSON(schoolMap));


    }
}
