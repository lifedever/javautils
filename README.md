Java 各种项目中可能用到的工具类
==
# SystemProperty.java
提供java读取操作系统各种参数的示例

# FtpClientUtils.java
提供对ftp上传下载的封装，功能包括：
### uploadFile(InputStream input, String filename)
* input：文件流
* filename：远程文件名称（可包含ftp相对路径）

通过文件流上传一个文件到ftp服务器

### downFiles(String remotePath, String localPath)
* remotePath：远程路径
* localPath：本地路径

下载一个远程目录到本地

### moveFile(String from, String to)
* form：源文件
* to：目标文件夹

移动目标文件到一个指定的文件夹中

### moveDir(String src, String des)
* src：源目录
* des：目标目录

移动整个目录到目标目录

### deleteFile(String remoteFile)
* remoteFile：远程文件

删除一个远程文件

