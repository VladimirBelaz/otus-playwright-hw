package extensions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import di.PageModule;
import di.PlaywrightModule;
import di.TestResourcesModule;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlaywrightExtension implements BeforeEachCallback, AfterEachCallback {

    private static final Path TRACE_DIRECTORY = Path.of("traces");

    private Injector injector;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Files.createDirectories(TRACE_DIRECTORY);

        injector = Guice.createInjector(
                new PlaywrightModule()
        );

        playwright = injector.getInstance(Playwright.class);
        browser = injector.getInstance(Browser.class);

        browserContext = browser.newContext();

        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = browserContext.newPage();

        injector = injector.createChildInjector(
                new TestResourcesModule(
                        browserContext,
                        page
                ),
                new PageModule()
        );

        injector.injectMembers(
                context.getRequiredTestInstance()
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        String testName = context.getRequiredTestMethod().getName();

        Path tracePath = TRACE_DIRECTORY.resolve(
                testName + ".zip"
        );

        try {
            if (browserContext != null) {
                browserContext.tracing().stop(
                        new Tracing.StopOptions()
                                .setPath(tracePath)
                );
            }
        } finally {
            if (browserContext != null) {
                browserContext.close();
            }

            if (browser != null) {
                browser.close();
            }

            if (playwright != null) {
                playwright.close();
            }
        }
    }

    public Injector getInjector() {
        return injector;
    }

    public Playwright getPlaywright() {
        return playwright;
    }

    public Browser getBrowser() {
        return browser;
    }

    public BrowserContext getBrowserContext() {
        return browserContext;
    }

    public Page getPage() {
        return page;
    }
}