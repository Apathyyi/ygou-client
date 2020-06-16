package sy.bishe.ygou.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;

public class Cityutils {
    private static final List<String> citys = new ArrayList<>();
    private static final List<List<String>> provinces = new ArrayList<>();

    public Cityutils() {
        get();
    }

    public static void get(){
        RestClient.builder()
                .setUrl("city/query")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response).getJSONObject("jsonObject");
                        Set<String> strings = jsonObject.keySet();
                        Iterator<String> iterator = strings.iterator();
                        while (iterator.hasNext()){
                            String next = iterator.next();
                            Log.d("next", next);
                            citys.add(next);
                            JSONArray jsonArray = jsonObject.getJSONArray(next);
                            int size = jsonArray.size();
                            List<String> provice = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                String name = jsonArray.getString(i);
                                Log.d("name", name);
                                provice.add(name);
                            }
                            provinces.add(provice);
                        }
                    }
                })
                .build()
                .get();
    }

    public static List<String> getCitys() {
        return citys;
    }

    public static List<List<String>> getProvinces() {
        return provinces;
    }
}
