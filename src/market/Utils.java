package market;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static final String BASE_URL = "http://api.zb.live/data/v1/ticker?market=%s_usdt";

    public static String getPrice() throws Exception {
        String url = String.format(BASE_URL, PerformanceWatcherForm.coinName.toLowerCase());
        String result = doGet(url);
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(result, JsonObject.class);
        return obj.getAsJsonObject("ticker").get("last").getAsString();
    }

    private static String doGet(String httpUrl) throws Exception {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp;
                    while ((temp = br.readLine()) != null) {
                        result.append(temp);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
            connection.disconnect();
        }
        return result.toString();
    }
}
