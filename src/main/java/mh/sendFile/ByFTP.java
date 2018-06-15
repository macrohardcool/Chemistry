package mh.sendFile;

import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

public class ByFTP {
    private String userID;
    private String userPassword;
    private String ftpServerIP;
    private String uploadFileName;
    private FileInputStream fis;

    public ByFTP(String ftpServerIP, String userID, String userPassword, String uploadFileName, FileInputStream fis) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.ftpServerIP = ftpServerIP;
        this.uploadFileName = uploadFileName;
        this.fis = fis;
    }

    public void uploadFile() throws IOException {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(this.ftpServerIP);
            ftpClient.login(this.userID, this.userPassword);

            //設置上傳目錄
            //ftpClient.changeWorkingDirectory("/admin/pic");
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            //設置檔案類型（二進位）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile(this.uploadFileName, fis);
        } catch (IOException e) {
            System.out.println("FTP用戶端出錯！");
            e.printStackTrace();
        } finally {
            if(fis != null) fis.close();
            try {
                ftpClient.disconnect();
            }
            catch (IOException e)
            {
                System.out.println("關閉FTP連接發生異常！");
                e.printStackTrace();
            }

        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFtpServerIP() {
        return ftpServerIP;
    }

    public void setFtpServerIP(String ftpServerIP) {
        this.ftpServerIP = ftpServerIP;
    }

    public FileInputStream getFis() {
        return fis;
    }

    public void setFis(FileInputStream fis) {
        this.fis = fis;
    }

}
