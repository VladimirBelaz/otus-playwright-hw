package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusinessPage extends BasePage {

    private static final String PATH = "/uslugi-kompaniyam";

    private final String detailsButtonSelector = "div:has-text('Не нашли нужный курс?') button:has-text('Подробнее')";
    private final String businessCoursePageTitleSelector = "h1:has-text('Разработка курса для бизнеса')";
    private final String directionItemsSelector = ".direction-item, .sc-...";

    @Inject
    public BusinessPage(Page page) {
        super(page);
    }

    public BusinessPage open() {
        open(PATH);
        return this;
    }

    public void clickDetailsButton() {
        page.locator(detailsButtonSelector).click();
        page.waitForTimeout(2000);
    }

    public void verifyBusinessCoursePageOpened() {
        assertThat(page).hasURL("https://otus.ru/razrabotka-kursa-dlya-biznesa");
        assertThat(page.locator(businessCoursePageTitleSelector)).isVisible();
    }

    public Locator getDirectionItems() {
        return page.locator(directionItemsSelector);
    }

    public void verifyDirectionsDisplayed() {
        assertThat(getDirectionItems().first()).isVisible();
        assertTrue(getDirectionItems().count() > 0);
    }

    public void clickFirstDirection() {
        getDirectionItems().first().click();
        page.waitForTimeout(2000);
    }

    public void verifyCatalogOpenedWithCategory(String categoryName) {
        assertThat(page).hasURL("https://otus.ru/catalog/courses");
        Locator selectedCategory = page.locator("div:has-text('Направление') + div p");
        assertThat(selectedCategory).hasText(categoryName);
    }
}