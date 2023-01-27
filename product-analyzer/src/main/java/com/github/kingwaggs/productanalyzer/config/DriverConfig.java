package com.github.kingwaggs.productanalyzer.config;

import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("pathFinder")
public class DriverConfig {

    private static final String WEB_DRIVER_ID = "webdriver.gecko.driver";

    @Bean
    public WebDriver driver() {
        System.setProperty(WEB_DRIVER_ID, PathFinder.getGeckodriver().toString());
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setHeadless(true);
        firefoxOptions.addPreference("webgl.force-enabled", "true");
        return new FirefoxDriver(firefoxOptions);
    }

}
