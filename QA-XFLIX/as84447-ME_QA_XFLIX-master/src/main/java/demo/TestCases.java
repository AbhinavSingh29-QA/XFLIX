package demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    private static RemoteWebDriver driver;
    private WebDriverWait wait;

    public TestCases() throws MalformedURLException {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(120).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        // Set browser to maximize and wait
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);
    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        if (driver != null) {
            driver.quit();
        }
    }

    public void testCase01() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-qa.vercel.app/");

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("xflix")) {
            System.out.println("testCase01: PASS");
        } else {
            System.out.println("testCase01: FAIL");
        }
        System.out.println("End Test case: testCase01");
    }

    public void testCase02() {
        System.out.println("Start Test case: testCase02");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement searchBox = driver.findElement(By.className("search-input"));
        searchBox.sendKeys("frameworks");

        List<WebElement> titleElements = driver.findElements(By.className("video-title"));
        for (WebElement titleElement : titleElements) {
            String text = titleElement.getText();
            if (text.toLowerCase().contains("frameworks")) {
                System.out.println("testCase02: Valid Search: PASS");
            } else {
                System.out.println("testCase02: Valid Search: FAIL");
            }

        }
        searchBox.clear();
        searchBox.sendKeys("selenium");

        WebElement noSearchFound = driver.findElement(By.xpath(
                "//div[text()='No Search is found :( . Please try searching some other movies.']"));
        if (noSearchFound.isDisplayed()) {
            System.out.println("testCase02: Valid Search: PASS");
        } else {
            System.out.println("testCase02: Valid Search: FAIL");
        }

        System.out.println("End Test case: testCase02");
    }
    
    public void testCase03() {
        System.out.println("Start Test case: testCase03");
        driver.get("https://xflix-qa.vercel.app/");

        List<WebElement> titleElementsBeforeFilter = driver.findElements(By.className("video-title"));
        List<String> titlesBeforeFilter = new ArrayList<>();
        for(WebElement titElement : titleElementsBeforeFilter){
            String title = titElement.getText();
            titlesBeforeFilter.add(title);
        }

        WebElement filterDropdown = driver.findElement(By.id("sortBySelect"));
        Select select = new Select(filterDropdown);
        select.selectByVisibleText("Sort By: View Count");

        List<WebElement> titleElementsAfterFilter = driver.findElements(By.className("video-title"));
        List<String> titlesAfterFilter = new ArrayList<>();
        for (WebElement titleElement : titleElementsAfterFilter){
            String title = titleElement.getText();
            titlesAfterFilter.add(title);
        }

        if(titlesAfterFilter.equals(titlesBeforeFilter)){
            System.out.println("testCase03: FAIL");
        }else{
            System.out.println("testCase03: PASS");
        }
        
        System.out.println("End Test case: testCase03");
    }

    public void testCase04() {
        System.out.println("Start Test case: testCase04");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement uploadButton = driver.findElement(By.xpath("//button[@class='header-btn btn-upload']"));
        uploadButton.click();
        WebElement uploadVideoButton = driver.findElement(By.xpath("//button[text()='upload video']"));
        uploadVideoButton.click();

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        if (alertText.contains("should not be empty")) {
            System.out.println("Test Case 04: Alert Message: Passed");
        } else {
            System.out.println("Test Case 04: Alert Message: Failed");
        }
        alert.accept();

        WebElement videoLink = driver.findElement(By.name("videoLink"));
        WebElement thumbNailImage = driver.findElement(By.name("previewImage"));
        WebElement tittle = driver.findElement(By.name("title"));

        videoLink.sendKeys("https://www.youtube.com/embed/GWfYHEuWh-k?si=UDwVjalFUBUEXxUT0");
        thumbNailImage.sendKeys("https://www.crio.do/blog/content/images/size/w1000/2020/08/Aug_04--1-.png");
        tittle.sendKeys("ABCD");

        Select genreDropDown = new Select(driver.findElement(By.id("genre-modal-dropdown")));
        genreDropDown.selectByVisibleText("Sports");

                Select ageDropDown = new Select(driver.findElement(By.id("age-modal-dropdown")));
        ageDropDown.selectByVisibleText("18+");

        WebElement dateField = driver.findElement(By.name("releaseDate"));
        dateField.sendKeys("25-07-2025");

        uploadVideoButton.click();

        Alert alert2 = driver.switchTo().alert();
        String alertSuccessText = alert2.getText();

        if (alertSuccessText.contains("Video Posted Successfully!")) {
            System.out.println("Test Case 04: Video Upload: Passed");
        } else {
            System.out.println("Test Case 04: Video Upload: Failed");
        }
        alert2.accept();

        System.out.println("End Test case: testCase04");
    }
    

    public void testCase05(){
        System.out.println("Start Test case: testCase05");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement firstVideoTitle = driver.findElement(By.className("video-title"));
        firstVideoTitle.click();
        WebElement likeElementBefore = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        String likeCountBefore = likeElementBefore.getText();
        likeElementBefore.click();
        
        String currentUrl = driver.getCurrentUrl();
        driver.switchTo().newWindow(WindowType.TAB);

        Set<String> windowsSet = driver.getWindowHandles();
        List<String> windowsList = new ArrayList<>(windowsSet);
        
        driver.switchTo().window(windowsList.get(1));
        driver.get(currentUrl);

        WebElement likeElementAfter = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        String likeCountAfter = likeElementAfter.getText();
        if(!likeCountAfter.equals(likeCountBefore)){
            System.out.println("testCase05: PASS");
        }else{
            System.out.println("testCase05: FAIL");
        }
        driver.close();
        driver.switchTo().window(windowsList.get(0));

        System.out.println("End Test case: testCase05");

    }
















}
