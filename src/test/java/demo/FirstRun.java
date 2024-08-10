package demo;

import java.io.File;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import utils.TestUtils;

public class FirstRun {

	String url = "http://www.google.com/";
	String searchTerm = "Joker";

	@Test
	void runTest() {
		String testChromeBinPath = "C:/Programs+/chrome-win64/chrome.exe";
		String chromeDriverBinPath = "src/test/resources/drivers-win64/chrome/chromedriver.exe";
		ChromeDriverService service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(chromeDriverBinPath)).build(); // Use project provided chromedriver
		ChromeOptions options = new ChromeOptions();
		options.setBinary(testChromeBinPath); // Use Chrome for Testing
		ChromeDriver driver = new ChromeDriver(service, options);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		driver.navigate().to(url);
		TestUtils.pause(3); // Let the user actually see something!
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys(searchTerm);
		TestUtils.pause(3); // Let the user actually see something!
		searchBox.submit();
		TestUtils.pause(3); // Let the user actually see something!
		System.out.println("Google search demo test passed.");

		driver.quit();
	}

}
