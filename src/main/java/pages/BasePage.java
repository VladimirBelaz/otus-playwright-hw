package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;

public abstract class BasePage {

    @Inject
    protected Page page;

    protected void open(String path) {
        page.navigate(TestConfig.getBaseUrl() + path);
    }

    protected void openUrl(String url) {
        page.navigate(url);
    }

    protected String getCurrentUrl() {
        return page.url();
    }

    protected String getPageTitle() {
        return page.title();
    }
}