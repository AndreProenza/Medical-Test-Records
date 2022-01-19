package com.hospitalserver.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
	private static final String LABSCERTS = "PubKeys" + FS;

	// autenticacao
	private static final String AUTHLABS = "AuthorizedLabs.txt";

	// Keystore
	private static String keystore;
	private static String keystorePassword;
	private static final String KEYSTORETYPE = "JKS";
	private static final String ALIAS = "serverRsa";

	private final static int PORT = 5000;

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
		private String labID = null;

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

					File userCert = new File(LABSCERTS + certName);
					if (!userCert.exists()) {
						// Nunca deveria acontecer
						System.out.println("O certificado foi apagado sem ser removido da lista");
					}

					String encodedCert = FileFunctions.getFirstLine(userCert);

					byte[] b = Base64.getDecoder().decode(encodedCert);
					CertificateFactory cf = CertificateFactory.getInstance("X509");
					Certificate c = cf.generateCertificate(new ByteArrayInputStream(b));

					// Verifica a assinatura
					PublicKey pk = c.getPublicKey();
					Signature s = Signature.getInstance("MD5withRSA");
					s.initVerify(pk);
					s.update(ByteUtil.longToBytes(nonce));

					if (!s.verify(assinado)) {
						// assinatura não é correta
						outStream.writeObject(1);
					} else {
						// Autenticado
						outStream.writeObject(0);
						auth = true;
					}
				}

			} catch (IOException e) {
				System.out.println("authenticateUser: Erro a escrever na stream");
			} catch (ClassNotFoundException e) {
				System.out.println("authenticateUser: erro a ler da stream");
			} catch (SignatureException e) {
				System.out.println("authenticateUser: Erro ao verificar a assinatura");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("authenticateUser: Erro Algoritmo de signature nao existe");
			} catch (InvalidKeyException e) {
				System.out.println("authenticateUser: A chave não para assinatura não está correta");
			} catch (CertificateException e1) {
				System.out.println("authenticateUser: Erro a desserializar o certificado");
			}

			return auth;

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

	private static void encryptFileWithServerKey(File f) {
		try {
			if (f.exists()) {
				// gerar uma chave aleatória para utilizar com o AES

				KeyGenerator kg = KeyGenerator.getInstance("AES");
				kg.init(128);
				SecretKey sk = kg.generateKey();

				Cipher c = Cipher.getInstance("AES");
				c.init(Cipher.ENCRYPT_MODE, sk);

				FileInputStream fis = new FileInputStream(f);
				FileOutputStream fos = new FileOutputStream(removeExtensao(f) + ".cif");
				CipherOutputStream cos = new CipherOutputStream(fos, c);

				byte[] b = new byte[16];
				int i = fis.read(b);
				while (i != -1) {
					cos.write(b, 0, i);
					i = fis.read(b);
				}

				cos.close();
				fis.close();
				fos.close();

				FileInputStream kfile = new FileInputStream(keystore);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, keystorePassword.toCharArray());

				// Vai buscar a pubkey
				Certificate cert = kstore.getCertificate(ALIAS);
				PublicKey publicKey = cert.getPublicKey();

				// Cipher a chave usada para cifrar o ficheiro com a pubkey do server
				Cipher c1 = Cipher.getInstance("RSA");
				c1.init(Cipher.WRAP_MODE, publicKey);
				byte[] wrappedKey = c1.wrap(sk);

				// Guardar a chave
				File fKey = new File(removeExtensao(f) + ".key"); // armazenar

				if (!fKey.createNewFile()) {
					System.out.println("Erro a criar o ficheiro " + fKey.getName());
				}
				FileFunctions.addNextLineToFile(Base64.getEncoder().encodeToString(wrappedKey), fKey);

				// depois de encryptar apaga o ficheiro de texto
				if (!f.delete()) {
					System.out.println("Ficheiro decifrado nao foi apagado!");
					System.exit(1);// erro de segurança logo o servidor devia desligar (?)
				}
			} else {
				// do nothing
			}
		} catch (IOException e) {
			System.out.println("encryptFile: erro a escrever no ObjectStream");
		} catch (InvalidKeyException e) {
			System.out.println("encryptFile: A secret key nao está no formato certo");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("encryptFile: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("encryptFile: O algoritmo escolhido nao pode ser utilizador");
		} catch (IllegalBlockSizeException e) {
			System.out.println("encryptFile: Erro a fazer wrap da chave");
		} catch (KeyStoreException e) {
			System.out.println("encryptFile: Erro a ir buscar o certificado");
		} catch (CertificateException e) {
			System.out.println("encryptFile: Erro a ler do keystore");
		}

	}

	private static void decryptFileWithServerKey(File f) {
		try {
			if (f.exists()) {

				// le a chave
				File fKey = new File(removeExtensao(f) + ".key");
				String keyB64 = FileFunctions.getFirstLine(fKey);

				byte[] wrappedKey = Base64.getDecoder().decode(keyB64);

				// para apanhar a private key
				FileInputStream kfile = new FileInputStream(keystore);
				KeyStore kstore = KeyStore.getInstance(KEYSTORETYPE);
				kstore.load(kfile, keystorePassword.toCharArray());

				// Vai buscar a privatekey
				Key privateKey = kstore.getKey(ALIAS, keystorePassword.toCharArray());

				Cipher c = Cipher.getInstance("RSA");
				c.init(Cipher.UNWRAP_MODE, privateKey);

				Key unwrappedKey = c.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

				FileInputStream fis = new FileInputStream(f);

				Cipher c1 = Cipher.getInstance("AES");
				c1.init(Cipher.DECRYPT_MODE, unwrappedKey);
				CipherInputStream cis = new CipherInputStream(fis, c1);

				File fDecif = new File(removeExtensao(f) + ".txt");
				FileOutputStream fos = new FileOutputStream(fDecif);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				byte[] b = new byte[16];

				int i = cis.read(b);

				while (i != -1) {
					bos.write(b, 0, i);
					i = cis.read(b);
				}

				cis.close();
				bos.close();

				// apaga o ficheiro com a chave e com o texto cifrado
				if (!fKey.delete()) {
					System.out.println("decryptFile: erro a apagar o ficheiro com a chave");
					System.exit(1);// erro de segurança logo o servidor devia desligar (?)
				}

				if (!f.delete()) {
					System.out.println("decryptFile: erro a apagar o ficheiro cifrado");
					System.exit(1); // erro de segurança logo o servidor devia desligar (?)
				}

			} else {
				// do nothing
			}
		} catch (IOException e) {
			System.out.println("decryptFile: erro a escrever no OnjectStream");
		} catch (InvalidKeyException e) {
			System.out.println("decryptFile: A secret key nao está no formato certo");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("decryptFile: Algoritmo de encriptacao nao existe");
		} catch (NoSuchPaddingException e) {
			System.out.println("decryptFile: O algoritmo escolhido nao pode ser utilizador");
		} catch (KeyStoreException e) {
			System.out.println("decryptFile: Erro a ir buscar o certificado");
		} catch (CertificateException e) {
			System.out.println("decryptFile: Erro a ler do keystore");
		} catch (UnrecoverableKeyException e) {
			System.out.println("decryptFile: Erro a tentar ir buscar a chave privada");
		}
	}

	private static String removeExtensao(File f) {

		if (f.isDirectory())
			return f.getPath();

		String fName = f.getName();

		int lastPeriodPos = fName.lastIndexOf('.');
		if (lastPeriodPos <= 0) {
			// Se o ficheiro não tiver ponto
			return f.getPath();

		} else {

			// Remove o ponto e tudo a seguir
			File semExtensao = new File(f.getParent(), fName.substring(0, lastPeriodPos));
			return semExtensao.getPath();
		}
	}

}
