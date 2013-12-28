package net.wincn.utils.string;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务中常用的字符串处理工具类
 */
public class StrUtils {

	/**
	 * 将字符用双引号包括
	 * 
	 * @param string
	 *            字符串
	 * @return 添加双引号后的字符
	 */
	public static String quoteString(String string) {
		return new StringBuffer().append("\"").append(string).append("\"").toString();
	}

	/**
	 * 用指定字符 将传入的字符串包括
	 * 
	 * @param string
	 *            字符串
	 * @param c1
	 *            左字符
	 * @param c2
	 *            右字符
	 * @return String
	 */
	public static String quoteString(String string, char c1, char c2) {
		return new StringBuffer().append(c1).append(string).append(c2).toString();
	}

	/**
	 * 用指定字符 将传入的字符串包括
	 * 
	 * @param string
	 *            字符串
	 * @param c
	 *            包括字符 如（单引号，双引号）;
	 * @return String
	 */
	public static String quoteString(String string, char c) {
		return new StringBuffer().append(c).append(string).append(c).toString();
	}

	public static String quoteString(String string, String c) {
		return new StringBuffer().append(c).append(string).append(c).toString();
	}

	public static String split(Long[] array) {
		String s = "";
		if (array != null && array.length > 0) {
			for (Long v : array) {
				s += "," + v.toString();
			}
			;
			s = s.replaceFirst(",", "");
		}
		return s;

	}

	/**
	 * 获取字符串文件路径 (已放弃，建议使用FilenameUtils)
	 * 
	 * @param fileName
	 * @param delimiter
	 * @return
	 */
	@Deprecated
	public static String getFilePath(String fileName) {
		int pos = fileName.lastIndexOf(File.pathSeparator);
		if (pos != -1)
			return fileName.substring(0, pos);
		else
			return fileName;
	}

	/**
	 * 获取文件后缀 (已放弃，建议使用FilenameUtils)
	 * 
	 * @param fileName
	 * @return
	 */
	@Deprecated
	public static String getFileExtension(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		return fileType;
	}

	public static String changeFileExtension(String filepath, String newExtension) {
		String filename = filepath.substring(0, filepath.lastIndexOf("."));
		if (!newExtension.contains(".")) {
			newExtension = "." + newExtension;
		}
		return filename + newExtension;
	}

	/**
	 * 将字符串的首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperFirst(String str) {
		String result = str;
		if (str != null) {
			result = str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return result;
	}

	/**
	 * 将字符串的首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerFirst(String str) {
		String result = str;
		if (str != null) {
			result = str.substring(0, 1).toLowerCase() + str.substring(1);
		}
		return result;
	}

	/**
	 * 将单词中的大写字母替换成下划线加小写字母 例如: ArchiveInfo -> archive_info
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceUpperToUnderline(String str) {
		char[] chars = str.toCharArray();
		String outs = "";
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isUpperCase(c)) {
				char lc = Character.toLowerCase(c);
				if (i == 0) {
					if (chars.length > 1 && Character.isUpperCase(chars[i + 1])) {
						outs += c;
					} else {
						outs += lc;
					}
				} else if (i > 0 && i < chars.length - 1) {
					// 只有该字母两边都是小写字母时才变小写，否则保持大写
					if (Character.isLowerCase(chars[i - 1]) && Character.isLowerCase(chars[i + 1])) {
						outs += '_';
						outs += lc;
					} else {
						// 如果该字母的前字母是小写，则加前缀下划线
						if (Character.isLowerCase(chars[i - 1])) {
							outs += '_';
							outs += c;
						}
						// 如果该字母的后字母是小写，则加后缀下划线
						else if (Character.isLowerCase(chars[i + 1])) {
							outs += c;
							outs += '_';
						}

					}
				} else if (i == chars.length - 1) {
					if (chars.length > 1 && Character.isUpperCase(chars[i - 1])) {
						outs += c;
					} else {
						outs += lc;
					}
				}

			} else {
				outs += c;
			}
		}

		return outs;
	}

	/**
	 * 将单词中的下划线及后面的字母替换成大写字母 例如 archive_info -> ArchiveInfo
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceUnderlineToUpper(String str) {
		char[] chars = str.toCharArray();
		String outs = "";
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				if (i < chars.length - 1) {
					char uc = Character.toUpperCase(chars[i + 1]);
					outs += uc;
				}

			} else {
				if (i == 0 || chars[i - 1] != '_') {
					outs += c;
				}

			}
		}

		return outs;
	}

	/**
	 * 用于替换有开始结束符指定的大段文本
	 * 
	 * @param strSource
	 *            原文本
	 * @param startFlag
	 *            开始标识(正则表达式)
	 * @param endFlag
	 *            结束标识（正则表达式）
	 * @param replaceStr
	 *            替换文本
	 * @param lineBreak
	 *            文本是否以换行形式存在
	 * @return 替换后的文本
	 */
	public static String replaceByFlag(String strSource, String startFlag, String endFlag, String replaceStr, boolean lineBreak) {

		String reg = "(" + startFlag + ").*(" + endFlag + ")";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(strSource);
		String g1 = lineBreak ? "$1\r" : "$1";
		String g2 = lineBreak ? "\r$2" : "$2";

		String result = matcher.replaceAll(g1 + replaceStr + g2);
		return result;

	}

	public static String substringByFlag(String strSource, String startFlag, String endFlag) {
		String subString = null;
		String reg = startFlag + "(.*)" + endFlag;
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(strSource);
		if (matcher.find()) {
			subString = matcher.group(1);
		}
		return subString;
	}

	/**
	 * 将数字格式化成固定长度的字符串，不足长度的部分用0替换。例如： 15 -> 00015
	 * 
	 * @param d
	 *            要格式化的数字
	 * @param length
	 *            字符串长度
	 * @return
	 */
	public static String formatNumber(int d, int length) {
		String s = String.format("%0" + length + "d", d);
		return s;
	}

	/**
	 * 将字符串数组转化成Integer数组
	 */
	public static Integer[] parseStrArrayToInt(String[] args) {
		Integer intArr[] = new Integer[args.length];
		for (int i = 0; i < args.length; i++) {
			intArr[i] = Integer.parseInt(args[i]);
		}
		return intArr;
	}

	/**
	 * 将字符串数组转化成Long数组
	 */
	public static Long[] parseStrArrayToLong(String[] args) {
		Long intArr[] = new Long[args.length];
		for (int i = 0; i < args.length; i++) {
			intArr[i] = Long.parseLong(args[i]);
		}
		return intArr;
	}
	
	  /**
	   * 过滤无效字符
	   * @param in
	   * @return
	   */
	  public String stripNonValidXMLCharacters(String in) {
	      StringBuffer out = new StringBuffer(); // Used to hold the output.
	      char current; // Used to reference the current character.

	      if (in == null || ("".equals(in)))
	          return ""; // vacancy test.
	      for (int i = 0; i < in.length(); i++) {
	          current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
	                                  // here; it should not happen.
	          if ((current == 0x9) || (current == 0xA) || (current == 0xD)
	                  || ((current >= 0x20) && (current <= 0xD7FF))
	                  || ((current >= 0xE000) && (current <= 0xFFFD))
	                  || ((current >= 0x10000) && (current <= 0x10FFFF)))
	              out.append(current);
	      }
	      return out.toString();
	  }
}
