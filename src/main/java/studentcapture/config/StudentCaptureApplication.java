package studentcapture.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@SpringBootApplication
@ComponentScan(basePackages = {"studentcapture"})
public class StudentCaptureApplication {
	public static String ROOT = System.getProperty("user.dir");
	public static void main(String[] args) {
		enableSelfSignedHTTPSCertificate();
		SpringApplication.run(StudentCaptureApplication.class, args);
	}


	//TODO Group 4: Philip Pålsson Why is this method here?
	@Bean
	public RestTemplate createTemplateMock(){
		return new RestTemplate();
	}

	//The below method and classes is used to temporarily enableSelfSignedHTTPSCertificate https/ssl verification to allow https
	// request with untrusted certificate.
	public static void enableSelfSignedHTTPSCertificate() {
		try {
			SSLContext sslc = SSLContext.getInstance("TLS");
			TrustManager[] trustManagerArray = { new NullX509TrustManager() };
			sslc.init(null, trustManagerArray, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static class NullX509TrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static class NullHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}


}
