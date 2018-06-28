#!/usr/bin/env python
# -*- coding: utf-8; py-indent-offset:4 -*-
import hashlib
import requests
import time

# 密钥对，请自行申请并妥善保管（注：正式环境是不同的）
accesskey = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
secretkey = 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
# REST请求环境（注：正式环境是不同的）
url_prefix = 'http://localhost'


# 验证签名
def get_request_data(params):
    # 组装验签参数
    param_for_sign = params.copy()
    timestamp = int(time.time())
    param_for_sign["accesskey"] = accesskey
    param_for_sign["secretkey"] = secretkey
    param_for_sign["timestamp"] = timestamp
    sign = '&'.join(['{}={}'.format(k, param_for_sign[k]) for k in sorted(param_for_sign.iterkeys())])
    # 生成签名
    md5_str = hashlib.md5(sign.encode("utf8")).hexdigest()
    # 组装请求参数
    request_data = {
        "common": {
            "accesskey": accesskey,
            "timestamp": timestamp,
            "sign": md5_str
        },
        "data": params
    }
    return request_data


def http_post(resource, params):
    r = requests.post(url_prefix + resource, json=params, timeout=1)
    result = r.json()
    return result
