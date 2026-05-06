package drivers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import config.MobileConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

public class MobileDriver implements WebDriverProvider {

    private static final MobileConfig config = ConfigFactory.create(MobileConfig.class, System.getProperties());

    public static URL getAppiumServerUrl() {
        try {
            return new URL("http://127.0.0.1:4723/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();
        Configuration.pageLoadTimeout = -1;
        if (config.isRemote()) {

            options.setPlatformName("Android");
            options.setDeviceName(config.deviceName());
            options.setPlatformVersion(config.platformVersion());
            options.setApp(config.appPath());
            options.setAppPackage(config.appPackage());
            options.setAppActivity(config.appActivity());
            options.setAutomationName("UIAutomator2");


            MutableCapabilities bstackOptions = new MutableCapabilities();
            bstackOptions.setCapability("userName", config.browserstackUser());
            bstackOptions.setCapability("accessKey", config.browserstackKey());
            bstackOptions.setCapability("projectName", "Wikipedia Android");
            bstackOptions.setCapability("buildName", "browserstack-build-1");
            bstackOptions.setCapability("sessionName", "Search Test");
            bstackOptions.setCapability("debug", true);
            bstackOptions.setCapability("networkLogs", true);

            options.setCapability("bstack:options", bstackOptions);
        } else {
            options.setAutomationName("UIAutomator2")
                    .setPlatformName("Android")
                    .setPlatformVersion(config.platformVersion())
                    .setDeviceName(config.deviceName())
                    .setApp(getAppPath())
                    .setAppPackage(config.appPackage())
                    .setAppActivity(config.appActivity());
        }

        try {

            System.out.println("Connecting to BS Hub: " + config.remoteUrl());

            return new AndroidDriver(new URL(config.remoteUrl()), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error in server URL: " + config.remoteUrl(), e);
        }
    }

    private String getAppPath() {
        String appVersion = "app-alpha-universal-release.apk";
        String appUrl = "https://github.com/wikimedia/apps-android-wikipedia" +
                "/releases/download/latest/" + appVersion;
        String appPath = "src/test/resources/apps/" + appVersion;

        File app = new File(appPath);
        if (!app.exists()) {
            try (InputStream in = new URL(appUrl).openStream()) {
                copyInputStreamToFile(in, app);
            } catch (IOException e) {
                throw new AssertionError("Failed to download application", e);
            }
        }
        return app.getAbsolutePath();
    }
}

