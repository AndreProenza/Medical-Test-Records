package com.hospital;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.hospital.utils.BackendUri;

@SpringBootApplication
public class HospitaFrontendApplication {
	
	/* IMPORTANT
	 * 
	 * Both hospital frontend and backend run https.
	 * Hospital frontend consumes the backend rest api using the class 'RestTemplate'
	 * 
	 * However, when invoking a HTTPS URL with 'RestTemplate' an exception is thrown
	 * This can happen because:
	 *                       - Expired certificate 
	 *                       - Self-signed certificate
	 *                       - Wrong host information in certificates
	 *                       - Revoked certificate
	 *                       - Untrusted root of certificate
	 * 
	 * In this case happens because:
	 * 						 - Self-signed certificate
	 * 
	 * We created a Self-signed certificate to the acess the backend rest api.
	 * To bypass this problem, we disabled SSL certificate verification and thus trust all kind of certificates 
	 * whether valid or not in Spring Boot RestTemplate.
	 * In non production environments, we often need to disable ssl certificate validation 
	 * (self-signed, expired, non trusted root, etc) for testing purpose. 
	 * We will configure RestTemplate to disable SSL validation and allow https requests to these hosts without throwing exception.
	 */
	@Bean
    public Boolean disableSSLValidation() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
 
        sslContext.init(null, new TrustManager[]{(TrustManager) new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
 
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
 
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);
 
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
 
        return true;
    }
	
	@Bean
	public RestTemplate getRestTemplate() {
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BackendUri.BACKEND_API_BASE_URI);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(uriBuilderFactory);
		return restTemplate;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HospitaFrontendApplication.class, args);
	}

}
