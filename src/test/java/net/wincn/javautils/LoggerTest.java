/*	
 * logger 的级别
 * SEVERE（最高值）
 * WARNING
 * INFO
 * CONFIG
 * FINE
 * FINER
 * FINEST（最低值）
 */
package net.wincn.javautils;

import java.util.logging.Logger;

import net.wincn.utils.logger.LoggerUtils;

import org.junit.Test;

public class LoggerTest {
	static Logger logger = Logger.getLogger("loggerTest");

	@Test
	public void loggerTest() {
		LoggerUtils.getLogger("dd", "dd");
	}
}
