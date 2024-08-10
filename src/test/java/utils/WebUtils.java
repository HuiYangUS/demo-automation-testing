package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This <WebUtils> class relies on <Selenium> <WebDriver> objects
 */
public class WebUtils {

	private WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private JavascriptExecutor js;

	public WebUtils(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.getTestConfigWaitTime()));
		actions = new Actions(driver);
		js = (JavascriptExecutor) driver;
	}

	public void printDriverData() {
		assertNotNull(driver, "Driver cannot be null.");
		Capabilities data = null;
		String driverVersion = null;
		if (driver instanceof ChromeDriver) {
			data = ((ChromeDriver) driver).getCapabilities();
			@SuppressWarnings("unchecked")
			Map<String, String> chrome = (Map<String, String>) data.getCapability("chrome");
			driverVersion = chrome.get("chromedriverVersion").replaceAll("[(].*[)]", "");
		}
		if (driver instanceof FirefoxDriver) {
			data = ((FirefoxDriver) driver).getCapabilities();
			driverVersion = "geckodriver - " + (String) data.getCapability("moz:geckodriverVersion");
		}
		if (driver instanceof EdgeDriver) {
			data = ((EdgeDriver) driver).getCapabilities();
			@SuppressWarnings("unchecked")
			Map<String, String> msedge = (Map<String, String>) data.getCapability("msedge");
			driverVersion = msedge.get("msedgedriverVersion").replaceAll("[(].*[)]", "");
		}
		if (driver instanceof SafariDriver)
			data = ((SafariDriver) driver).getCapabilities();
		assertNotNull(data, "No driver data is available.");
		String browserName = data.getBrowserName();
		String browserVersion = data.getBrowserVersion();
		System.out.println("Browser name: " + browserName);
		System.out.println("Browser version: " + browserVersion);
		System.out.println("Driver version: " + driverVersion);
	}

	public void jsClick(WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	public void jsClick(By locator) {
		jsClick(driver.findElement(locator));
	}

	/**
	 * When an element has the "autocomplete" on
	 */
	public void forceClear(WebElement element) {
		while (!element.getAttribute("value").isEmpty())
			element.sendKeys(Keys.BACK_SPACE);
	}

	public void forceClear(By locator) {
		forceClear(driver.findElement(locator));
	}

	/**
	 * Change the border of an element to be thick and red
	 */
	public void elementOnFocus(WebElement element) {
		// Set this element's border to be red and thick
		js.executeScript("arguments[0].style.borderColor = 'red'; arguments[0].style.borderWidth = 'thick';", element);
		TestUtils.pause(2);
		// Reset this element's border
		js.executeScript("arguments[0].style.borderColor = ''; arguments[0].style.borderWidth = '';", element);
		TestUtils.pause(1);
	}

	public void elementOnFocus(By locator) {
		elementOnFocus(driver.findElement(locator));
	}

	public Actions useMouseOrKey() {
		return actions;
	}

	public void savesScreenshot() {
		savesScreenshot(null, false);
	}

	/**
	 * Study material
	 */
	public void testEnvLogin() {
		fail("Legacy: no longer required but remain as study material.");
		wait.until(ExpectedConditions.urlContains(ConfigsReader.getTextValue("test-env", "url")));
		String originWindow = driver.getWindowHandle();
		wait.until(
				ExpectedConditions.elementToBeClickable(By.cssSelector(ConfigsReader.getTextValue("test-env", "id"))))
				.click();
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> windows = driver.getWindowHandles();
		String targetWindow = null;
		for (String window : windows)
			if (!window.equals(originWindow)) {
				targetWindow = window;
				break;
			}
		driver.switchTo().window(targetWindow);
		driver.findElement(By.name("login")).sendKeys(ConfigsReader.getTextValue("test-env", "username"));
		driver.findElement(By.name("password")).sendKeys(ConfigsReader.getTextValue("test-env", "password"));
		driver.findElement(By.name("commit")).submit();
		wait.until(ExpectedConditions.numberOfWindowsToBe(1));
		driver.switchTo().window(originWindow);
		wait.until(ExpectedConditions.urlContains(ConfigsReader.getTextValue("config", "url")));
	}

	/**
	 * Takes a screenshot and stores in the "target" folder
	 */
	public void savesScreenshot(String postfix, boolean useTimeStamp) {
		String tail = "-" + postfix;
		if (postfix == null)
			tail = "";
		if (useTimeStamp)
			tail += "-" + TestUtils.getDateString() + "-" + TestUtils.getTimeStamp();
		TakesScreenshot cam = (TakesScreenshot) driver;
		File imgData = cam.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(imgData, new File(String.format("target/webpage-screenshots/screenshot%s.png", tail)));
		} catch (IOException e) {
			throw new RuntimeException("Failed to capture the screenshot.", e);
		}
	}

}
