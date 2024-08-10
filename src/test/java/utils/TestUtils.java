package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;

import io.cucumber.datatable.DataTable;

public class TestUtils {

	public static void pause(int time) {
		try {
			Thread.sleep(time * 1000L);
		} catch (InterruptedException e) {
			// Do nothing
			System.out.println("Thread failed to sleep.");
		}
	}

	public static void closeAllWindows(WebDriver driver) {
		int num = driver.getWindowHandles().size();
		for (int i = 0; i < num; i++)
			driver.close();
	}

	public static Map<String, String> stringToMap(String text) {
		String[] textPairs = text.substring(1, text.length() - 1).split(", ");
		Map<String, String> data = new TreeMap<>();
		for (String textPair : textPairs) {
			String[] dataPair = textPair.split("=");
			data.put(dataPair[0], dataPair[1]);
		}
		assertEquals(data.toString(), text, "Invalid string format for conversion.");
		return data;
	}

	public static boolean isValidDate(String year, String month, String dayOfMonth) {
		try {
			@SuppressWarnings("unused")
			LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(dayOfMonth));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static long getTimeStamp() {
		return Timestamp.valueOf(LocalDateTime.now()).getTime();
	}

	public static String getDateString() {
		return LocalDate.now().toString();
	}

	public static String getFormattedDateString() {
		return getDateString().replace("-", "");
	}

	public static int getTestConfigWaitTime() {
		return ConfigsReader.getIntNumValue("config", "waitTime");
	}

	public static String getTestConfigBrowserName() {
		String data = ConfigsReader.getTextValue("config", "browser");
		if (data == null)
			throw new RuntimeException("No browser data from config file.");
		return data.toLowerCase();
	}

	public static boolean isDemoTest() {
		return ConfigsReader.getBooleanValue("config", "demo");
	}

	public static String getOS() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isWindows() {
		return getOS().contains("windows");
	}

	public static boolean isMac() {
		return getOS().contains("mac");
	}

	public static boolean isLinux() {
		return getOS().contains("linux");
	}

	public static String getCurrentDir() {
		return System.getProperty("user.dir").replace("\\", "/");
	}

	/**
	 * Return the first row of a cucumber data table as map
	 */
	public static Map<String, String> getDataRow(DataTable dataTable) {
		if (!dataTable.asMaps().isEmpty())
			return dataTable.asMaps().get(0);
		throw new RuntimeException("This cucumber data table does not have enough data.");
	}

}
