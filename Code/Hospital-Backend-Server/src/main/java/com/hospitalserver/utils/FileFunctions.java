package com.hospitalserver.utils;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



public class FileFunctions {
	private static final String AUXFILE = "temp.txt";
	private static final String ENTRADA = "Entrou: ";
	
	private FileFunctions() {}
	

	public static boolean contains(String str, File f) {
		try (FileReader fr = new FileReader(f); 
				BufferedReader br = new BufferedReader(fr)) {

			String readLine;
			while ((readLine = br.readLine()) != null) {
				if (readLine.contains(str)) {
					return true;
				}
			}
		}catch (FileNotFoundException e) {
			System.out.println("ficheiro nao existe");
			return false;
		} catch (IOException e) {
			System.out.println("contains: Erro a ler do bufferedReader");
		}
		
		return false;
	}

    public static boolean isLineInFile(String str, File file) {
    	
    	if (!file.exists()) {
    		return false;
    	}
		String readLine;
		boolean found = false;
		
		try (FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr)) {
				
			while ((readLine = br.readLine()) != null && !found) {
				if (readLine.equals(str)) {
					found = true;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("isLineInFile: Erro a ler o ficheiro");
		} catch (IOException e) {
			System.out.println("isLineInFile: Erro a ler do bufferedReader");
		}
		return found;
	}
    

    /**
     * Apaga um diretorio recursivamente
     * @param dir
     * @return
     */
    public static boolean deleteDirRec(File dir) {
        File[] allFiles = dir.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                deleteDirRec(file);
            }
        }
        return dir.delete();
    }
    
    
    public static int getNumberLines(File file) {

		int i = 0;
		try (FileReader fr = new FileReader(file); 
		BufferedReader br = new BufferedReader(fr)) {
			@SuppressWarnings("unused")
			String readLine;
			while ((readLine = br.readLine()) != null) {
				i++;
			}
		}catch (FileNotFoundException e) {
			// Se o ficheiro não existe não há linhas para ler
			return 0;
		} catch (IOException e) {
			System.out.println("getNumberLines: Erro a ler do bufferedReader");
		}
		return i;
	}


	public static int userPassInFile(String user, String pass, File file) {

		int autAux = 1;
		try (FileReader fr = new FileReader(file); 
			BufferedReader br = new BufferedReader(fr)) {
		
			String readLine;
			while ((readLine = br.readLine()) != null) {
				String [] splitLine = readLine.split(":");
				if (splitLine[0].equals(user)) {
					if (splitLine[2].equals(pass))  {
						// user e passe certa
						autAux = 0;
					} else {
						// user certo, passe errada
						autAux = 2;
					}
					break;
				}	
			}
		} catch (FileNotFoundException e) {
			System.out.println("existsInFile: Erro a ler o ficheiro");
		} catch (IOException e) {
			System.out.println("existsInFile: Erro a ler do bufferedReader");
		}

		return autAux;
	} 

    public static void addNextLineToFile (String line, File file) {
    	
		try (FileWriter fw = new FileWriter(file, true); 
			PrintWriter pw = new PrintWriter(fw)) {

	        pw.println(line);
	        
		} catch (IOException e) {
			System.out.println("addNextLineToFile: Erro a abrir o filewriter");
		}
	}

	public static void removeLineFromFile (String line, File file) {
        	File tempFile = new File(file.getParentFile() + File.separator + AUXFILE);

			try (FileWriter writer = new FileWriter (tempFile, true);
				PrintWriter out = new PrintWriter(writer);
				BufferedReader br = new BufferedReader(new FileReader(file))) {
				
				String readLine = null;
				while ((readLine = br.readLine()) != null){
					if (!readLine.equals(line)){ 
						out.println(readLine);
					}
				}
				
			} catch (IOException e) {
				System.out.println("removeLineFromFile: Erro a abrir o FileWriter");
				e.printStackTrace();
			}

			//apagar original e renomwia nova
			if (!file.delete()) {
				System.out.println("removeLineFromFile: Erro a apagar o ficheiro antigo");
			}
			if (!tempFile.renameTo(file)) {
				System.out.println("removeLineFromFile: Erro a renomear o ficheiro antigo para o novo");
			}
			
	}
	
	public static void addAsFirstLine(String line, File file) {
    	File tempFile = new File(file.getParentFile() + File.separator + AUXFILE);

		try (FileWriter writer = new FileWriter (tempFile, true);
			PrintWriter out = new PrintWriter(writer);
			BufferedReader br = new BufferedReader(new FileReader(file))) {
			
			out.println(line);
			
			String readLine = null;
			while ((readLine = br.readLine()) != null){ 
				out.println(readLine);
			}
			
		} catch (IOException e) {
			System.out.println("addAsFirstLine: Erro a abrir o FileWriter");
		}

		if (!file.delete()) {
			System.out.println("addAsFirstLine: Erro a apagar o ficheiro antigo");
		}
		if (!tempFile.renameTo(file)) {
			System.out.println("addAsFirstLine: Erro a renomear o ficheiro antigo para o novo");
		}
		
}
	
	public static int createNewDir(File dir) {
		try{
			if (!dir.exists()) {
				dir.mkdir();
				System.out.println("Directory "+ dir.getName() + " created");
				return 0;
			} else {
				return -1;
			}
		}catch(SecurityException se){
			System.out.println("Não tem permissões para criar o Diretorio");
		}
		return -1;
	}

	/*
	 * Retorna uma lista em que cada elemento é uma string correspondente a uma linha
	 * do ficheiro
	 */
	public static String[] getListFromLines(File f) {
		String[] linesArray = null;
		try (
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr)) {
			
			String readLine = null;
			List<String> linesList = new ArrayList<>(); 
		    while ((readLine = br.readLine()) != null){
		    	linesList.add(readLine);
		    }
		    
			linesArray = new String[linesList.size()];
			linesList.toArray(linesArray);	
		} catch (FileNotFoundException e) {
			System.out.println("getFollowersList: Ficheiro nao existe");
		} catch (IOException e) {
			System.out.println("getFollowersList: erro a abrir o FileReader");	
		}
		return linesArray;
	}

	public static String[] nRecentPhotos(File photosInfo, int nPhotos) {
		String[] nPhotosInfo = null;
		try (FileReader fr = new FileReader(photosInfo);
			BufferedReader br = new BufferedReader(fr)){
			
			
			String readLine;
			List<String> allPhotosInfo = new ArrayList<>(); 
		    while ((readLine = br.readLine()) != null){
				String [] splitLine = readLine.split(":");
		    	allPhotosInfo.add(splitLine[1] + ":" + splitLine[2]);
			}
			
			nPhotosInfo = new String[allPhotosInfo.size()];
			if (allPhotosInfo.size() > nPhotos) {
				String[] aux = new String[allPhotosInfo.size()];
				allPhotosInfo.toArray(aux);
				nPhotosInfo = nLastFromStringArray(aux, nPhotos);
			}else{
				allPhotosInfo.toArray(nPhotosInfo);
			}
			
		} catch (IOException e) {
			System.out.println("nRecentPhotos: Erro a ler o ficheiro");
		}
		return nPhotosInfo;
	}

	private static String[] nLastFromStringArray(String [] array, int n) {
		String[] result = new String[n];
		for (int i = 0; i < result.length; i++) {
			result[0] = array[array.length-n+i];
		}
		return result;
	}
	
	/**
	 * Troca uma String existentLine por uma newLine no ficheiro f
	 * @param existentLine Linha que já existe no ficheiro
	 * @param newLine nova linha
	 * @param f ficheiro
	 * @return 0 caso linha exista e tenha sido trocada, -1 caso contrario
	 */
	public static int changeLine(String existentLine, String newLine, File f) {
    	File tempFile = new File(f.getParentFile() + File.separator + AUXFILE);
    	boolean change = false;
		try (FileWriter writer = new FileWriter (tempFile, true);
			PrintWriter out = new PrintWriter(writer);
			BufferedReader br = new BufferedReader(new FileReader(f))) {
			
			String readLine = null;
			while ((readLine = br.readLine()) != null){ 
				if (readLine.equals(existentLine)) {
	        		out.println(newLine);
	        		change = true;
	        	}else {
	        		out.println(readLine);	        		
	        	}
			}
			
		} catch (IOException e) {
			System.out.println("changeLine: Erro a abrir o FileWriter");
		}

		if (!f.delete()) {
			System.out.println("changeLine: Erro a apagar o ficheiro antigo");
		}
		if (!tempFile.renameTo(f)) {
			System.out.println("changeLine: Erro a renomear o ficheiro antigo para o novo");
		}
		
		if (change) {
			return 0;
		}
		return -1;
		
	}
	
	public static void getNewMessages(String user, File from, File to) {
		
    	File tempFile = new File(to.getParentFile() + File.separator + AUXFILE);
		try (FileWriter writer = new FileWriter (tempFile, true);
			PrintWriter out = new PrintWriter(writer);
			PrintWriter outToDecrypt = new PrintWriter(new FileWriter ("ToSend", true));
			BufferedReader brTo = new BufferedReader(new FileReader(to));
			BufferedReader brFrom1 = new BufferedReader(new FileReader(from));
			BufferedReader brFrom2 = new BufferedReader(new FileReader(from))) {
	
			
			String readLineTo;
			String lastLineTo = null;
			while ((readLineTo = brTo.readLine()) != null) {
				out.println(readLineTo);
				outToDecrypt.println(readLineTo);
				lastLineTo = readLineTo;
			}
			
			
			String readLineFrom = null;
			boolean perm = false;
			boolean change = false;
			/*
			 * Caso em que:
			 * existem varios users e a ultima msg ainda não foi apagada
			 *  Vais buscar todas as mensagens que foram escritas depois
			 *  da ultima msg do history do user
			 */
			while ((readLineFrom = brFrom1.readLine()) != null) {
				if (perm) {
					
					if (change && !readLineFrom.contains("Entrou:")) {
						out.println(readLineFrom);
						outToDecrypt.println(readLineFrom);
					}
					if (readLineFrom.equals(lastLineTo)) {
						change = true;
					}
					
				}
				if (readLineFrom.contains(ENTRADA + user)) {
					perm = true;
				}
			
			}
			
			if (!change) {
				perm = false;
				while ((readLineFrom = brFrom2.readLine()) != null) {
					if (perm && !readLineFrom.contains("Entrou:")) {
						out.println(readLineFrom);
						outToDecrypt.println(readLineFrom);
					}
					if (readLineFrom.contains(ENTRADA + user)) {
						perm = true;
					}
				
				}
			}
			
			
			if (!to.delete()) {
				System.out.println("changeLine: Erro a apagar o ficheiro antigo");
			}
			if (!tempFile.renameTo(to)) {
				System.out.println("changeLine: Erro a renomear o ficheiro antigo para o novo");
			}
			
		
		} catch (IOException e) {
			System.out.println("changeLine: Erro a abrir o FileWriter");
		}
		
	}
	
	
	/**
	 * Retorna a linha inteira que contem a string str
	 * @param str a string que a linha deve conter
	 * @param file o ficheiro a procurar
	 * @return null caso nao encontre, caso contrario a linha que contem str
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getLineContaining(String str, File file) {

		
		String line = null;
		try (FileReader fr = new FileReader(file); 
				BufferedReader br = new BufferedReader(fr)) {
			
			String readLine;
			while ((readLine = br.readLine()) != null) {
				if (readLine.contains(str)) {
					line = readLine;
					break;
				}
			}
		}  catch (FileNotFoundException e) {
			System.out.println("getLineContaining: Erro a ler o ficheiro");
		} catch (IOException e) {
			System.out.println("getLineContaining: Erro a ler do bufferedReader");
		}
		
        return line;
	}
	
	public static boolean isFirstLine(String str, File file) {

		try (FileReader fr = new FileReader(file); 
				BufferedReader br = new BufferedReader(fr)) {

			String readLine = br.readLine();
			return readLine.equals(str);
			
		}catch (FileNotFoundException e) {
			System.out.println("isFirstLine: Erro a ler o ficheiro");
		} catch (IOException e) {
			System.out.println("isFirstLine: Erro a ler do bufferedReader");
		}
		// nunca chega aqui
		return false;
		
	}
	
	
	
	/** Retorna a primeira linha de um ficheiro
	 * @param f ficheiro a ler
	 * @return a String com a linha, null caso não exista
	 */
	public static String getFirstLine(File f) {
		try (FileReader fr = new FileReader(f); 
				BufferedReader br = new BufferedReader(fr)) {

			return br.readLine();
					
		}catch (FileNotFoundException e) {
			// retorna null caso o ficheiro não exista
			return null;
		} catch (IOException e) {
			System.out.println("getFirstLine: Erro a ler do bufferedReader");
		}
		// nunca chega aqui
		return null;
	}

	
	public static byte[] getContentByteArray(File f) {
		try (FileInputStream fis = new FileInputStream(f)) {
			
			byte[] b = new byte[(int) f.length()];  
		    
			// i é o tamanho lido, se ainda houver algo para ler, 
			// ou -1 caso tenha sido lido tudo
			int i = fis.read(b); 
		    
		    if (i == -1) {
		    	System.out.println("getFileContentByteArray: Erro a ler todo o conteudo do ficheiro");
		    } else 
		    	return b;
		    
		} catch (IOException e) {
			System.out.println("getFileContentByteArray: Erro a ler do ficheiro");
		}
		return null;
	}
		
	
	
}
