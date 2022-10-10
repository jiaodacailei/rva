package com.ruoyi.rva.framework.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

@Slf4j
public class RvaPinyinUtils {

	public static void main(String[] args) {
		String str = null;
		str = "做ss坐d作左座昨佐咗唑";
		System.out.println("Spell=" + getPinyin(str));
		System.out.println("Spell=" + getPinyinFirstChar(str));
	}

	/**
	 * @param src
	 * @return
	 */
	public static String getPinyinLower(String src) {
		return getPinyin(src).toLowerCase();
	}
	
	/**
	 * 得到 全拼
	 * 
	 * @param src
	 * @return
	 */
	@SneakyThrows
	private static String getPinyin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		for (int i = 0; i < t0; i++) {
			// 判断是否为汉字字符
			if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
				t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
				t4 += t2[0];
			} else {
				t4 += Character.toString(t1[i]);
			}
		}
		return t4;
	}

	/**
	 * 得到中文首字母
	 * 
	 * @param str
	 * @return
	public static String getPinyinFirstCharUpper(String str) {
		return getPinyinFirstChar(str).toUpperCase();
	}
	 */

	public static String getPinyinFirstCharLower(String str) {
		return getPinyinFirstChar(str).toLowerCase();
	}
	
	/**
	 * 得到中文首字母
	 * 
	 * @param str
	 * @return
	 */
	private static String getPinyinFirstChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}
}
