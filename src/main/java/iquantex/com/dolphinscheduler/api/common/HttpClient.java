package iquantex.com.dolphinscheduler.api.common;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.dolphinscheduler.command.Constant;
import iquantex.com.dolphinscheduler.pojo.Result;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author mujp
 */
public class HttpClient {
    private List<NameValuePair> parameters;
    private String url;
    private String sessionId;
    private String method;

    public HttpClient() {
    }

    public HttpClient(List<NameValuePair> parameters
            , String url, String sessionId, String method) {
        this.parameters = parameters;
        this.url = url;
        this.sessionId = sessionId;
        this.method = method;
    }

    public void submit(Result result) {
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            URI uri = new URIBuilder(Constant.URL_HEADER + this.url)
                    .setParameters(this.parameters)
                    .build();

            if (Constant.GET.equalsIgnoreCase(this.method)) {
                getting(uri, result, closeableHttpClient);
            }
            if (Constant.POST.equalsIgnoreCase(this.method)) {
                posting(uri, result, closeableHttpClient);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            result.setState(Constant.STATE_ERROR);
            result.setMsg(e.toString());
        }
    }

    public void getting(URI uri, Result result, CloseableHttpClient closeableHttpClient) {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("sessionId", this.sessionId);
            response = closeableHttpClient.execute(httpGet);
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            string2Json(content, result);
        } catch (IOException e) {
            e.printStackTrace();
            result.setState(Constant.STATE_ERROR);
            result.setMsg(e.toString());
        } finally {
            close(closeableHttpClient, response);
        }
    }

    public void posting(URI uri, Result result, CloseableHttpClient closeableHttpClient) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("sessionId", this.sessionId);
            response = closeableHttpClient.execute(httpPost);
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            string2Json(content, result);
        } catch (IOException e) {
            e.printStackTrace();
            result.setState(Constant.STATE_ERROR);
            result.setMsg(e.toString());
        } finally {
            close(closeableHttpClient, response);
        }
    }

    public void string2Json(String content, Result result) {
        try {
            result.setState(Constant.STATE_ERROR);
            result.setMsg(content);

            Object data = JSONObject.parseObject(content).get("data");
            result.setData(data == null ? "" : data.toString());

            Object code = JSONObject.parseObject(content).get("code");
            result.setState((int)code == 0 ? Constant.STATE_SUCCESS : Constant.STATE_ERROR);

            Object msg = JSONObject.parseObject(content).get("msg");
            result.setMsg(msg == null ? "" : msg.toString());

        } catch (Exception e) {
            e.printStackTrace();
            result.setState(Constant.STATE_ERROR);
            result.setMsg(e.toString());
        }


    }

    void close(CloseableHttpClient closeableHttpClient, CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (response != null) {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
