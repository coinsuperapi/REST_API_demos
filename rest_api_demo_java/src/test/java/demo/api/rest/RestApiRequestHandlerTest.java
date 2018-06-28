package demo.api.rest;
import com.alibaba.fastjson.JSONObject;
import demo.api.rest.gateway.RestApiRequestHandler;
import demo.api.rest.vo.RestRequestParam;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RestApiRequestHandlerTest {
    private static final String MARKET_DEPTH_API = "/api/v1/market/depth";
    private static final String USER_ASSET_INFO_API = "/api/v1/asset/userAssetInfo";

    /**
     * 查询深度数据
     * @throws IOException
     */
    @Test
    public void testGetMarketDepth() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        //组装data参数
        Map<String,String> data = new HashMap<String, String>();
        data.put("symbol","ETC/BTC");
        //生成请求参数+自动组装common参数
        RestRequestParam param = RestApiRequestHandler.generateRequestParam(data);
        //发起API调用请求并接收返回结果(返回结果为json字符串，可自行转成json对象获取需要的信息)
        String response = RestApiRequestHandler.handleApiPost(MARKET_DEPTH_API, param);
        System.out.println(response);
    }

    /**
     * 查询深度数据（自动封装请求参数）
     * @throws IOException
     */
    @Test
    public void testGetMarketDepthSimple() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        //组装data参数
        Map<String,String> data = new HashMap<String, String>();
        data.put("symbol","ETC/BTC");
        String response = RestApiRequestHandler.handleApiPostSimple(MARKET_DEPTH_API, data);
        System.out.println(response);
    }

    /**
     * 查询API用户的个人资产信息
     * @throws IOException
     */
    @Test
    public void testGetUserAssetInfo() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        RestRequestParam<Map<String, String>> param = RestApiRequestHandler.generateRequestParam(null);
        System.out.println(JSONObject.toJSONString(param));
        String response = RestApiRequestHandler.handleApiPost(USER_ASSET_INFO_API, param);
        System.out.println(response);
    }

    /**
     * 查询API用户的个人资产信息（自动封装请求参数）
     * @throws IOException
     */
    @Test
    public void testGetUserAssetInfoSimple() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String response = RestApiRequestHandler.handleApiPostSimple(USER_ASSET_INFO_API, null);
        System.out.println(response);
    }

}