package com.weimingfj.api.qiniu.comm;


import java.io.IOException;

import com.weimingfj.api.qiniu.http.Response;


@SuppressWarnings("serial")
public class QiniuException extends IOException {
    public final Response response;


    public QiniuException(Response response) {
        this.response = response;
    }

    public QiniuException(Exception e) {
        super(e);
        this.response = null;
    }

    public String url() {
        return response.url();
    }

    public int code() {
        return response == null ? -1 : response.statusCode;
    }
}
