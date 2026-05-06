package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "classpath:config/${env}.properties",
        "classpath:config/local.properties"
})
public interface MobileConfig extends Config {
    @Key("platformName")
    @DefaultValue("Android")
    String platformName();

    @Key("deviceName")
    @DefaultValue("")
    String deviceName();

    @Key("platformVersion")
    @DefaultValue("")
    String platformVersion();

    @Key("appPath")
    @DefaultValue("src/test/resources/apps/app-alpha-universal-release.apk")
    String appPath();

    @Key("appPackage")
    @DefaultValue("org.wikipedia.alpha")
    String appPackage();

    @Key("appActivity")
    @DefaultValue("org.wikipedia.main.MainActivity")
    String appActivity();

    @Key("isRemote")
    @DefaultValue("false")
    boolean isRemote();

    @Key("browserstack.user")
    @DefaultValue("")
    String browserstackUser();

    @Key("browserstack.key")
    @DefaultValue("")
    String browserstackKey();

    @Key("remoteUrl")
    @DefaultValue("http://185.154.53.106:4444/wd/hub")
    String remoteUrl();

}

