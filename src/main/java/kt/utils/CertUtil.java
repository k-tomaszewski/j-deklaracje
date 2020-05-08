package kt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CertUtil.class);
    private static int DEFAULT_HTTPS_PORT = 443;
    private static final TrustManager NO_CHECKS_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
    };

    public static X509Certificate fetchCertificate(URL url) {
        if (url == null || !"https".equalsIgnoreCase(url.getProtocol())) {
            return null;
        }
        LOG.debug("Fetching certificate from {}...", url);
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{NO_CHECKS_TRUST_MANAGER}, null);
            try (SSLSocket sslSocket = (SSLSocket) createSocket(sslContext, url)) {
                sslSocket.setUseClientMode(true);
                sslSocket.startHandshake();
                Certificate[] certs = sslSocket.getSession().getPeerCertificates();
                LOG.debug("Done. Certificate chain has {} elements.", certs.length);
                return (X509Certificate) certs[0];
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot fetch certificate from " + url, e);
        }
    }

    static Socket createSocket(SSLContext sslContext, URL url) throws IOException {
        int port = url.getPort();
        if (port < 0) {
            port = DEFAULT_HTTPS_PORT;
        }
        return sslContext.getSocketFactory().createSocket(url.getHost(), port);
    }

}
