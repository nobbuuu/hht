package com.booyue.base.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinyinUtils {
	public static String getPY(String name){
		StringBuilder sb = new StringBuilder();
		try{
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0; i < name.length(); i++) {
				String s = name.substring(i,i+1);
				if(s.matches("[\u4e00-\u9fff]")){
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(i),format);
					for (String string : pinyin) {
						sb.append(string);
					}
				}else {
					if(s.matches("[a-zA-Z]")){
						s = s.toUpperCase();
						sb.append(s);
					}else{
						sb.append(s);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
}
