package di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import config.TestConfig;

public class PlaywrightModule extends AbstractModule {

    @Provides
    @Singleton
    public Playwright providePlaywright() {
        return Playwright.create();
    }

    @Provides
    @Singleton
    public Browser provideBrowser(Playwright playwright) {
        BrowserType browserType = switch (TestConfig.getBrowser().toLowerCase()) {
            case "chromium" -> playwright.chromium();
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + TestConfig.getBrowser()
            );
        };

        return browserType.launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(TestConfig.isHeadless())
        );
    }
}