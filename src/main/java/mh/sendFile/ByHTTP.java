package mh.sendFile;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class ByHTTP {
    private String webURL;
    private String loginName;
    private String webLoginNameID;
    private String loginPassword;
    private String webLoginPasswordID;
    private String uploadFileName;
    private InputStream is;

    public ByHTTP(String webURL, String loginName,String webLoginNameID, String loginPassword,String webLoginPasswordID,String uploadFileName, InputStream is) {
        this.webURL = webURL;
        this.loginName = loginName;
        this.webLoginNameID = webLoginNameID;
        this.loginPassword = loginPassword;
        this.webLoginPasswordID = webLoginPasswordID;
        this.uploadFileName = uploadFileName;
        this.is = is;
    }

    public void uploadFile2Web() throws Exception
    {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();


        try {

            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI(this.webURL))
                    .addParameter(this.webLoginNameID, this.loginName)
                    .addParameter(this.webLoginPasswordID, this.loginPassword)
                    .build();
            CloseableHttpResponse response = httpclient.execute(login);
            try {
                HttpEntity entity = response.getEntity();


                if(response.getStatusLine().getStatusCode() == 302)
                {
                    HttpPost redirect = new HttpPost(response.getFirstHeader("Location").getValue());

                    response = httpclient.execute(redirect);
                    entity = response.getEntity();
                }
                EntityUtils.consume(entity);

                //檔案上傳fileUploadAction.action
                HttpEntity data = MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addBinaryBody("myFile", is, ContentType.IMAGE_JPEG, this.uploadFileName)
                        .addTextBody("text", "uploadTest", ContentType.DEFAULT_BINARY)
                        .build();

                // build http request and assign multipart upload data
                HttpUriRequest request = RequestBuilder
                        .post("http://localhost:8080/houseTX/fileUploadAction.action")
                        .setEntity(data)
                        .build();
                ResponseHandler<String> responseHandler = responseUpload -> {
                    int status = responseUpload.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entityUpload = responseUpload.getEntity();
                        return entityUpload != null ? EntityUtils.toString(entityUpload) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                };
                String responseBody = httpclient.execute(request, responseHandler);
                System.out.println(responseBody);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

    }


}
