package  mh;

import mh.sendFile.ByEMail;
import mh.sendFile.ByFTP;
import mh.sendFile.ByHTTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;

public class chemicalDataProcess {

    private String emailSubject = "Please add some real message for this e-mail !!";
    private String bodyMessage = "Please add some real message for this e-mail !!";

    public static void main(String[] args)
    {
        try
        {
            chemicalDataProcess dataProcess = new chemicalDataProcess();

            File uploadFile = new File("build.gradle");
            FileInputStream fis = new FileInputStream(uploadFile);

            /*This is for FTP
                dataProcess.sendByFTP("192.168.","","",uploadFile.getName(),fis);
            */

            /* this is for Email
            dataProcess.emailSubject = "The Subject Test";
            dataProcess.bodyMessage = "Body Message";
            dataProcess.sendByEMail("","587","","","","",uploadFile.getName(),fis);
            */

            // this is for HTTP
               dataProcess.sendByHTTP("http://localhost:8080/houseTX/login.action","","userId","","password",uploadFile.getName(),fis);

             //


            fis.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void sendByFTP(String ftpServerIP, String userID, String userPassword, String uploadFileName, FileInputStream fis)
    {
        try
        {
            ByFTP ftpclient =new ByFTP(ftpServerIP,userID,userPassword,uploadFileName,fis);
            System.out.println("*******************************");
            System.out.println("開始 FTP 上傳資料~~");
            ftpclient.uploadFile();
            System.out.println("使用 FTP 上傳");
            System.out.println("上傳時間：" + new Timestamp(System.currentTimeMillis()));
            System.out.println("上傳IP：" + ftpServerIP);
            System.out.println("使用者：" + userID);
            System.out.println("檔名：" + uploadFileName);
            System.out.println("上傳成功 !!");
            System.out.println("*******************************");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendByEMail(String smtpIP,String smtpPort, String smtpUserName, String smtpPassword, String mailTo, String mailFrom, String attchFilename, FileInputStream fis)
    {
        try
        {
            ByEMail eMailclient =new ByEMail(smtpIP,smtpPort,smtpUserName,smtpPassword,mailTo,mailFrom,attchFilename,fis);

            eMailclient.setEmailSubject(this.emailSubject);
            eMailclient.setBodyMessage(this.bodyMessage);

            System.out.println("*******************************");
            System.out.println("開始 E-Mail 寄送資料~~");
            eMailclient.sendEmail();
            System.out.println("使用 E-Mail 上傳");
            System.out.println("上傳時間：" + new Timestamp(System.currentTimeMillis()));
            System.out.println("上傳給：" + mailTo);
            System.out.println("檔名：" + attchFilename);
            System.out.println("E-Mail寄送成功 !!");
            System.out.println("*******************************");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendByHTTP(String webURL, String loginName, String webLoginNameID, String loginPassword,String webLoginPasswordID,String uploadFileName, InputStream is)
    {
        try
        {
            ByHTTP httpClient =new ByHTTP(webURL, loginName, webLoginNameID, loginPassword,webLoginPasswordID,uploadFileName, is);

            System.out.println("*******************************");
            System.out.println("開始 HTTP 上傳資料~~");
            httpClient.uploadFile2Web();
            System.out.println("使用 HTTP 上傳");
            System.out.println("上傳時間：" + new Timestamp(System.currentTimeMillis()));
            System.out.println("上傳給：" + webURL);
            System.out.println("檔名：" + uploadFileName);
            System.out.println("HTTP寄送成功 !!");
            System.out.println("*******************************");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
