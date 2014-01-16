package com.btaz.util.api.rest;

import com.btaz.util.api.Method;
import com.btaz.util.api.Resource;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class ResourceTest {
    @Test
    public void testInitializingOfLocalhostUrlStringShouldCreateResource() throws Exception {
        // given
        String input = "localhost";

        // when
        Resource resource = new Resource(input);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(Method.HTTP));
        assertThat(resource.getHost(), is(equalTo("localhost")));
        assertThat(resource.getPort(), is(equalTo(80)));
        assertThat(resource.getPath(), is(equalTo("")));
    }

    @Test
    public void testInitializingOfLocalhostWithPortUrlStringShouldCreateResource() throws Exception {
        // given
        String input = "localhost:8080";

        // when
        Resource resource = new Resource(input);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(Method.HTTP));
        assertThat(resource.getHost(), is(equalTo("localhost")));
        assertThat(resource.getPort(), is(equalTo(8080)));
        assertThat(resource.getPath(), is(equalTo("")));
    }

    @Test
    public void testInitializingOfMethodAndLocalhostAndPathUrlStringShouldCreateResource() throws Exception {
        // given
        String input = "https://localhost/";

        // when
        Resource resource = new Resource(input);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(Method.HTTPS));
        assertThat(resource.getHost(), is(equalTo("localhost")));
        assertThat(resource.getPort(), is(equalTo(443)));
        assertThat(resource.getPath(), is(equalTo("/")));
    }

    @Test
    public void testInitializingOfMethodAndLocalhostWithPortAndPathUrlStringShouldCreateResource() throws Exception {
        // given
        String input = "https://localhost:443/";

        // when
        Resource resource = new Resource(input);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(Method.HTTPS));
        assertThat(resource.getHost(), is(equalTo("localhost")));
        assertThat(resource.getPort(), is(equalTo(443)));
        assertThat(resource.getPath(), is(equalTo("/")));
    }

    @Test
    public void testInitializingOfMethodAndDomainWithPortAndPathUrlStringShouldCreateResource() throws Exception {
        // given
        String input = "https://api.domain.com:443/fruits";

        // when
        Resource resource = new Resource(input);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(Method.HTTPS));
        assertThat(resource.getHost(), is(equalTo("api.domain.com")));
        assertThat(resource.getPort(), is(equalTo(443)));
        assertThat(resource.getPath(), is(equalTo("/fruits")));
    }

    @Test
    public void testOfResourceConstructorShouldCreateResource() throws Exception {
        // given

        // when
        Resource resource = new Resource(Method.HTTP, "localhost", 80, null);

        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getMethod(), is(equalTo(Method.HTTP)));
        assertThat(resource.getHost(), is(equalTo("localhost")));
        assertThat(resource.getPort(), is(equalTo(80)));
        assertThat(resource.getPath(), is(equalTo("")));
    }
}
