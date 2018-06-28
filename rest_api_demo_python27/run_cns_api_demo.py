#!/usr/bin/env python
# -*- coding: utf-8; py-indent-offset:4 -*-
import cns_api

# 获取用户账户信息
params = {}
print "获取用户账户信息"
print cns_api.http_post("/api/v1/asset/userAssetInfo", cns_api.get_request_data(params))


# 下买单
params = {
    "orderType": 'LMT',
    'symbol': 'BTC/USD',
    'priceLimit': 8000,
    'quantity': 1,
    'amount': 0
}
print "下买单"
print cns_api.http_post("/api/v1/order/buy", cns_api.get_request_data(params))
