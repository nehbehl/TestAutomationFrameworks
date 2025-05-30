package com.example.demo.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


/*
 * 
 */

public class LocatorReader {
	private Document doc;
	
	
	/**
	 * Read Locator from file
	 * 
	 * @param locatorFile: passing locator file from folder repository
	 */
	public LocatorReader(String locatorFile) {
		SAXReader reader = new SAXReader();
		try {
			String locatorFilePath = System.getProperty(locatorFile);
			InputStream resourceStream;

			// This path is not null when execution is done from automation jar
			if (locatorFilePath == null) {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				resourceStream = loader.getResourceAsStream("repository/" + locatorFile);
				
			} else {

				resourceStream = new FileInputStream(locatorFilePath);
			}
			doc = reader.read(resourceStream);
			resourceStream.close();
		} catch (DocumentException e) {
			TestListeners.extentLogger.fail(e);
		} catch (Exception e) {

		}
	}

	public String getPath() {

		String path = "";
		File file = new File("");
		String absolutePathOfFile = file.getAbsolutePath();
		path = absolutePathOfFile.replaceAll("\\\\+", "/");
		return path;
	}

	public String getLocator(String locator) {
		return doc.selectSingleNode("//" + locator.replace('.', '/')).getText();
	}

}
