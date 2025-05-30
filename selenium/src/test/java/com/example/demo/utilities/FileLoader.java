package com.example.demo.utilities;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

/*
 * @Author: Neha Verma
 * @Desc: To read a file
 */

public class FileLoader {
	
	public static String readFile(String folder, String file) throws IOException {
		Charset cs = Charset.forName("UTF-8");
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(folder + "/" + file);
		try {
			Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		} finally {
			stream.close();
		}
	}
	
	public static String readFile(String file) throws IOException {
		Charset cs = Charset.forName("UTF-8");
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(file);
		try {
			Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		} finally {
			stream.close();
		}
	}

	/**
	 * Load properties file.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the properties
	 */
	public static Properties loadPropertiesFile(String fileName) {
		Properties prop = new Properties();
		try {
			String user_dir = System.getProperty("user.dir");

			// File path when executing from batch file
			String providedPath = System.getProperty("LocatorFile");
			if (providedPath != null) {
				user_dir = user_dir.replace("bin", "");
			}
			FileInputStream fs = new FileInputStream(
					user_dir + "//src//test//resources//" + fileName);
			
			prop.load(fs);
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * Sets the clipboard data.
	 * 
	 * @param string
	 *            the new clipboard data
	 */
	public static void setClipboardData(String string) {

		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	public static void uploadAutoIT() throws IOException {
		Runtime.getRuntime().exec("src/test/resources/AutoIT/UploadFile.exe");
	}
	

}