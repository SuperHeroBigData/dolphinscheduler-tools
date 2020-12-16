package com.iquantex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * @ClassName Test
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/9 11:30 上午
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        String s2 =  "ODSCH.TQ_BD_BIDINFO_x000D_";
        System.out.println(s2.replace("_x000D_",""));
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("asd","asd");
        HashMap<Object, Object> hashMap1 = new HashMap<>(hashMap);
        hashMap.clear();
        System.out.println(hashMap1);
        String s ="{\"tasks-64448\":{\"targetarr\":\"\",\"nodenumber\":\"1\",\"name\":\"ODSCH.TQ_BD_NEWESTBASICINFO_DEPENDENT\",\"x\":777,\"y\":707},\"tasks-20665\":{\"targetarr\":\"tasks-57586,tasks-64448\",\"nodenumber\":\"0\",\"name\":\"ODSCH.TQ_BD_NEWESTBASICINFO\",\"x\":384,\"y\":23},\"tasks-57586\":{\"targetarr\":\"\",\"nodenumber\":\"1\",\"name\":\"ODSCH.TQ_BD_NEWESTBASICINFO_KETTLE\",\"x\":774,\"y\":885}}";

        JSONObject parse = (JSONObject)JSON.parse(s);
        for (String key:
             parse.keySet()) {
            System.out.println(key);
        }
        System.out.println(parse);
        int a = 1;
        int b=1;
        int c = 2;

       get(a,b,c);


    }

    public static void get(int a,int b,int c){
        if (a == c || a == b){
            System.out.println(11);
        }
    }
}
