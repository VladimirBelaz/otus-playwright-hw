package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusinessPage {

    private static final String URL = "https://otus.ru/uslugi-kompaniyam";

    private final Page page;

    // Локаторы (временные, нужно уточнить по реальному HTML)
    private final String detailsButtonSelector = "div:has-text('Не нашли нужный курс?') button:has-text('Подробнее')";
    private final String businessCoursePageTitleSelector = "h1:has-text('Разработка курса для бизнеса')";
    private final String directionItemsSelector = ".direction-item, .sc-..."; // замените на реальный селектор

    @Inject
    public BusinessPage(Page page) {
        this.page = page;
    }

    public BusinessPage open() {
        page.navigate(URL, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    public void clickDetailsButton() {
        page.locator(detailsButtonSelector).click();
        page.waitForTimeout(2000);
    }

    public void verifyBusinessCoursePageOpened() {
        // Проверка через URL
        assertThat(page).hasURL("https://otus.ru/razrabotka-kursa-dlya-biznesa");
        // Или проверка по тексту
        assertThat(page.locator(businessCoursePageTitleSelector)).isVisible();
    }

    public Locator getDirectionItems() {
        return page.locator(directionItemsSelector);
    }

    public void verifyDirectionsDisplayed() {
        assertThat(getDirectionItems().first()).isVisible();
        // Используем JUnit для проверки количества
        assertTrue(getDirectionItems().count() > 0);
    }

    public void clickFirstDirection() {
        getDirectionItems().first().click();
        page.waitForTimeout(2000);
    }

    public void verifyCatalogOpenedWithCategory(String categoryName) {
        assertThat(page).hasURL("https://otus.ru/catalog/courses");
        // Проверяем, что в фильтре направления выбрана нужная категория
        Locator selectedCategory = page.locator("div:has-text('Направление') + div p");
        assertThat(selectedCategory).hasText(categoryName);
    }
}