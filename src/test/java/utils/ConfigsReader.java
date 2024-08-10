package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * This <ConfigsReader> class reads data from '.properties' files in
 * "src/test/resources/configs" folder
 */
public class ConfigsReader {

	private static final String DIR_PATH = "src/test/resources/configs/";

	/**
	 * Return a <String> value, default to null value
	 */
	public static String getTextValue(String fileName, String key) {
		String filePath = DIR_PATH + fileName + ".properties";
		Properties p = load(filePath);
		return p.containsKey(key) ? (p.getProperty(key).isEmpty() ? null : p.getProperty(key)) : null;
	}

	private static Properties load(String filePath) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(filePath));
		} catch (Exception e) {
			throw new RuntimeException("No such file in configs folder is discovered.", e);
		}
		return p;
	}

	private static final String ERROR_TEXT = "There is no such key in this config file.";

	public static boolean getBooleanValue(String fileName, String key) {
		String target = getTextValue(fileName, key);
		assertNotNull(target, ERROR_TEXT);
		boolean result;
		try {
			result = Boolean.parseBoolean(target.toLowerCase());
		} catch (Exception e) {
			throw new RuntimeException("This key does not have a boolean value.", e);
		}
		return result;
	}

	public static int getIntNumValue(String fileName, String key) {
		String target = getTextValue(fileName, key);
		assertNotNull(target, ERROR_TEXT);
		int result;
		try {
			result = Integer.parseInt(target.toLowerCase());
		} catch (Exception e) {
			throw new RuntimeException("This key does not have an integer value.", e);
		}
		return result;
	}

}
