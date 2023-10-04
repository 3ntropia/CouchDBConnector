package org.connector.impl;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.connector.exceptions.CouchDBException;
import org.connector.http.AutoCloseableHttpResponse;
import org.connector.http.CouchHttpHeaders;
import org.connector.model.SaveResponse;
import org.connector.util.ConnectorFunction;
import org.connector.util.JSON;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.connector.util.ConnectorFunction.wrapEx;

public abstract class HTTPHandler {


    private final HttpHost httpHost;
    private final HttpContext httpContext;
    private final HttpClient httpClient;

    public HTTPHandler(HttpHost httpHost, HttpContext httpContext, HttpClient httpClient) {
        this.httpHost = httpHost;
        this.httpContext = httpContext;
        this.httpClient = httpClient;
    }

    static NameValuePair asPair(String key, String value) {
        return new BasicNameValuePair(key, value);
    }

    String mapResponseToJsonString(HttpResponse response) {
        return ofNullable(response)
                .flatMap(res -> ofNullable(res.getEntity()))
                .map(entity -> {
                    try {
                        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        throw new CouchDBException("Error parsing response", e);
                    }
                }).orElse("");
    }

    @NonNull
    URI getURI(@NonNull URI base, String... pathSegments) {
        try {
            return new URIBuilder(base).setPathSegments(pathSegments).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @NonNull
    URI getURI(@NonNull URI base, @NonNull List<NameValuePair> parameters) {
        try {
            return new URIBuilder(base).addParameters(parameters).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @NonNull
    URI getURI(@NonNull URI base, @NonNull List<String> pathSegments,
                         @NonNull List<NameValuePair> parameters) {
        try {
            return new URIBuilder(base).setPathSegments(pathSegments).addParameters(parameters).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    <T> T get(@NonNull URI uri,
                        @NonNull ConnectorFunction<HttpResponse, T, ? extends Exception> responseProcessor) {
        try (var response = new AutoCloseableHttpResponse()) {
            var get = new HttpGet(uri);
            get.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            response.set(execute(get));
            return wrapEx(responseProcessor).apply(response.get());
        }
    }

    InputStream get(URI uri) {
        HttpGet get = new HttpGet(uri);
        get.addHeader("Accept", "application/json");
        return get(get);
    }

    InputStream get(HttpGet httpGet) {
        HttpResponse response = execute(httpGet);
        try {
            return response.getEntity().getContent();
        } catch (Exception e) {
            throw new CouchDBException("Error reading response. ", e);
        }
    }

    <T> T post(@NonNull URI uri, @NonNull String json,
                         @NonNull ConnectorFunction<HttpResponse, T, ? extends Exception> responseProcessor) {
        try (var response = new AutoCloseableHttpResponse()) {
            final var post = new HttpPost(uri);
            var entity = new StringEntity(json, "UTF-8");
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            post.setEntity(entity);
            response.set(execute(post));
            return wrapEx(responseProcessor).apply(response.get());
        }
    }

    <T> T put(@NonNull URI uri, @NonNull String json,
                        @NonNull ConnectorFunction<HttpResponse, T, ? extends Exception> responseProcessor) {
        try (var response = new AutoCloseableHttpResponse()) {
            final var put = new HttpPut(uri);
            var entity = new StringEntity(json, "UTF-8");
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            put.setEntity(entity);
            response.set(execute(put));
            return wrapEx(responseProcessor).apply(response.get());
        }
    }

    SaveResponse put(URI uri, InputStream instream, String contentType) {
        try (var response = new AutoCloseableHttpResponse()) {
            final HttpPut put = new HttpPut(uri);
            final InputStreamEntity entity = new InputStreamEntity(instream, -1);
            entity.setContentType(contentType);
            put.setEntity(entity);
            response.set(execute(put));
            return JSON.readValue(response.get().getEntity().getContent(), SaveResponse.class);
        } catch (Exception e) {
            throw new CouchDBException(e);
        }
    }

    <T> T delete(@NonNull URI uri,
                           @NonNull ConnectorFunction<HttpResponse, T, ? extends Exception> responseProcessor) {
        try (var response = new AutoCloseableHttpResponse()) {
            final var delete = new HttpDelete(uri);
            delete.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            response.set(execute(delete));
            return wrapEx(responseProcessor).apply(response.get());
        }
    }

    CouchHttpHeaders head(@NonNull URI uri) {
        try (var response = new AutoCloseableHttpResponse()) {
            final var head = new HttpHead(uri);
            response.set(execute(head));
            var headers = Arrays.stream(response.get().getAllHeaders())
                    .collect(Collectors.toMap(
                            NameValuePair::getName,
                            NameValuePair::getValue
                    ));
            return CouchHttpHeaders.of(headers);
        }
    }

    @NonNull HttpResponse execute(@NonNull HttpRequestBase request) {
        try {
            return httpClient.execute(httpHost, request, httpContext);
        } catch (IOException e) {
            request.abort();
            throw new CouchDBException(e);
        }
    }
}
