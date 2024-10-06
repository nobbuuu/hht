package com.booyue.base.util;

import android.text.TextUtils;
import android.widget.EditText;

import com.booyue.base.toast.Tips;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.10:50
 *
 * 判空工具类，检测
 */

public class EmptyUtil {
    /**
     * 参数校验
     */
    public static boolean stringEmpty(String... params) {
        for (int i = 0; i < params.length; i++) {
            if (TextUtils.isEmpty(params[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * list是否为空
     */
    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
    /**
     * 判断字符串是否为NULL或空字符串
     */
    public static boolean isStringEmpty(String obj) {
        if (obj == null || obj.length() == 0 || obj.equals("null") || obj.contains("null")){
            return true;
        }

        return false;
    }
    /**
     * 判断editText是否为空
     * @param ets
     * @return
     */
    public static boolean isEmpty(EditText... ets){
        for (EditText editText : ets) {
            if(TextUtils.isEmpty(editText.getText().toString())){
                //如果error提示信息的背景能显示但是不显示文字
                //说明文字的颜色和背景色重复了
                //利用安卓的Html类的fromHtml方法可以构建一个Spanned类型的字符串
                //将该字符串的颜色设置为蓝色
                //此时文字的颜色和背景色就不一样了，错误提示信息就可以显示了
//                Spanned msg = Html.fromHtml("<font color = red>内容不能为空</font>");
//                editText.setError(msg);
                Tips.show("内容不能为空~");
                return true;
            }
        }
        return false;
    }
}
