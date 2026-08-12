package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;

public abstract class BasePage {

    protected final Page page;

    @Inject
    protected BasePage(Page page) {
        this.page = page;
    }

    protected void open(String path) {
        page.navigate(TestConfig.getBaseUrl() + path);
    }

    protected String getCurrentUrl() {
        return page.url();
    }

    protected String getPageTitle() {
        return page.title();
    }
}