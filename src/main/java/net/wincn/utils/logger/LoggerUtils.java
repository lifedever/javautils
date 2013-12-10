package net.wincn.utils.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 日志的封装
 * 
 * @author gefangshuai
 * @email gefangshuai@163.com
 * @createDate 2013年12月10日 下午2:02:21
 */
public class LoggerUtils extends Logger {
	static Logger logger = Logger.getLogger("logger");

	protected LoggerUtils(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

	public static void main(String[] args) {
		// logger.setLevel(Level.WARNING);

		try {
			FileHandler handler = new FileHandler("d:/javautils.log");// 日志输出到文本中
			handler.setFormatter(new Formatter() {// 设置日志格式
				@Override
				public String format(LogRecord record) {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getMillis()) + "\t" + record.getSourceClassName() + "\t"
							+ record.getSourceMethodName() + "\n" + record.getLevel() + ":" + record.getMessage() + "\n";
				}
			});
			logger.addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("我的日志信息");
		logger.warning("我的日志警告");
	}
}
