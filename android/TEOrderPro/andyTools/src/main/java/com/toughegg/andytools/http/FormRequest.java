package com.toughegg.andytools.http;

import android.util.Log;

import com.toughegg.andytools.http.method.HttpCallBack;
import com.toughegg.andytools.http.method.HttpHeaderParser;
import com.toughegg.andytools.http.method.HttpMethod;
import com.toughegg.andytools.http.method.NetworkResponse;
import com.toughegg.andytools.http.method.Request;
import com.toughegg.andytools.http.method.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Form表单形式的Http请求
 * 
 */
public class FormRequest extends Request<byte[]> {

    private final HttpParams mParams;

    public FormRequest(String url, HttpCallBack callback) {
        this(HttpMethod.GET, url, null, callback);
    }

    public FormRequest(int httpMethod, String url, HttpParams params,
            HttpCallBack callback) {
        super(httpMethod, url, callback);
        if (params == null) {
            params = new HttpParams();
        }
        this.mParams = params;
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
        if (mParams.getContentType() != null) {
            return mParams.getContentType().getValue();
        } else {
            return super.getBodyContentType();
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return mParams.getHeaders();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mParams.writeTo(bos);
        } catch (IOException e) {
            Log.e("error IOE", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, response.headers,
                HttpHeaderParser.parseCacheHeaders(mConfig, response));
    }

    @Override
    protected void deliverResponse(Map<String, String> headers, byte[] response) {
        if (mCallback != null) {
            mCallback.onSuccess(headers, response);
        }
    }
}
