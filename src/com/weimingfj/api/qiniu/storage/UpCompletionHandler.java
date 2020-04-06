package com.weimingfj.api.qiniu.storage;

import com.weimingfj.api.qiniu.http.Response;

/**
 * Created by bailong on 15/10/8.
 */
public interface UpCompletionHandler {
    void complete(String key, Response r);
}
