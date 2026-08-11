package config;

public final class TestConfig {

    private static final String BASE_URL_PROPERTY = "base.url";
    private static final String BROWSER_PROPERTY = "browser";
    private static final String HEADLESS_PROPERTY = "headless";

    private static final String DEFAULT_BASE_URL = "https://otus.ru";
    private static final String DEFAULT_BROWSER = "chromium";

    private TestConfig() {
    }

    public static String getBaseUrl() {
        return System.getProperty(BASE_URL_PROPERTY, DEFAULT_BASE_URL);
    }

    public static String getBrowser() {
        return System.getProperty(BROWSER_PROPERTY, DEFAULT_BROWSER);
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                System.getProperty(HEADLESS_PROPERTY, "false")
        );
    }
}