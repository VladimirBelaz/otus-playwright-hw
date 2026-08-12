package di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class TestResourcesModule extends AbstractModule {

    private final BrowserContext browserContext;
    private final Page page;

    public TestResourcesModule(BrowserContext browserContext, Page page) {
        this.browserContext = browserContext;
        this.page = page;
    }

    @Provides
    @Singleton
    public BrowserContext provideBrowserContext() {
        return browserContext;
    }

    @Provides
    @Singleton
    public Page providePage() {
        return page;
    }
}