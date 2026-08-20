package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;

public class BusinessPage extends BasePage {

    private static final String PATH = "/b2b";
    private final String catalogLinkSelector = "a[href='/catalog/courses']:has-text('Каталог курсов')";

    @Inject
    public BusinessPage(Page page) {
        super(page);
    }

    public BusinessPage open() {
        open(PATH);
        return this;
    }

    public CatalogPage clickCatalogLink() {
        Page newPage = page.context().waitForPage(() -> {
            page.locator(catalogLinkSelector).click();
        });
        newPage.waitForURL("**/catalog/courses");
        return new CatalogPage(newPage);
    }
}