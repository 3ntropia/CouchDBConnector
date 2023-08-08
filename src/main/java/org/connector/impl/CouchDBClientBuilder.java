package org.connector.impl;

import org.connector.http.ThrowingInterceptor;
import org.connector.exceptions.CouchDBException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class CouchDBClientBuilder {

    private final CouchDBClientProperties properties = new CouchDBClientProperties();

    public @NotNull CouchDBClientBuilder properties(@NotNull CouchDBClientProperties properties) {
        Assert.notNull(properties, "Properties must not be null.");
        this.properties.copy(properties);
        return this;
    }

    public @NotNull CouchDBClientBuilder url(@NotNull String url) {
        Assert.hasText(url, "Url must not be null nor empty.");
        this.properties.setUrl(url);
        return this;
    }

    /**
     * If protocol is empty http is used by default.
     */
    public @NotNull CouchDBClientBuilder url(String protocol,
                                             @NotNull String host,
                                             @NotNull Integer port) {
        Assert.hasText(host, "Host must not be null nor empty.");

        this.properties.setUrl(resolveUrlString(protocol, host, port));
        return this;
    }

    public @NotNull CouchDBClientBuilder database(@NotNull String database) {
        Assert.hasText(database, "Database name cannot be blank or null.");
        this.properties.setDatabase(database);
        return this;
    }

    public @NotNull CouchDBClientBuilder username(@NotNull String username) {
        Assert.hasText(username, "Username must not be null nor empty.");
        this.properties.setUsername(username);
        return this;
    }

    public @NotNull CouchDBClientBuilder password(@NotNull String password) {
        Assert.hasText(password, "Password must not be null nor empty.");
        this.properties.setPassword(password);
        return this;
    }

    public @NotNull CouchDBClientBuilder bulkMaxSize(@NotNull Integer bulkMaxSize) {
        Assert.notNull(bulkMaxSize, "BulkMaxSize must not be null.");
        Assert.isTrue(10 <= bulkMaxSize && bulkMaxSize <= 100000,
                "BulkMaxSize must be greaterEq than 10 and lesserEq than 100000.");
        this.properties.setBulkMaxSize(bulkMaxSize);
        return this;
    }

    public @NotNull CouchDBClientBuilder maxConnections(@NotNull Integer maxConnections) {
        Assert.notNull(maxConnections, "MaxConnections must not be null.");
        Assert.isTrue(1 <= maxConnections, "MaxConnections must greater than 1.");
        this.properties.setMaxConnections(maxConnections);
        return this;
    }

    public @NotNull CouchDBClientBuilder defaultPartitioned(@NotNull Boolean defaultPartitioned) {
        Assert.notNull(defaultPartitioned, "Default Partitioned must not be null.");
        this.properties.setDefaultPartitioned(defaultPartitioned);
        return this;
    }

    public @NotNull CouchDBClientBuilder testConnection(@NotNull Boolean testConnection) {
        Assert.notNull(testConnection, "Test Connection must not be null.");
        this.properties.setTestConnection(testConnection);
        return this;
    }

    public @NotNull CouchDBClientBuilder initIndexesFromFiles(@NotNull Boolean initIndexesFromFiles) {
        Assert.notNull(initIndexesFromFiles, "initIndexesFromFiles must not be null.");
        this.properties.setInitIndexesFromFiles(initIndexesFromFiles);
        return this;
    }

    public @NotNull CouchDBClientBuilder createDatabase(@NotNull Boolean createDatabase) {
        Assert.notNull(createDatabase, "createDatabase must not be null.");
        this.properties.setCreateDatabase(createDatabase);
        return this;
    }

    public @NotNull CouchDBClient build() {
        var uri = URI.create(ifNotNull(properties.getUrl(), "Url must be configured (can not be null)"));
        var host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        var context = getHttpContext(
                host,
                ifNotNull(properties.getUsername(), "User must be configured, (can not be null)"),
                ifNotNull(properties.getPassword(), "Password must be configured, (can not be null)")
        );
        var client = getHttpClient();
        return new CouchDBClient(uri, properties.getDatabase(), host, context, client,
                properties.isCreateDatabase(), properties.isDefaultPartitioned(), properties.getBulkMaxSize(),
                properties.isTestConnection(), properties.isInitIndexesFromFiles());
    }

    private @NotNull HttpContext getHttpContext(@NotNull HttpHost host, @NotNull String username, @NotNull String password) {
        AuthCache authCache = new BasicAuthCache();
        authCache.put(host, new BasicScheme());
        HttpContext context = new BasicHttpContext();
        context.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
        context.setAttribute(HttpClientContext.CREDS_PROVIDER, getCredentialProvider(username, password));
        return context;
    }

    private @NotNull CredentialsProvider getCredentialProvider(@NotNull String username, @NotNull String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
    }

    private <T> T ifNotNull(T o, String message) {
        Assert.notNull(o, message);
        return o;
    }

    private String resolveUrlString(String protocol, String host, Integer port) {
        var uriBuilder = new URIBuilder()
                .setHost(host)
                .setPort(port);
        uriBuilder.setScheme(isBlank(protocol) ? this.properties.getProtocol() : protocol);
        try {
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            log.error("Error building uri with {}, {}, {}, {}", protocol, host, port, e);
            throw new CouchDBException(e);
        }
    }

    private @NotNull HttpClient getHttpClient() {
        try {
            var ccm = new PoolingHttpClientConnectionManager(getRegistry());
            ccm.setDefaultMaxPerRoute(this.properties.getMaxConnections());
            ccm.setMaxTotal(this.properties.getMaxConnections());
            var clientBuilder = HttpClients.custom()
                    .setConnectionManager(ccm)
                    .setDefaultConnectionConfig(ConnectionConfig.custom()
                            .setCharset(Consts.UTF_8).build())
                    .setDefaultRequestConfig(RequestConfig.DEFAULT)
                    .addInterceptorFirst(new ThrowingInterceptor())
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            return clientBuilder.build();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create HTTP client", e);
        }
    }

    private Registry<ConnectionSocketFactory> getRegistry() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", new SSLConnectionSocketFactory(
                        SSLContexts.custom().loadTrustMaterial(null, TrustSelfSignedStrategy.INSTANCE).build(),
                        NoopHostnameVerifier.INSTANCE))
                .register("http", PlainConnectionSocketFactory.INSTANCE).build();
    }

}
