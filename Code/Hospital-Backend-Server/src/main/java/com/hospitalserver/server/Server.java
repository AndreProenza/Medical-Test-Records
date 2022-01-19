package com.hospitalserver.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.springframework.util.ResourceUtils;

import com.hospitalserver.utils.ByteUtil;
import com.hospitalserver.utils.FileFunctions;

public class Server {

	private static final String FS = File.separator;

	// Diretorios
	// private static final String LABSCERTS = "PubKeys" + FS;

	// autenticacao
	// private static final String AUTHLABS = "AuthorizedLabs.txt";

	// Keystore
	private static String keystore = "hospital-backend-server.p12";
	private static String keystorePassword = "MedicalRecords";
	private static final String KEYSTORETYPE = "JCKS";
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

		private Socket socket = null;
		private ObjectOutputStream outStream = null;
		private ObjectInputStream inStream = null;

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

				if (auth()) {
					System.out.println("Autenticado");
					// userID = (String)inStream.readObject();
				}

				// Fecha coneccoes
				outStream.close();
				inStream.close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Processa um pedido de autenticacao de um utilizador
		 * 
		 * @return true caso o utilizador fique autenticado, false caso contrário
		 */
		private boolean auth() {

			boolean auth = false;
			try {

				// O id do lab que se está a ligar
				// labID = (String) inStream.readObject();

				long nonce = getNonce();

				// escreve na socket o nonce
				outStream.writeObject(nonce);

				// Abre o ficheiro que lista os labs autorizados
				// File f = ResourceUtils.getFile("classpath:" + AUTHLABS);

				// File enc = new File(removeExtensao(f) + ".cif");

				// decryptFileWithServerKey(enc);

				// if Lab existe e é reconhecido pelo Hospital
				// if (f.exists() && FileFunctions.contains(labID, f)) {
				// auth = authenticateUser(f, nonce);
				auth = authenticateLab(nonce);

				// }

				// encryptFileWithServerKey(f);

			} catch (IOException e) {
				System.out.println("Erro a ler da socket");
			}

			return auth;
		}

		private boolean authenticateLab(long nonce) {
			boolean auth = false;
			try {

				outStream.writeObject(0);

				// clear text nonce from client
				long nonceRcvd = (long) inStream.readObject();

				// signed nonce from client
				byte[] assinado = (byte[]) inStream.readObject();

				if (nonceRcvd != nonce) {
					// nonces não sao iguais por isso não vai autenticar
					outStream.writeObject(1);
				} else {

					if (verifySignature(nonce, assinado)) {
						outStream.writeObject(0);
						auth = true;
					} else {
						// not signed
						outStream.writeObject(1);
					}
				}

			} catch (ClassNotFoundException e) {
				System.out.println("authenticateUser: erro a ler da stream");
			} catch (IOException e) {
				System.out.println("authenticateUser: Erro a escrever na stream");
			}

			return auth;

		}

		private boolean verifySignature(long nonce, byte[] assinado) {
			try {
				File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
				FileInputStream kfile = new FileInputStream(keystoreF);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, keystorePassword.toCharArray());

				// Vai buscar a pubkey do lab
				Certificate cert = kstore.getCertificate(LABALIAS);
				PublicKey pk = cert.getPublicKey();

				Signature s = Signature.getInstance("MD5withRSA");
				s.initVerify(pk);
				s.update(ByteUtil.longToBytes(nonce));

				return s.verify(assinado);
				
			}catch (SignatureException e) {
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
		private long getNonce() {
			return new Random().nextLong();
		}
	}

	/**
	 * Verifica se uma string e um inteiro
	 * 
	 * @param s a string a verificar
	 * @return true caso seja, false caso contrario
	 */
	private static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static String encryptMessage(String msg) {
		try {

			File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
			FileInputStream kfile = new FileInputStream(keystoreF);
			KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
			kstore.load(kfile, keystorePassword.toCharArray());

			// Vai buscar a pubkey do lab
			Certificate cert = kstore.getCertificate(LABALIAS);
			PublicKey labPub = cert.getPublicKey();

			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, labPub);
			byte[] input = msg.getBytes();
			byte[] encrypted = c.doFinal(input);

			return Base64.getEncoder().encodeToString(encrypted);

		} catch (InvalidKeyException e) {
			System.out.println("encryptMessage: A secret key nao está no formato certo");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("encryptMessage: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("encryptMessage: O algoritmo escolhido nao pode ser utilizador");
		} catch (IllegalBlockSizeException e) {
			System.out.println("encryptMessage: Erro a fazer wrap da chave");
		} catch (BadPaddingException e) {
			System.out.println("encryptMessage: Erro a encriptar a mensagem");
		} catch (FileNotFoundException e) {
			System.out.println("encryptMessage: keystore não encontrada");
		} catch (KeyStoreException e) {
			System.out.println("encryptMessage: Instancia da keystore diferente da definida");
		} catch (CertificateException e) {
			System.out.println("encryptMessage: Password da keystore incorreta");
		} catch (IOException e) {
			System.out.println("encryptMessage: Erro na keystore");
		}
		return null;
	}

	private static String decryptMessage(String msg) {
		try {

			// para apanhar a private key
			File keystoreF = ResourceUtils.getFile("classpath:" + keystore);
			FileInputStream kfile = new FileInputStream(keystoreF);
			KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
			kstore.load(kfile, keystorePassword.toCharArray());

			// Vai buscar a privatekey
			Key serverPrivateKey = kstore.getKey(SERVERALIAS, keystorePassword.toCharArray());

			// Vai buscar a pubkey do lab
			Certificate cert = kstore.getCertificate(LABALIAS);
			PublicKey labPub = cert.getPublicKey();

			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, serverPrivateKey);
			byte[] input = Base64.getDecoder().decode(msg);
			byte[] decrypted = c.doFinal(input);

			return new String(decrypted);

		} catch (InvalidKeyException e) {
			System.out.println("encryptMessage: A secret key nao está no formato certo");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("encryptMessage: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("encryptMessage: O algoritmo escolhido nao pode ser utilizador");
		} catch (IllegalBlockSizeException e) {
			System.out.println("encryptMessage: Erro a fazer wrap da chave");
		} catch (BadPaddingException e) {
			System.out.println("encryptMessage: Erro a encriptar a mensagem");
		} catch (FileNotFoundException e) {
			System.out.println("encryptMessage: keystore não encontrada");
		} catch (KeyStoreException e) {
			System.out.println("encryptMessage: Instancia da keystore diferente da definida");
		} catch (CertificateException e) {
			System.out.println("encryptMessage: Password da keystore incorreta");
		} catch (IOException e) {
			System.out.println("encryptMessage: Erro na keystore");
		} catch (UnrecoverableKeyException e) {
			System.out.println("encryptMessage: Erro a obter chave privada do server");
		}
		return null;
	}

}
