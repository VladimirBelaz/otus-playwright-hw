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
        BrowserType browserType = getBrowserType(playwright);
        return browserType.launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(TestConfig.isHeadless())
        );
    }

    private BrowserType getBrowserType(Playwright playwright) {
        String browserName = TestConfig.getBrowser().toLowerCase();
        if ("chromium".equals(browserName)) {
            return playwright.chromium();
        }
        if ("firefox".equals(browserName)) {
            return playwright.firefox();
        }
        if ("webkit".equals(browserName)) {
            return playwright.webkit();
        }
        throw new IllegalArgumentException(
                "Unsupported browser: " + TestConfig.getBrowser()
        );
    }
}