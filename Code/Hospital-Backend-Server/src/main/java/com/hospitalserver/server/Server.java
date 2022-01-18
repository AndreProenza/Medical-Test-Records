package com.hospitalserver.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.Random;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import com.hospitalserver.utils.FileFunctions;

public class Server {

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

		// id do utilizador da thread currente
		private String clientID = null;

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

				clientID = (String) inStream.readObject();

				System.out.println("Recebeu o client " + clientID);

				long nonce = getNonce();

				// escreve na socket o nonce
				outStream.writeObject(nonce);

				File f = new File(USERSDIR + USERSAUTH);

				File enc = new File(removeExtensao(f) + ".cif");

				decryptFileWithServerKey(enc);

				if (!f.exists()) {
					auth = newUser(f, nonce);
				} else {

					if (!FileFunctions.contains(clientID, f)) {
						auth = newUser(f, nonce);
					} else {
						auth = authenticateUser(f, nonce);
					}
				}
				encryptFileWithServerKey(f);

			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Erro a ler da socket");
			}

			return auth;
		}

		private boolean authenticateUser(File f, long nonce) {
			boolean auth = false;
			try {

				outStream.writeObject(0);

				// recebe o nonce do cliente
				long nonceRcvd = (long) inStream.readObject();

				// recebe o nonce do cliente
				byte[] assinado = (byte[]) inStream.readObject();

				if (nonceRcvd != nonce) {
					// nonces não sao iguais por isso não vai autenticar
					outStream.writeObject(1);
				} else {
					String certName = FileFunctions.getLineContaining(clientID + ":", f).split(":")[1];
					File userCert = new File(USERCERTS + certName);
					if (!userCert.exists()) {
						System.out.println("O certificado do user foi apagado");
						// nunca deveria acontecer
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

}
