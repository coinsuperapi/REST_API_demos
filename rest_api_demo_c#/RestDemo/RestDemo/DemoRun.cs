using System;
using System.Collections.Generic;

namespace RestDemo
{
    
    class DemoRun
    {
        public const string API_MY_ASSET = "/api/v1/asset/userAssetInfo";
        public const string API_MARKET_DEPTH = "/api/v1/market/depth";

        /// <summary>
        /// 测试方法
        /// </summary>
        /// <param name="args"></param>
        static void Main(string[] args)
        {
            //System.Diagnostics.Debug.WriteLine(ApiUtil.SendPost(API_MY_ASSET,null));
            var data = new Dictionary<string, string>(1);
            data.Add("symbol", "ETC/BTC");
            System.Diagnostics.Debug.WriteLine(ApiUtil.SendPost(API_MARKET_DEPTH, data));
        }
    }
}
