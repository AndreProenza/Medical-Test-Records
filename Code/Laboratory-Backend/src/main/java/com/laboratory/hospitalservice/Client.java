package com.laboratory.hospitalservice;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.laboratory.model.mongodb.ClinicalRecord;

public class Client {

	private String ip;
	private int porta;

	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;
	private static SSLSocket clientSocket;

	private static String keystore = "laboratory-backend.p12";
	private static String keystorePassword = "MedicalRecords";
	private static final String KEYSTORETYPE = "JCKS";
	private static final String SERVERALIAS = "hospital-backend-server";
	private static final String LABALIAS = "laboratory-backend";

	public Client(String ip, int porta) {
		this.ip = ip;
		this.porta = porta;
		
		System.setProperty("javax.net.ssl.trustStore",System.getProperty("user.dir") + FS + truststore);
		System.setProperty("javax.net.ssl.trustStorePassword", keystorePassword);
		System.setProperty("javax.net.ssl.keyStore", System.getProperty("user.dir") + FS + keystore);
		System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
	}

	public Socket connect() {
		clientSocket = null;
		SocketFactory sf = SSLSocketFactory.getDefault();
		try {
			//InetAddress serverAddr = InetAddress.getByName("192.168.1.2");
			clientSocket = (SSLSocket) sf.createSocket(ip, porta);
			
			// Verify Server identity
			SSLSession sess = clientSocket.getSession();
			X509Certificate[] certs = (X509Certificate[]) sess.getPeerCertificates();
			String dnRemote = certs[0].getSubjectX500Principal().getName();
				System.out.println(dnRemote);
			if (dnRemote.equals("CN=Hospital Backend Server, OU=di, O=FCUL, L=Lisboa, ST=Lisboa, C=PT")) {
				System.out.println("Certificado do servidor: " + dnRemote);
			} else {
				System.out.println("Certificado do servidor está errado! Abortando");
				System.exit(1);
			}

		} catch (UnknownHostException e) {
			System.out.println("Host não conhecido!");
		} catch (IOException e) {
			System.out.println("Ligação falhou!");
		}

		if (clientSocket == null) {
			System.out.println("Conexão falhou, possiveis motivos:");
			System.out.println("+   server em baixo");
			System.out.println("+   ip incorreto");
			System.out.println("+   password da keystore incorreto");
			System.out.println("Por favor verifique se os argumentos inseridos estão corretos");

			System.exit(-1);
		}
		return clientSocket;
	}

	public void sendRecord(ClinicalRecord record) {

	}

	public void authenticate() {
		// TODO Auto-generated method stub
		
	}

}
