package br.com.leonardoferreira.jirareport.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NaiveSSLSocketFactory extends SSLSocketFactory {

    private final SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

    private final SSLContext alwaysAllowSslContext;

    private final Set<String> naivelyTrustedHostnames;

    public NaiveSSLSocketFactory(final String... naivelyTrustedHostnames) throws NoSuchAlgorithmException,
            KeyManagementException {
        this.naivelyTrustedHostnames =
                Collections.unmodifiableSet(new HashSet<>(Arrays.asList(naivelyTrustedHostnames)));
        alwaysAllowSslContext = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {

            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                    throws CertificateException {
                // Check ok
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                    throws CertificateException {
                // Check ok
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        alwaysAllowSslContext.init(null, new TrustManager[] {tm}, null);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
            throws IOException {
        return naivelyTrustedHostnames.contains(host)
                ? alwaysAllowSslContext.getSocketFactory().createSocket(socket, host, port, autoClose)
                : sslSocketFactory.createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(final String host, final int port) throws IOException {
        return naivelyTrustedHostnames.contains(host)
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port)
                : sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort)
            throws IOException {
        return naivelyTrustedHostnames.contains(host)
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port, localAddress, localPort)
                : sslSocketFactory.createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(final InetAddress host, final int port) throws IOException {
        return naivelyTrustedHostnames.contains(host.getHostName())
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port)
                : sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(final InetAddress host, final int port, final InetAddress localHost, final int localPort)
            throws IOException {
        return naivelyTrustedHostnames.contains(host.getHostName())
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port, localHost, localPort)
                : sslSocketFactory.createSocket(host, port, localHost, localPort);
    }
}