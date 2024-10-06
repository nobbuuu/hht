package com.booyue.view.wheelview;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.booyue.view.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/21.
 */
public class RegionWheelView extends LinearLayout {

    private static final String TAG = "RegionView";


    private WheelView wv_province;//省
    private WheelView wv_city;//市
    private Context mContext;
    private String mCurProvince;


    public RegionWheelView(Context context) {
        super(context);
        initLayout(context);

    }

    public RegionWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public RegionWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        Log.d(TAG, "initLayout()");
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_wheel_region, this);
        wv_province = (WheelView) view.findViewById(R.id.wv_province);
        wv_city = (WheelView) view.findViewById(R.id.wv_city);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）=720
        LinearLayout.LayoutParams paramP = new LinearLayout.LayoutParams(width * 4/9,LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams paramC = new LinearLayout.LayoutParams(width * 5/9,LayoutParams.MATCH_PARENT);
        wv_province.setLayoutParams(paramP);
        wv_city.setLayoutParams(paramC);
        wv_province.setLabel("");
        wv_city.setLabel("");
        parseJson();
//        wv_province.setLabel("市");
//        wv_city.setLabel("区");
//        wv_province.setAdapter(new ArrayWheelAdapter<>(mPArray));
//        wv_city.setAdapter(new ArrayWheelAdapter<>(mPArray));
        wv_province.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setCurrentPC(newValue);
            }
        });
        int textSize = (int) context.getResources().getDimension(R.dimen.wheelview_item_textsize);
        wv_city.TEXT_SIZE = textSize;
        wv_province.TEXT_SIZE = textSize;
    }

    /**
     * 从文件中读取json
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext) {

        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open("address.txt")));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     * 对json进行解析
     */
    private String[] mPArray;

    private Map<String, String[]> mCity = new HashMap<>();

    public void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(getJson(mContext));
            JSONArray province = jsonObject.getJSONArray("province");
            JSONObject city = jsonObject.getJSONObject("city");
//            JSONObject district = jsonObject.getJSONObject("district");
            List<String> pList = new ArrayList<>();//暂时存放省份
            for (int i = 0; i < province.length(); i++) {
                pList.add((String) province.get(i));//获取省份

                List<String> c = new ArrayList<>();//暂时存放市

                JSONArray cityJSONArray = city.getJSONArray((String) province.get(i));

                for (int i1 = 0; i1 < cityJSONArray.length(); i1++) {

                    c.add((String) cityJSONArray.get(i1));
//                    JSONArray districtJSONArray = district.getJSONArray(province.get(i) + "-" + cityJSONArray.get(i1));
//                    LoggerUtils.d(TAG,province.get(i) + "-" + cityJSONArray.get(i1) + districtJSONArray.toString());
//                    List<String> dis = new ArrayList<>();
//                    for (int i2 = 0; i2 < districtJSONArray.length(); i2++) {
//                        dis.add((String) districtJSONArray.get(i2));
//                    }
//                    mDistrict.put(province.get(i) + "-" + cityJSONArray.get(i1), dis);
                }
                //市 list copy to string[]
                String[] cArray = new String[c.size()];
                for (int i1 = 0; i1 < c.size(); i1++) {
                    cArray[i1] = c.get(i1);
                }

                mCity.put((String) province.get(i), cArray);
            }
            //省 list to string[]
            mPArray = new String[pList.size()];
            for (int i = 0; i < pList.size(); i++) {
                mPArray[i] = pList.get(i);
            }
            wv_province.setAdapter(new ArrayWheelAdapter<>(mPArray));
            wv_province.setCurrentItem(18);
            setCurrentPC(18);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 滚动的时候实时变化
     * @param wv_provinceCurrentItem
     */
    public void setCurrentPC(int wv_provinceCurrentItem){
        mCurProvince = mPArray[wv_provinceCurrentItem];
        String[] cityArry = mCity.get(mCurProvince);
        wv_city.setAdapter(new ArrayWheelAdapter<>(cityArry));
        wv_city.setCurrentItem(cityArry.length / 2);
    }

    /**
     * 设置指定的地区
     * @param province 省
     * @param city 城市
     */
    public void setRegion(String province,String city){
        int provinceIndex = 18;
        for (int i = 0; i < mPArray.length; i++) {
            if(mPArray[i].equals(province)){
                provinceIndex = i;
                break;
            }
        }
        wv_province.setCurrentItem(provinceIndex);
        mCurProvince = province;
        String[] cityArry = mCity.get(mCurProvince);
        wv_city.setAdapter(new ArrayWheelAdapter<>(cityArry));
        int cityIndex = cityArry.length / 2;
        for (int i = 0; i < cityArry.length; i++) {
            if(cityArry[i].equals(city)){
                cityIndex = i;
                break;
            }
        }
        wv_city.setCurrentItem(cityIndex);
    }
    /**
     * 当前市
     *
     * @return
     */

    public String getRegion() {
        int currentItem = wv_city.getCurrentItem();
        return mCurProvince + mCity.get(mCurProvince)[currentItem];
    }


}
