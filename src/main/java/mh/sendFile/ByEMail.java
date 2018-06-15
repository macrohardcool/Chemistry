package mh.sendFile;

import org.apache.commons.io.IOUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ByEMail {
    private String smtpIP;
    private String smtpPort;
    private String smtpUserName;
    private String smtpPassword;
    private String mailTo;
    private String mailFrom;
    private String attchFilename;
    private InputStream fis;
    private String emailSubject = "Please add some real message for this e-mail !!";
    private String bodyMessage = "Please add some real message for this e-mail !!";


    public ByEMail(String smtpIP, String smtpPort, String smtpUserName, String smtpPassword, String mailTo, String mailFrom, String attchFilename, InputStream fis) {

        this.smtpIP = smtpIP;
        this.smtpPort = smtpPort;
        this.smtpUserName = smtpUserName;
        this.smtpPassword = smtpPassword;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.attchFilename = attchFilename;
        this.fis = new BufferedInputStream(fis);
    }

    public void sendEmail() throws Exception
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.smtpIP);
        props.put("mail.smtp.port", this.smtpPort);
        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUserName, smtpPassword);
                    }
                });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailFrom));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(this.mailTo));
            message.setSubject(this.emailSubject);

            Multipart multipart = new MimeMultipart();

            //建立訊息區
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(this.bodyMessage);

            //建立附件檔區
            byte[] fileByteArray = IOUtils.toByteArray(this.fis);
            byte[] fileBase64ByteArray = java.util.Base64.getEncoder().encode(fileByteArray);
            InternetHeaders fileHeaders = new InternetHeaders();
            fileHeaders.setHeader("Content-Type", "application/octet-stream" + "; name=\"" + attchFilename + "\"");
            fileHeaders.setHeader("Content-Transfer-Encoding", "base64");
            fileHeaders.setHeader("Content-Disposition", "attachment; filename=\"" + attchFilename + "\"");
            MimeBodyPart attchBodyPart = new MimeBodyPart(fileHeaders,fileBase64ByteArray);
            attchBodyPart.setFileName(this.attchFilename);

            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attchBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSmtpIP() {
        return smtpIP;
    }

    public void setSmtpIP(String smtpIP) {
        this.smtpIP = smtpIP;
    }

    public String getSmtpUserName() {
        return smtpUserName;
    }

    public void setSmtpUserName(String smtpUserName) {
        this.smtpUserName = smtpUserName;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getAttchFilename() {
        return attchFilename;
    }

    public void setAttchFilename(String attchFilename) {
        this.attchFilename = attchFilename;
    }

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

}
