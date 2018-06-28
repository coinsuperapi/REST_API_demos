using System;
using System.Collections.Generic;
using System.Security.Cryptography;
using System.Text;
using RestSharp;
/// <summary>
/// rest api相关工具 by Lynn Li
/// </summary>
namespace RestDemo
{
    class ApiUtil
    {

        private const string API_HOST = "http://localhost";
        private const string API_URL_PREFIX = API_HOST;

        private const string ACCESS_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        private const string SECRET_KEY = "YYYYYYYYYYYYYYYYYYYYYYYYYYYY";

        private static readonly long DATE_TIME_1970 = DateTime.Parse("1970-1-1").Ticks;

        /// <summary>
        /// 发送api post请求
        /// </summary>
        /// <param name="api">接口路径</param>
        /// <param name="dataDic">post数据的data部分</param>
        /// <returns>响应的字符串结果</returns>
        public static string SendPost(string api, Dictionary<string, string> dataDic)
        {
            return SendPostRequest(api,dataDic,Method.POST);
        }

        /// <summary>
        /// 发送api get请求
        /// </summary>
        /// <param name="api">接口路径</param>
        /// <returns>响应的字符串结果</returns>
        public static string SendGet(string api)
        {
            return DoSend(api, null, Method.GET);
        }

        /// <summary>
        /// 发送请求
        /// </summary>
        /// <param name="api">接口路径</param>
        /// <param name="dataDic">post数据的data部分</param>
        /// <param name="method">请求方式</param>
        /// <returns>响应的字符串结果</returns>
        private static string SendPostRequest(string api,Dictionary<string, string> dataDic, Method method)
        {
            var commonDic = GetCommon();
            if (dataDic == null)
            {
                dataDic = new Dictionary<string, string>(0);
            }
            var sign = GetSign(commonDic,dataDic);
            commonDic.Add("sign",sign);
            var postJson = new Dictionary<string,Object>();
            postJson.Add("common",commonDic);
            postJson.Add("data", dataDic);
            return DoSend(api,postJson,method);
        }

        /// <summary>
        /// 执行发送请求
        /// </summary>
        /// <param name="api">接口路径</param>
        /// <param name="jsonData">post数据的data部分</param>
        /// <param name="method">请求方式</param>
        /// <returns>响应的字符串结果</returns>
        private static string DoSend(string api,Dictionary<string,Object> jsonData,Method method)
        {
            RestClient client = new RestClient(API_HOST);
            var url = API_URL_PREFIX + api;
            var request = new RestRequest(url, method);
            if (jsonData != null)
            {
                request.AddJsonBody(jsonData);
            }
            return client.Execute(request).Content;
        }

        /// <summary>
        /// 获取common Dic
        /// </summary>
        /// <returns>common参数</returns>
        private static Dictionary<string, string> GetCommon()
        {
            var commonDic = new Dictionary<string,string>(3);
            commonDic.Add("accesskey", ACCESS_KEY);
            commonDic.Add("timestamp", Convert.ToString((DateTime.Now.Ticks - DATE_TIME_1970) / 10000));
            return commonDic;
        }

        /// <summary>
        /// Uri参数值进行转义
        /// </summary>
        /// <param name="common">传参common部分</param>
        /// <param name="data">传参data部分</param>
        /// <returns>sign签名</returns>
        private static string GetSign(Dictionary<string, string> common, Dictionary<string, string> data)
        {
            //检查common不能为空
            if (common == null)
            {
                throw new ArgumentException("common cannot be null");
            }
            var signSourceBuilder = new StringBuilder();
            //初始化 参数有序dic
            var sortDic = new SortedDictionary<string, string>();
            //将common参数放入有序dic
            foreach (var commonPair in common)
            {
                sortDic.Add(commonPair.Key, commonPair.Value);
            }
            //将data参数放入有序dic
            if (data!=null)
            {
                foreach (var dataPair in data)
                {
                    sortDic.Add(dataPair.Key, dataPair.Value);
                }
            }
            //将sign移出有序dic
            sortDic.Remove("sign");
            //将secret_sign加入有序dic
            sortDic.Add("secretkey", SECRET_KEY);
            //将有序dic转换为待签字符串
            foreach (var item in sortDic)
            {
                signSourceBuilder.Append(item.Key).Append("=").Append(item.Value).Append("&");
            }
            return MD5Encrypt(signSourceBuilder.ToString().TrimEnd('&'));
        }
        

        /// <summary>
        /// 给一个字符串进行MD5加密
        /// </summary>
        /// <param name="strText">待加密字符串</param>
        /// <returns>加密后的字符串</returns>
        public static string MD5Encrypt(string strText)
        {
            //MD5 md5 = new MD5CryptoServiceProvider();
            byte[] result = MD5.Create().ComputeHash(System.Text.Encoding.UTF8.GetBytes(strText));
            var md5Sign = new StringBuilder();
            for (int i = 0; i < result.Length; i++)
            {
                var hex2 = result[i].ToString("x");
                //32位md5，单字节1位时需补0
                if (hex2.Length<2)
                {
                    hex2 = "0" + hex2;
                }
                md5Sign.Append(hex2);
            }
            return md5Sign.ToString();
        }
    }
}
