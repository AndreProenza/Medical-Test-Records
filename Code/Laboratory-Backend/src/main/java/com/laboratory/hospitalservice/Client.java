package com.laboratory.hospitalservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.net.SocketFactory;

import org.springframework.util.ResourceUtils;


import com.hospitalserver.model.mongodb.MedicalRecord;
import com.laboratory.utils.ByteUtil;


public class Client {

	private String ip;
	private int porta;

	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;
	private static Socket clientSocket;
	private static Key simetricKey;

	private static String keystore = "laboratory-backend.p12";
	private static String truststore = "laboratory-backend-truststore.p12";
	private static String keystorePassword = "MedicalRecords";
	private static String truststorePassword = "MedicalRecords";
	private static final String KEYSTORETYPE = "PKCS12";
	private static final String SERVERALIAS = "hospital-backend-server";
	private static final String LABALIAS = "laboratory-backend";

	public Client(String ip, int porta) {
		this.ip = ip;
		this.porta = porta;
	}

	public Socket connect() {
		clientSocket = null;
		SocketFactory sf = SocketFactory.getDefault();
		try {
			// InetAddress serverAddr = InetAddress.getByName("192.168.1.2");
			clientSocket = (Socket) sf.createSocket(ip, porta);

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

	public boolean authenticate() {
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());

			// Could be a labID
			out.writeObject("hello");

			String encodedCertB64 = (String) in.readObject();
			byte[] b = Base64.getDecoder().decode(encodedCertB64);

			CertificateFactory cf = CertificateFactory.getInstance("X509");
			Certificate c = cf.generateCertificate(new ByteArrayInputStream(b));

			if (c.getPublicKey().equals(getServerPubKey())) {
				// if (true) {
				out.writeObject("OK");

				long nonce = (long) in.readObject();

				File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
				FileInputStream kfile = new FileInputStream(keystoreF);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, keystorePassword.toCharArray());

				// Ir buscar a chave privada
				PrivateKey privateKey = (PrivateKey) kstore.getKey(LABALIAS, keystorePassword.toCharArray());

				// assinar a mensagem com a chave privada
				Signature s = Signature.getInstance("MD5withRSA");
				// usa private para assinar
				s.initSign(privateKey);
				// passa o nonce para um byte[]
				byte buf[] = ByteUtil.longToBytes(nonce);
				s.update(buf);

				// envia o nonce recebido
				out.writeObject(nonce);
				
				// nonce assinado
				byte[] assinado = s.sign();

				// envia o nonce assinado
				String assinadoB64 = new String(Base64.getEncoder().encode(assinado));
				out.writeObject(assinadoB64);

				int res = (Integer) in.readObject();
				if (res == 0) {
					// Autenticated

					String wrappedSimetricKey = (String) in.readObject();

					simetricKey = decryptSimetricKey(wrappedSimetricKey);
					return true;
				}
			} else {
				out.writeObject("FAILED");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Falha na rececao do objeto no create.");
		} catch (IOException e) {
			System.out.println("Falha na criação de output e input streams");
			e.printStackTrace();
		} catch (KeyStoreException e) {
			System.out.println("Erro no keystore");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Erro no algoritmo da keystore");
		} catch (CertificateException e) {
			System.out.println("Erro no certificado da keystore");
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			System.out.println("Erro a ir buscar a privatekey do cliente");
			e.printStackTrace();
		} catch (SignatureException e) {
			System.out.println("Erro ao assinar o nonce");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Erro a chave usada na encriptacao não a é privada.");
			e.printStackTrace();
		} 
		// failure
		return false;

	}
	

	public void sendRecord(MedicalRecord record) {
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, simetricKey);
			SealedObject sealedRecord = new SealedObject(record, c);
			// sends records
			out.writeObject(sealedRecord);

		} catch (InvalidKeyException e) {
			System.out.println("sendRecord: A secret key nao está no formato certo");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("sendRecord: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("sendRecord: O algoritmo escolhido nao pode ser utilizador");
		} catch (IllegalBlockSizeException e) {
			System.out.println("sendRecord: Erro a fazer wrap da chave");
		} catch (IOException e) {
			System.out.println("sendRecord: Erro a encriptar a mensagem");
		}
	}

	private static Key getLabPrivateKey() {
		try {
			// para apanhar a private key
			File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
			FileInputStream kfile = new FileInputStream(keystoreF);
			KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
			kstore.load(kfile, keystorePassword.toCharArray());

			// Vai buscar a privatekey
			return kstore.getKey(LABALIAS, keystorePassword.toCharArray());

		} catch (NoSuchAlgorithmException e) {
			System.out.println("getLabPrivateKey: Algoritmo de encriptacao nao existe");
		} catch (FileNotFoundException e) {
			System.out.println("getLabPrivateKey: keystore não encontrada");
		} catch (KeyStoreException e) {
			System.out.println("getLabPrivateKey: Instancia da keystore diferente da definida");
		} catch (CertificateException e) {
			System.out.println("getLabPrivateKey: Password da keystore incorreta");
		} catch (IOException e) {
			System.out.println("getLabPrivateKey: Erro na keystore");
		} catch (UnrecoverableKeyException e) {
			System.out.println("getLabPrivateKey: Wasnt able to recover private key");
		}
		return null;
	}

	private static PublicKey getServerPubKey() {
		try {

			File truststoreF = ResourceUtils.getFile("classpath:" + truststore);
			FileInputStream kfile = new FileInputStream(truststoreF);
			KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
			kstore.load(kfile, truststorePassword.toCharArray());

			// Vai buscar a pubkey do lab
			Certificate cert = kstore.getCertificate(SERVERALIAS);
			return cert.getPublicKey();

		} catch (NoSuchAlgorithmException e) {
			System.out.println("getLabPubKey: Algoritmo de encriptacao nao existe");
		} catch (FileNotFoundException e) {
			System.out.println("getLabPubKey: keystore não encontrada");
		} catch (KeyStoreException e) {
			System.out.println("getLabPubKey: Instancia da keystore diferente da definida");
		} catch (CertificateException e) {
			System.out.println("getLabPubKey: Password da keystore incorreta");
		} catch (IOException e) {
			System.out.println("getLabPubKey: Erro na keystore");
		}
		return null;
	}

	private static Key decryptSimetricKey(String encodedKey) {
		try {

			byte[] wrappedKey = Base64.getDecoder().decode(encodedKey);

			// Vai buscar a privatekey
			Key privateKey = getLabPrivateKey();

			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.UNWRAP_MODE, privateKey);
			// return unwrappedKey
			return c.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

		} catch (InvalidKeyException e) {
			System.out.println("decryptSimetricKey: A secret key nao está no formato certo");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("decryptSimetricKey: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("decryptSimetricKey: O algoritmo escolhido nao pode ser utilizador");
		}
		return null;
	}


	/**
	 * Closes socket connection
	 */
	public void close() {
		try {
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("close: failed closing socket");
		}

	}
}
