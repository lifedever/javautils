import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Ftp上传下载工具类
 * @author gefangshuai
 * @email gefangshuai@163.com
 * @createDate 2013年12月4日 下午6:17:47
 */
public class FtpClientUtils {

	private String username;
	private String password;
	private String url;
	private int port;
	private FTPClient ftpClient;

	/**
	 * 获得链接
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean connect() throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}
		boolean success = false;
		ftpClient = new FTPClient();
		int reply;
		ftpClient.connect(url, port);// 连接FTP服务器
		// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		success = ftpClient.login(username, password);// 登录
		ftpClient.configure(new FTPClientConfig(ftpClient.getSystemType()));
		ftpClient.setBufferSize(100000); // 不设置的话为1024
		if ("linux".equals(System.getProperties().getProperty("os.name").toLowerCase())) {
			ftpClient.setControlEncoding("utf-8");
		} else {
			ftpClient.setControlEncoding("GBK");
		}
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			return success;
		}
		return success;
	}

	/**
	 * 断开连接
	 */
	private void disconnect() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException ioe) {
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param input
	 *            文件流
	 * @param filename
	 *            远程服务器文件名称
	 * @return
	 */
	public boolean uploadFile(InputStream input, String filename) {
		try {
			if (!connect())
				return false;
			File file = new File(filename);
			String parent = file.getParent();
			if (!ftpClient.changeWorkingDirectory(parent)) {
				if (!ftpClient.makeDirectory(parent)) {
					return false;
				}
			}
			ftpClient.changeWorkingDirectory("/");
			ftpClient.storeFile(filename, input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
		return true;
	}

	/**
	 * 下载目录中的文件
	 * 
	 * @param remotePath
	 * @param localPath
	 * @return
	 */
	public boolean downFiles(String remotePath, String localPath) {
		localPath = localPath.endsWith("/") ? localPath : localPath + "/";
		File tmp = new File(localPath);
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		try {
			if (!connect())
				return false;
			this.recursiveDownLoad(ftpClient, localPath, remotePath);
			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return true;
	}

	/**
	 * 递归下载
	 * 
	 * @author gefangshuai
	 * @param ftpClient
	 * @param localPath
	 * @param remotePath
	 */
	private void recursiveDownLoad(FTPClient ftpClient, String localPath, String remotePath) {
		try {
			FTPFile[] fs = ftpClient.listFiles(remotePath, new FTPFileFilter() {

				@Override
				public boolean accept(FTPFile f) {
					return !f.getName().endsWith(".hlsq");
				}
			});
			for (FTPFile file : fs) {
				ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
				if (file.isDirectory()) {
					String tempLocal = localPath + file.getName();
					this.recursiveDownLoad(ftpClient, tempLocal, file.getName());
				} else {
					// File localDir = new File(localPath);
					// if(!localDir.exists())
					// localDir.mkdirs();
					// File localFile = new File(localPath + "/" +
					// file.getName());
					File localFile = new File(localPath + file.getName());
					OutputStream out = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), out);
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动远程文件到指定文件夹
	 */
	public boolean moveFile(String from, String to) {
		try {
			if (!connect()) {
				return false;
			}
			if (!isExist(from)) {
				disconnect();
				return false;
			}
			File file = new File(to);
			String parent = file.getParent();
			ftpClient.makeDirectory(parent);
			ftpClient.rename(from, to);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return false;
	}

	/**
	 * 移动整个目录中的文件到另一个目录
	 * 
	 * @param src
	 * @param des
	 * @return
	 */
	public boolean moveDir(String src, String des) {
		List<FileForFTP> fileForFTPs = new ArrayList<FtpClientUtils.FileForFTP>();
		fileForFTPs = this.getFiles(fileForFTPs, src);
		for (FileForFTP fileForFTP : fileForFTPs) {
			this.moveFile(fileForFTP.getFilePath() + "/" + fileForFTP.getFtpFile().getName(), fileForFTP.getFilePath().replace(src, des) + "/" + fileForFTP.getFtpFile().getName());
		}
		return true;
	}

	/**
	 * 递归列出所有文件
	 * 
	 * @author gefangshuai
	 * @param directory
	 * @return
	 */
	private List<FileForFTP> getFiles(List<FileForFTP> fileForFTPs, String directory) {
		FTPFile[] files = null;
		try {
			if (!connect()) {
				return null;
			}
			files = ftpClient.listFiles(directory, new FTPFileFilter() {
				@Override
				public boolean accept(FTPFile f) {
					return !f.getName().endsWith(".hlsq");
				}
			});
			for (FTPFile file : files) {
				if (file.isDirectory()) {
					ftpClient.changeWorkingDirectory(file.getName());// 转移到FTP服务器目录
					this.getFiles(fileForFTPs, directory + "/" + file.getName());
				} else {
					FileForFTP fileForFTP = new FileForFTP();
					fileForFTP.setFilePath(directory);
					fileForFTP.setFtpFile(file);
					fileForFTPs.add(fileForFTP);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileForFTPs;
	}

	/**
	 * 删除一个远程文件String file
	 * 
	 * @param remoteFile
	 *            远程文件名(包括完整路径)
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteFile(String remoteFile) {
		try {
			if (!connect()) {
				return false;
			}
			ftpClient.deleteFile(remoteFile);
			disconnect();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnect();
		return false;
	}

	private boolean isExist(String file) throws IOException {
		int count = ftpClient.listFiles(file).length;
		if (count == 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param server
	 *            ftp地址
	 * @param port
	 *            ftp端口
	 * 
	 */
	public FtpClientUtils(String username, String password, String url, int port) {
		this.username = username;
		this.password = password;
		this.url = url;
		this.port = port;
	}

	/**
	 * 用于ftp传输的封装的文件
	 * 
	 * @author gefangshuai
	 * @email gefangshuai@163.com
	 * @createDate 2013年11月13日 下午3:09:15
	 */
	class FileForFTP {
		private String filePath;
		private FTPFile ftpFile;

		public String getFilePath() {
			return filePath;
		}

		public FTPFile getFtpFile() {
			return ftpFile;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public void setFtpFile(FTPFile ftpFile) {
			this.ftpFile = ftpFile;
		}

	}
}
