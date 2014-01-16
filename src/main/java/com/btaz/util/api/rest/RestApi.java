package com.btaz.util.api.rest;

import com.btaz.util.api.Resource;

/**
 * User: msundell
 */
public class RestApi {
    private Resource resource;

    public RestApi(String url) {
        resource = new Resource(url);
    }

    public static RestApi get(String url) {
        return new RestApi(url);
    }

    public String asString() {
        return null;
    }
}
