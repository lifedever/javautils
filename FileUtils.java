package net.wincn.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.wincn.utils.exception.UtilsException;

/**
 * 文件工具类
 * 
 * @project wincn-utils
 * @author gefangshuai
 * @email gefangshuai@163.com
 * @createDate 2013-6-25 下午4:13:40
 */
public final class FileUtils {

	/**
	 * 根据路径规则，列出目录下的所有文件，如：C盘一级目录 C:/ ；C盘根目录，带递归：C:/**
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> listFiles(String path) {
		List<File> files = new ArrayList<File>();
		path = path.replace("\\", "/");
		if (path.contains("/*")) {// c:/**/*.txt
			String[] strArr = path.split("\\*\\*");
			String regex = "";
			if (strArr.length == 2) {
				regex = joinRegex(strArr[1]);
			} else if (strArr.length > 2) {
				throw new UtilsException("路径规则有误，请检查是否出现两组'**'！");
			}
			System.out.println("regex:" + regex);
			Pattern pattern = Pattern.compile(regex);
			reListFiles(new File(strArr[0]), files, pattern);
		} else {
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					listFiles(file, files);
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 * 处理正则表达式
	 * 
	 * @param str
	 * @return
	 */
	private static String joinRegex(String str) {
		str = str.replace("/", "");
		String[] r = str.split("\\*");
		StringBuffer sb = new StringBuffer();
		if (!str.contains("*")) {
			sb.append("^").append(r[0]).append("$");
		} else {

			if (r.length == 1) {
				if (str.contains("*")) {
					sb.append("^").append(r[0]);
				}
			} else if (r.length == 2) {
				if (("").equals(r[0])) {
					sb.append(r[1]).append("$");
				} else {
					sb.append("^").append(r[0]).append("+").append(r[1]).append("$");
				}
			} else {
				throw new UtilsException("路径规则有误！");
			}
		}
		return sb.toString().replace(".", "\\.");
	}

	/**
	 * 列出目录下的所有非隐藏并且有权限读取的文件
	 * 
	 * @param dirFile
	 * @return
	 */
	public static File[] listFiles(File dirFile) {
		return dirFile.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return file.list() != null;
				}
				return !file.isHidden();
			}
		});
	}

	/**
	 * 返回一级目录下的所有文件
	 * 
	 * @param dirFile
	 * @param files
	 * @return
	 */
	public static List<File> listFiles(File dirFile, List<File> files) {
		File[] tempFiles = listFiles(dirFile);
		for (File file : tempFiles) {
			if (file.isFile()) {
				files.add(file);
			}
		}
		return null;
	}

	/**
	 * 递归返回目录下的所有文件
	 * 
	 * @param dirFile
	 * @param files
	 */
	public void reListFiles(File dirFile, List<File> files) {
		if (dirFile.isDirectory()) {
			File[] tempFiles = listFiles(dirFile);
			for (File file : tempFiles) {
				if (file.isDirectory()) {
					reListFiles(file, files);
				} else {
					files.add(file);
				}
			}
		}
	}

	/**
	 * 递归返回目录下的所有文件，有正则规则
	 * 
	 * @param dirFile
	 * @param files
	 * @param pattern
	 */
	public static void reListFiles(File dirFile, List<File> files, Pattern pattern) {
		if (dirFile.isDirectory()) {
			File[] tempFiles = listFiles(dirFile);
			for (File file : tempFiles) {
				if (file.isDirectory()) {
					reListFiles(file, files, pattern);
				} else {
					if (pattern.matcher(file.getName()).find()) {
						files.add(file);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// String path = "F:/*\\\\";
		// Pattern pattern = Pattern.compile(".*");
		// for (File file : FileUtils.listFiles(path.replace("*", ""))) {
		// Matcher matcher = pattern.matcher(file.getAbsolutePath());
		// if (matcher.find()) {
		// System.out.println(file.getAbsolutePath());
		// }
		// }

		// com/app.*$

		// com/app/.*$
		// Pattern pattern = Pattern.compile(".pdf$");
		// Matcher matcher = pattern.matcher(path);
		// if (matcher.find()) {
		// System.out.println(path);
		// }
		String[] arr = "qwww".split("3");
		System.out.println(arr.length);
		for (String str : arr) {
			System.out.println(str);
		}
		System.out.println("c:/**/*.txt".split("\\*").toString());
	}
}
