package com.alading.ee.util;

import android.net.ParseException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
	 */
	static final char DBC_CHAR_START = 33; // 半角!

	/**
	 * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
	 */
	static final char DBC_CHAR_END = 126; // 半角~

	/**
	 * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
	 */
	static final char SBC_CHAR_START = 65281; // 全角！

	/**
	 * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
	 */
	static final char SBC_CHAR_END = 65374; // 全角～

	/**
	 * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
	 */
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔

	/**
	 * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
	 */
	static final char SBC_SPACE = 12288; // 全角空格 12288

	/**
	 * 半角空格的值，在ASCII中为32(Decimal)
	 */
	static final char DBC_SPACE = ' '; // 半角空格

	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	public final static String MD5_2(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
					+ charset);
		}
	}




	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	public static Boolean IDCardValidate(String IDStr) throws ParseException,
			NumberFormatException, java.text.ParseException {
		String errorInfo = "";// 记录错误信息

		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return false;
		}
		// =======================(end)========================
		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return false;
		}
		// =======================(end)========================
		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(
						strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			errorInfo = "身份证生日不在有效范围。";
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return false;
		}
		Hashtable<?, ?> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return false;
		}
		// ==============================================
		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;
		if (IDStr.length() == 18) {
			if (!Ai.equalsIgnoreCase(IDStr)) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return false;
			}
		} else {
			return true;
		}
		LogX.d("Alading-身份证验证信息", errorInfo);
		// =====================(end)=====================
		return true;
	}

	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");

		return hashtable;
	}

	public static boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}

	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能描述：替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer str = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			str.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		str.append(source);
		return str.toString();
	}

	public static String getUUID() {
		return StringUtil.replace("-", "", UUID.randomUUID().toString());
	}

	/**
	 * 格式化价格，如果是纯整数就不显示小数点后的数字，如果非纯整数保留两位小数。
	 */
	public static String formatPrice(float price) {
		boolean isPureNumber = true;
		String textPrice = String.valueOf(price);
		for (int i = textPrice.length() - 1; i > textPrice.indexOf('.'); i--) {
			if (textPrice.charAt(i) != '0') {
				isPureNumber = false;
				break;
			}
		}

		if (!isPureNumber) {

			DecimalFormat df = new DecimalFormat("#0.00");

			return df.format(price);
		} else {
			return String.valueOf((int) (price));
		}
	}

	/**
	 * 格式化浮点数，强制显示小数点后两位。
	 */
	public static String formatPrice2(float f) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String str = df.format(f).toString();
		if (str.equals("0.00")) {
			str = str.split("\\.")[0];
		}
		return str;
	}

	/**
	 * 格式化浮点数，如果是整数，则不显示小数部分的00
	 */
	public static String formatPrice3(float f) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String str = df.format(f).toString();
		if (str.split("\\.")[1].equals("00")) {
			str = str.split("\\.")[0];
		}
		return str;
	}

	/**
	 * 格式化价格，不保留小数。
	 */
	public static String formatPrice5(float price) {

		boolean isPureNumber = true;
		String textPrice = String.valueOf(price);
		for (int i = textPrice.length() - 1; i > textPrice.indexOf('.'); i--) {
			if (textPrice.charAt(i) != '0') {
				isPureNumber = false;
				break;
			}
		}

		if (!isPureNumber) {

			DecimalFormat df = new DecimalFormat("#0");

			return df.format(price);
		} else {
			return String.valueOf((int) (price));
		}
	}

	/**
	 * 格式化字符串形式的价格，强制显示小数后两位
	 */
	public static String formatStringPrice(String f) {
		float price = 0;
		try {
			price = Float.valueOf(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		String str = df.format(price).toString();
		if (str.equals("0.00")) {
			str = str.split("\\.")[0];
		}
		return str;
	}

	/**
	 * 格式化字符串形式的价格，如果是整数，则不显示小数部分的00
	 */
	public static String formatStringPrice2(String f) {
		float price = 0;
		try {
			price = Float.valueOf(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		String str = df.format(price).toString();

		// minjie 于 2015-5-6 晚上修改（加上注释）
		// if (str.split("\\.")[1].equals("00")) {
		// str = str.split("\\.")[0];
		// }
		return str;
	}

	public static String formatFloatPrice(float price) {

		// if(price==1){//特殊情况
		// return "1.00";
		// }

		if (price < 2) {

			String temp = (float) (Math.round(price * 100)) / 100 + "";

			if (temp.length() == 3) {
				temp += "0";
			}
			return temp;
			// return (float)(Math.round(price*100))/100+"";
		}
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(price);
	}

	/**
	 * 获取#,##0.00格式的价格，如果是整数，则不显示小数部分的00
	 */
	public static String formatStringPrice3(String f) {
		float price = 0;
		try {
			price = Float.valueOf(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#,##0.00");
		String str = df.format(price).toString();
		if (str.split("\\.")[1].equals("00")) {
			str = str.split("\\.")[0];
		}
		return str;
	}

	/**
	 * 获取#,##0.00格式的价格，如果是整数，则不显示小数部分的00
	 */
	public static String formatStringPrice4(float f) {
		float price = f;
		DecimalFormat df = new DecimalFormat("#,##0.00");
		String str = df.format(price).toString();

		return str;
	}

	public static String getFormatedTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
		return sdf.format(new Date());
	}

	public static String getMobileTail(String mobile) {
		if (mobile == null || mobile.length() < 4) {
			return null;
		}

		return mobile.substring(mobile.length() - 4, mobile.length());
	}

	public static String getPresentDate(String rawDate) {
		if (rawDate == null) {
			return null;
		}

		int index = rawDate.indexOf(" ");
		if (index > 0) {
			return rawDate.substring(0, index);
		} else {
			return rawDate;
		}
	}


	public static String formateOrderNumber(String orderNumber) {
		StringBuilder builder = new StringBuilder(orderNumber);

		int whiteSpace = 0;
		for (int i = 0; i < builder.length(); i++) {
			if (i == (4 * (whiteSpace + 1) + whiteSpace)) {
				builder.insert(i, ' ');
				whiteSpace++;
			}
		}

		return builder.toString();
	}

	public static String phoneFilter(String str) {
		String regEx = "[()#;*+-]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		String result = m.replaceAll("").replace(" ", "");
		if (result.startsWith("86")) {
			result = result.substring(2);
		}

		return result;
	}

	/*
	 * 判断字符串是否只包含数字和字母
	 */
	public static boolean isLetterDigit(String str) {
		String regex = "^[a-z0-9A-Z]+$";
		return str.matches(regex);
	}

	/*
	 * 英文字符转换为中文字符
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}


	/**
	 * <PRE>
	 * 全角字符->半角字符转换   
	 * 只处理全角的空格，全角！到全角～之间的字符，忽略其他
	 * </PRE>
	 */
	public static String qj2bj(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < src.length(); i++) {
			if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
				buf.append((char) (ca[i] - CONVERT_STEP));
			} else if (ca[i] == SBC_SPACE) { // 如果是全角空格
				buf.append(DBC_SPACE);
			} else { // 不处理全角空格，全角！到全角～区间外的字符
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <PRE>
	 * 半角字符->全角字符转换   
	 * 只处理空格，!到˜之间的字符，忽略其他
	 * </PRE>
	 */
	public static String bjToqj(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代
				buf.append(SBC_SPACE);
			} else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符
				buf.append((char) (ca[i] + CONVERT_STEP));
			} else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}

	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	public static String FormatFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 检测是否有emoji表情
	 * 
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是Emoji
	 * 
	 * @param codePoint
	 *            比较的单个字符
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
}
