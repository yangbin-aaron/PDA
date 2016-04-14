package com.toughegg.andytools.http;


import com.toughegg.andytools.AndyConstants;
import com.toughegg.andytools.http.method.HttpCallBack;
import com.toughegg.andytools.http.method.HttpHeaderParser;
import com.toughegg.andytools.http.method.HttpMethod;
import com.toughegg.andytools.http.method.NetworkResponse;
import com.toughegg.andytools.http.method.Request;
import com.toughegg.andytools.http.method.Response;
import com.toughegg.andytools.systemUtil.AndyLoger;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 用来发起application/json格式的请求的，我们平时所使用的是form表单提交的参数，而使用JsonRequest提交的是json参数。
 * Created by Andy on 15/7/8.
 */
public class JsonReques extends Request<byte[]> {

    private static final String PROTOCOL_CONTENT_TYPE = String.format(
            "application/json; charset=%s", AndyConstants.DEFAULT_PARAMS_ENCODING);

    private String mRequestBody;
    private HttpParams mParams;


    public JsonReques(int method, String url, HttpParams params, HttpCallBack callBack) {
        super(method, url, callBack);
        mRequestBody = params.getJsonParams();
        mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mParams.getHeaders();
    }


    @Override
    public String getCacheKey() {
        if (getMethod() == HttpMethod.POST) {
            return getUrl() + mParams.getUrlParams();
        } else {
            return getUrl();
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(AndyConstants.DEFAULT_PARAMS_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            AndyLoger.debug("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, AndyConstants.DEFAULT_PARAMS_ENCODING);
            return null;
        }
    }

    //解析获取NetworkResponse
    @Override
    public Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, response.headers, HttpHeaderParser.parseCacheHeaders(mConfig, response));
    }

    //分发执行 callback
    @Override
    protected void deliverResponse(Map<String, String> headers, byte[] response) {
        if (mCallback != null) {
            mCallback.onSuccess(headers, response);
        }
    }
}
