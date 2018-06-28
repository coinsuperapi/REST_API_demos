package demo.api.rest.gateway;

import com.alibaba.fastjson.JSONObject;
import demo.api.rest.common.RestCommonUtil;
import demo.api.rest.common.RestHttpClient;
import demo.api.rest.common.RestSignUtil;
import demo.api.rest.vo.RestRequestParam;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * API请求处理器（含API服务端配置）
 * @author Lynn Li
 */
public class RestApiRequestHandler {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private static final String CHARSET = "UTF-8";
    private static final String URI_SPLIT = "/";

    private static final String REST_SCHEME = "http";
    private static final String REST_HOST = "localhost";
    private static final String REST_PORT = "80";
    private static final String REST_URL_PREFIX = REST_SCHEME +"://"+ REST_HOST +":"+ REST_PORT;
    /**密钥对，请自行申请并妥善保管（注：正式环境是不同的）*/
    private static final String ACCESS_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String SECRET_KEY = "YYYYYYYYYYYYYYYYYYYYYYYYYYYY";

    /**
     * 发送API POST请求
     * @param apiUri
     * @param param
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> String handleApiPost(String apiUri, RestRequestParam<T> param) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        boolean isUriValid = isUriValid(apiUri);
        if(!isUriValid){
            return "API uri is invalid, uri mast start with \"/\"";
        }
        String paramData = JSONObject.toJSONString(param);
        String apiUrl = generateRestApiUrl(apiUri);
        return RestHttpClient.post(apiUrl,paramData,CONNECT_TIMEOUT,READ_TIMEOUT,CHARSET);
    }

    /**
     * 发送API POST请求(自动封装请求参数)
     * @param apiUri
     * @param data
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> String handleApiPostSimple(String apiUri, T data) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        RestRequestParam<T> param = generateRequestParam(data);
        boolean isUriValid = isUriValid(apiUri);
        if(!isUriValid){
            return "API uri is invalid, uri mast start with \"/\"";
        }
        String paramData = JSONObject.toJSONString(param);
        String apiUrl = generateRestApiUrl(apiUri);
        return RestHttpClient.post(apiUrl,paramData,CONNECT_TIMEOUT,READ_TIMEOUT,CHARSET);
    }

    /**
     * 封装请求参数
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestRequestParam<T> generateRequestParam(T data) {
        RestRequestParam<T> param = new RestRequestParam<T>();
        param.setData(data);
        RestRequestParam.Common common = param.newCommon(ACCESS_KEY,System.currentTimeMillis());
        String sign = RestSignUtil.generateSign(param, SECRET_KEY);
        common.setSign(sign);
        return param;
    }

    /**
     * 返回是否是有效的apiUri
     * @param apiUri
     * @return
     */
    private static boolean isUriValid(String apiUri) {
        return RestCommonUtil.isNotEmpty(apiUri)&&apiUri.startsWith(URI_SPLIT);
    }

    /**
     * 生成请求url
     * @param apiUri
     * @return
     */
    private static String generateRestApiUrl(String apiUri){
        String validUrl = REST_URL_PREFIX;
        if(RestCommonUtil.isNotEmpty(apiUri)&&!apiUri.startsWith(URI_SPLIT)){
            validUrl += URI_SPLIT + apiUri;
        }else if(RestCommonUtil.isNotEmpty(apiUri)){
            validUrl += apiUri;
        }
        return validUrl;
    }
}