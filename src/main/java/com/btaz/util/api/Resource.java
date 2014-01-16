package com.btaz.util.api;

import com.btaz.util.api.rest.RestApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: msundell
 */
public class Resource {
    private Method method;
    private String host;
    private int port;
    private String path;
    private Pattern reResource = Pattern.compile("(?:(http|https)://)?(localhost|[a-z0-9\\-\\.]+)(?::([0-9]+))?(/.*)?");

    /**
     * Initialize a resource using parameters
     */
    public Resource(Method method, String host, int port, String path) {
        // method
        if(method == null) {
            this.method = Method.HTTP;
        } else {
            this.method = method;
        }

        // host
        if(host == null) {
            throw new RestApiException("Invalid hostname");
        }
        this.host = host;

        // port
        this.port = port;

        // path
        if(path == null) {
            this.path = "";
        } else {
            this.path = path;
        }
    }

    /**
     * Initialize
     * @param host host
     * @param port port number
     * @param path path
     */
    public Resource(String host, int port, String path) {
        this(Method.HTTP, host, port, path);
    }

    /**
     * Initialize
     * @param host host
     * @param path path
     */
    public Resource(String host, String path) {
        this(Method.HTTP, host, 80, path);
    }

    /**
     * Initialize a resource instance from a URL string
     * @param url URL string
     * @throws com.btaz.util.api.rest.RestApiException exception
     */
    public Resource(String url) throws RestApiException {
        // match URL
        Matcher m = reResource.matcher(url);
        if(! m.matches()) {
            throw new RestApiException("Invalid URL pattern: " + url);
        }

        // method
        if(m.group(1) != null) {
            method = Method.valueOf(m.group(1).toUpperCase());
        } else {
            method = Method.HTTP;
        }

        // host
        if(m.group(2) == null) {
            throw new RestApiException("Invalid host definition");
        }
        host = m.group(2);

        // port
        if(m.group(3) != null) {
            port = Integer.valueOf(m.group(3));
        } else {
            if(method == Method.HTTP) {
                port = 80;
            } else {
                port = 443;
            }
        }

        // path
        if(m.group(4) == null) {
            path = "";
        } else {
            path = m.group(4);
        }
    }

    /**
     * Get method type
     * @return {@code Method}
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Get host
     * @return {@code String} host name
     */
    public String getHost() {
        return host;
    }

    /**
     * Get port number
     * @return {@code int}
     */
    public int getPort() {
        return port;
    }

    /**
     * Get path
     * @return {@code String} path
     */
    public String getPath() {
        return path;
    }
}
