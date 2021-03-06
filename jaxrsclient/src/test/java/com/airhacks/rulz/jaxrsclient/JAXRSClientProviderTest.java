package com.airhacks.rulz.jaxrsclient;

/*
 * #%L
 * jaxrsclient
 * %%
 * Copyright (C) 2015 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class JAXRSClientProviderTest {

    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://www.google.com");

    @Test
    public void pingJava() {
        Client client = provider.client();
        assertNotNull(client);
        WebTarget target = provider.target();
        assertNotNull(target);
        assertNotNull(provider.addPath("en"));
        assertNotNull(provider.target("http://www.google.de"));

        Response response = target.request(MediaType.TEXT_HTML).get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void pingJavaAndVerifyWithMatcher() {
        Client client = provider.client();
        assertNotNull(client);
        WebTarget target = provider.target();
        assertNotNull(target);
        assertNotNull(provider.addPath("en"));
        assertNotNull(provider.target("http://www.google.de"));

        Response response = target.request(MediaType.TEXT_HTML).get();
        assertThat(response, is(successful()));
    }

    @Test
    public void buildWithNonExistingSystemProperty() {
        String defaultUri = "http://duke:42";
        JAXRSClientProvider p = JAXRSClientProvider.buildWithURI("--", defaultUri);
        assertNotNull(p);
        String actual = p.target().getUri().toString();
        assertThat(actual, is(defaultUri));
    }

    @Test
    public void buildWithExistingSystemProperty() {
        String expectedUri = "http://configured:21";
        final String key = "server-uri";
        System.setProperty(key, expectedUri);
        String defaultUri = "http://duke:42";
        JAXRSClientProvider p = JAXRSClientProvider.buildWithURI(key, defaultUri);
        assertNotNull(p);
        String actual = p.target().getUri().toString();
        assertThat(actual, is(expectedUri));
    }

}
