package config;

public final class Config {

    private Config() {
    }

    public static String baseUrl() {
        return System.getProperty(
                "baseUrl",
                "https://otus.ru"
        );
    }
}