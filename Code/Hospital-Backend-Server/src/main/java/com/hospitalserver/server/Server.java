package com.hospitalserver.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.net.ServerSocketFactory;

import org.springframework.util.ResourceUtils;

import com.hospitalserver.model.mongodb.MedicalRecord;
import com.hospitalserver.utils.ByteUtil;

public class Server {

	// Keystore
	private static String keystore = "hospital-backend-server.p12";
	private static String truststore = "hospital-backend-server-truststore.p12";
	private static String keystorePassword = "MedicalRecords";
	private static String truststorePassword = "MedicalRecords";
	private static final String KEYSTORETYPE = "PKCS12";
	private static final String SERVERALIAS = "hospital-backend-server";
	private static final String LABALIAS = "laboratory-backend";

	private final static int PORT = 4000;

	public static void run() {

		ServerSocket socket = null;

		try {

			ServerSocketFactory ssf = ServerSocketFactory.getDefault();
			socket = (ServerSocket) ssf.createServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

		while (true) {
			try {
				Socket inSoc = (Socket) socket.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
			} catch (NullPointerException e) {
				System.out.println("Server socket é null");
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// dead code
		// socket.close();

	}

	/**
	 * Threads utilizadas para comunicacao com os clientes
	 */
	private static class ServerThread extends Thread {

		private static Socket socket = null;
		private static ObjectOutputStream outStream = null;
		private static ObjectInputStream inStream = null;

		private static SecretKey simetricKey = null;

		// id do laboratorio da thread currente
		// private String labID = null;

		/**
		 * Construtor da classe. Cria uma nova Thread para o cliente que se ligou no
		 * momento
		 * 
		 * @param inSoc a socket da ligação estabelecida
		 */
		public ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");

		}

		public void start() {
			try {
				outStream = new ObjectOutputStream(socket.getOutputStream());
				inStream = new ObjectInputStream(socket.getInputStream());

				if (authServer() && authenticateLab()) {
					System.out.println("Autenticado");

					MedicalRecord record = receiveRecord();
					
					// TODO put in BD
					System.out.println(record.toString());
				}

				// Close conections
				inStream.close();
				outStream.close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private static boolean authServer() {
			try {
				// O id do lab que se está a ligar
				@SuppressWarnings("unused")
				String helloFromLab = (String) inStream.readObject();

				// server sends its certificate for authentication
				Certificate cert = getServerCertificate();
				byte[] encodedCert = cert.getEncoded();

				// write encodedCert
				String encodedCertB64 = new String(Base64.getEncoder().encode(encodedCert));
				outStream.writeObject(encodedCertB64);

				String OK = (String) inStream.readObject();
				if (OK.equals("OK")) {
					return true;
				}

			} catch (ClassNotFoundException e) {
				System.out.println("authenticateUser: erro a ler da stream");
			} catch (IOException e) {
				System.out.println("authenticateUser: Erro a escrever na stream");
			} catch (CertificateEncodingException e) {
				System.out.println("authenticateUser: encodificação do certificado falhou");
			}
			return false;
		}

		private boolean authenticateLab() {
			try {

				long nonce = getNonce();

				// escreve na socket o nonce
				outStream.writeObject(nonce);

				// clear text nonce from client
				long nonceRcvd = (long) inStream.readObject();

				// signed nonce from client
				String assinadoB64 = (String) inStream.readObject();

				byte[] assinado = Base64.getDecoder().decode(assinadoB64);

				int n = 1;
				if (nonceRcvd == nonce && verifySignature(nonce, assinado)) {
					n = 0;

					outStream.writeObject(n);
					PublicKey labkey = getLabPubKey();
					// Generate simetrikey
					simetricKey = createSimetricKey();
					// sends the new Simetric key for the comunication
					byte[] wrappedKey = encryptSimetricKey(simetricKey, labkey);

					// outStream.writeObject(wrappedKey);
					String encodedSimetricKey = new String(Base64.getEncoder().encode(wrappedKey));
					outStream.writeObject(encodedSimetricKey);

					return true;

				}
				outStream.writeLong(n);

			} catch (ClassNotFoundException e) {
				System.out.println("authenticateUser: erro a ler da stream");
			} catch (IOException e) {
				System.out.println("authenticateUser: Erro a escrever na stream");
			}

			return false;

		}

		private static boolean verifySignature(long nonce, byte[] assinado) {
			try {
				File keystoreF = ResourceUtils.getFile("classpath:" + truststore);

				FileInputStream kfile = new FileInputStream(keystoreF);

				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, truststorePassword.toCharArray());

				// Vai buscar a pubkey do lab
				Certificate cert = kstore.getCertificate(LABALIAS);
				PublicKey pk = cert.getPublicKey();

				Signature s = Signature.getInstance("MD5withRSA");
				s.initVerify(pk);
				s.update(ByteUtil.longToBytes(nonce));

				return s.verify(assinado);

			} catch (SignatureException e) {
				System.out.println("verifySignature: Erro ao verificar a assinatura");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("verifySignature: Erro Algoritmo de signature nao existe");
			} catch (InvalidKeyException e) {
				System.out.println("verifySignature: A chave não para assinatura não está correta");
			} catch (CertificateException e1) {
				System.out.println("verifySignature: Erro a desserializar o certificado");
			} catch (FileNotFoundException e) {
				System.out.println("verifySignature: keystore não encontrada");
			} catch (KeyStoreException e) {
				System.out.println("verifySignature: Instancia da keystore diferente da definida");
			} catch (IOException e) {
				System.out.println("verifySignature: Erro a escrever na stream");
			}
			return false;
		}

		/*
		 * Gera um long aleatorio
		 */
		private static long getNonce() {
			return new Random().nextLong();
		}

		/**
		 * Obtains a server certificate to send to remote lab for autentication
		 * 
		 * @return the server certificate
		 */
		private static Certificate getServerCertificate() {
			try {

				File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
				FileInputStream kfile = new FileInputStream(keystoreF);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, keystorePassword.toCharArray());

				return kstore.getCertificate(SERVERALIAS);

			} catch (NoSuchAlgorithmException e) {
				System.out.println("getServerPrivateKey: Algoritmo de encriptacao nao existe");
			} catch (FileNotFoundException e) {
				System.out.println("getServerPrivateKey: keystore não encontrada");
			} catch (KeyStoreException e) {
				System.out.println("getServerPrivateKey: Instancia da keystore diferente da definida");
			} catch (CertificateException e) {
				System.out.println("getServerPrivateKey: Password da keystore incorreta");
			} catch (IOException e) {
				System.out.println("getServerPrivateKey: Erro na keystore");
			}
			return null;
		}

		private static PublicKey getLabPubKey() {
			try {

				File truststoreF = ResourceUtils.getFile("classpath:" + truststore);
				FileInputStream kfile = new FileInputStream(truststoreF);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, truststorePassword.toCharArray());

				// Vai buscar a pubkey do lab
				Certificate cert = kstore.getCertificate(LABALIAS);
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

		/**
		 * Creates a simetric key using AES 128 bits
		 * 
		 * @return The SecretKey generated
		 */
		private static SecretKey createSimetricKey() {
			try {

				KeyGenerator kg = KeyGenerator.getInstance("AES");
				kg.init(128);
				return kg.generateKey();

			} catch (NoSuchAlgorithmException e) {
				System.out.println("encryptFile: Encryption algorithm not existent");
			}
			return null;
		}

		private static byte[] encryptSimetricKey(SecretKey simetricKey, PublicKey labkey) {
			try {

				// Cipher a chave usada para cifrar o ficheiro com a pubkey do server
				Cipher c1 = Cipher.getInstance("RSA");
				c1.init(Cipher.WRAP_MODE, labkey);

				return c1.wrap(simetricKey);

			} catch (InvalidKeyException e) {
				System.out.println("encryptFile: A secret key nao está no formato certo");
			} catch (NoSuchPaddingException e) {
				System.out.println("encryptFile: O algoritmo escolhido nao pode ser utilizador");
			} catch (IllegalBlockSizeException e) {
				System.out.println("encryptFile: Erro a fazer wrap da chave");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Algoritmo de encriptacao escolhido nao existe");
			}
			return null;
		}

		public MedicalRecord receiveRecord() {
			try {
				Cipher c = Cipher.getInstance("AES");
				c.init(Cipher.DECRYPT_MODE, simetricKey);

				SealedObject sealedObject = (SealedObject) inStream.readObject();
				return (MedicalRecord) sealedObject.getObject(c);

			} catch (InvalidKeyException e) {
				System.out.println("receiveRecord: A secret key nao está no formato certo");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("receiveRecord: Algoritmo de encriptacao nao existe");
			} catch (NoSuchPaddingException e) {
				System.out.println("receiveRecord: O algoritmo escolhido nao pode ser utilizador");
			} catch (IllegalBlockSizeException e) {
				System.out.println("receiveRecord: Erro a fazer wrap da chave");
			} catch (IOException e) {
				System.out.println("receiveRecord: Erro a encriptar a mensagem");
			} catch (ClassNotFoundException e) {
				System.out.println("receiveRecord: Erro a dar cast para SealedObject");
				e.printStackTrace();
			} catch (BadPaddingException e) {
				System.out.println("receiveRecord falha a decifrar a mensagem");
			}
			return null;
		}

	}
}
