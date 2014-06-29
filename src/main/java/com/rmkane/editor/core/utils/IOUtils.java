package com.rmkane.editor.core.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.dd.plist.BinaryPropertyListWriter;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;

public class IOUtils {
	private static final Logger LOG = Logger.getLogger(IOUtils.class);

	public static String decode(File file) {
		NSObject obj = null;

		if (file != null) {
			try {
				obj = PropertyListParser.parse(file);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			} catch (PropertyListFormatException e) {
				LOG.error(e.getMessage(), e);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			} catch (ParserConfigurationException e) {
				LOG.error(e.getMessage(), e);
			} catch (SAXException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		if (obj != null) {
			return obj.toXMLPropertyList();
		}

		return null;
	}

	public static NSObject parse(String xmlString) {
		InputStream stream = new ByteArrayInputStream(
				xmlString.getBytes(StandardCharsets.UTF_8));
		NSObject root = null;

		try {
			root = PropertyListParser.parse(stream);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (PropertyListFormatException e) {
			LOG.error(e.getMessage(), e);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage(), e);
		} catch (SAXException e) {
			LOG.error(e.getMessage(), e);
		}

		return root;
	}

	public static void write(File f, NSObject root) {
		try {
			BinaryPropertyListWriter.write(f, root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File write(File file, String xmlStr) {
		try {
			FileWriter fileWriter = null;

			fileWriter = new FileWriter(file);
			fileWriter.write(xmlStr);
			fileWriter.close();
		} catch (IOException e) {
			LOG.fatal(null, e);
		}

		try {
			BinaryPropertyListWriter.write(file, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}
}
