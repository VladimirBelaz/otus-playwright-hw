package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPage  extends BasePage {
    private static final String FILTER = "div.sc-1w8jhjp-0.gitpfW";
    private static final String FILTER_VALUE = "p.sc-i4g3a4-0.byWUUn";
    private static final String DURATION_DISPLAY = "div.sc-1x9oq14-0.sc-1i4kf3x-0.eMZyoN.cUgsii";
    private static final String DURATION_TRACK = "div.sc-1kvgqt9-1.ifTLyl";
    private static final String DURATION_SLIDER = "[role='slider']";
    private static final String DURATION_COLLAPSE = ".ReactCollapse--collapse";
    private static final String DURATION_TOGGLE = "button.sc-1w8jhjp-3.dAywCQ";
    private static final String ARCHITECTURE_CHECKBOX = "#default-filter-checkbox-option-architecture";
    private static final String CATALOG_CONTAINER = "div.sc-18q05a6-1";
    private static final String COURSE_CARD = CATALOG_CONTAINER + " a.sc-zzdkm7-0";
    private static final String EMPTY_MESSAGE = "text=Ничего не найдено";

    private static final String PATH = "/catalog/courses";

    @Inject
    public CatalogPage(Page page) {
        super(page);
    }

    public CatalogPage open() {
        open(PATH);
        waitForCoursesLoaded();
        System.out.println("Страница каталога открыта");
        return this;
    }

    private Locator getDirectionFilter() {
        return page.locator(FILTER).filter(new Locator.FilterOptions().setHasText("Направление")).first();
    }

    private Locator getLevelFilter() {
        return page.locator(FILTER).filter(new Locator.FilterOptions().setHasText("Уровень")).first();
    }

    private Locator getDurationFilter() {
        return page.locator(FILTER).filter(new Locator.FilterOptions().setHasText("Продолжительность")).first();
    }

    private Locator getFilterValue(Locator filter) {
        return filter.locator(FILTER_VALUE);
    }

    public void verifyDefaultFilters() {
        assertThat(getFilterValue(getDirectionFilter())).hasText("Все направления");
        System.out.println("Проверка: выбрано 'Все направления'");
        assertThat(getFilterValue(getLevelFilter())).hasText("Любой уровень");
        System.out.println("Проверка: выбран 'Любой уровень сложности'");
        assertThat(getDurationFilter().locator(DURATION_DISPLAY)).hasText("От 0 до 15 месяцев");
        System.out.println("Проверка: продолжительность от 0 до 15 месяцев");
    }

    public void verifyDirectionFilterDefault() {
        assertThat(getFilterValue(getDirectionFilter())).hasText("Все направления");
    }

    public void verifyLevelFilterDefault() {
        assertThat(getFilterValue(getLevelFilter())).hasText("Любой уровень");
    }

    public void selectDuration(int fromMin, int toMax) {
        Locator durationFilter = getDurationFilter();
        Locator collapse = durationFilter.locator(DURATION_COLLAPSE);

        if (!collapse.isVisible()) {
            durationFilter.locator(DURATION_TOGGLE).click();
            assertThat(collapse).isVisible();
        }

        Locator sliders = durationFilter.locator(DURATION_TRACK).locator(DURATION_SLIDER);
        assertThat(sliders).hasCount(2);

        Locator leftSlider = sliders.nth(0);
        int leftMin = Integer.parseInt(leftSlider.getAttribute("aria-valuemin"));
        int leftMax = Integer.parseInt(leftSlider.getAttribute("aria-valuemax"));
        assertTrue(fromMin >= leftMin && fromMin <= leftMax,"Некорректное минимальное значение: " + fromMin);

        leftSlider.focus();
        leftSlider.press("Home");
        for (int value = leftMin; value < fromMin; value++) {
            leftSlider.press("ArrowRight");
        }
        assertThat(leftSlider).hasAttribute("aria-valuenow", String.valueOf(fromMin));

        sliders = durationFilter.locator(DURATION_TRACK).locator(DURATION_SLIDER);
        assertThat(sliders).hasCount(2);

        Locator rightSlider = sliders.nth(1);
        int rightMin = Integer.parseInt(rightSlider.getAttribute("aria-valuemin"));
        int rightMax = Integer.parseInt(rightSlider.getAttribute("aria-valuemax"));
        assertTrue(toMax >= rightMin && toMax <= rightMax, "Некорректное максимальное значение: " + toMax);

        rightSlider.focus();
        rightSlider.press("End");

        for (int value = rightMax; value > toMax; value--) {
            rightSlider.press("ArrowLeft");
        }

        assertThat(rightSlider).hasAttribute("aria-valuenow",String.valueOf(toMax));

        assertThat(durationFilter.locator(DURATION_DISPLAY)).hasText("От " + fromMin + " до " + toMax + " месяцев");

        System.out.println("Выбрана продолжительность от "+ fromMin + " до " + toMax + " месяцев");

        waitForCatalogAfterFilter();
        page.waitForTimeout(300);
    }


    public void selectArchitectureDirection() {
        Locator checkbox = page.locator(ARCHITECTURE_CHECKBOX);
        assertThat(checkbox).isVisible();

        checkbox.click();
        assertThat(getFilterValue(getDirectionFilter())).hasText("Архитектура");

        System.out.println("Выбрано направление 'Архитектура'");

        waitForCatalogAfterFilter();
        page.waitForTimeout(300);
    }

    public void verifyDirectionFilterArchitecture() {
        assertThat(getFilterValue(getDirectionFilter())).hasText("Архитектура");
        System.out.println("Проверка: выбрано 'Архитектура'");
    }


    public Locator getCourseCards() {
        return page.locator(COURSE_CARD);
    }

    public int getCoursesCount() {
        return getCourseCards().count();
    }

    public Set<String> getCourseCardsIds() {
        Set<String> result = new HashSet<>();
        Locator cards = getCourseCards();
        int count = cards.count();
        for (int i = 0; i < count; i++) {
            String href = cards.nth(i).getAttribute("href");

            if (href != null && !href.isBlank()) {result.add(href);}
        }
        return result;
    }

    public void verifyAllCoursesDurationInRange(int fromMin, int toMax) {
        Locator cards = getCourseCards();
        int count = cards.count();
        assertTrue(count > 0, "Нет карточек для проверки продолжительности");
        System.out.println("Проверяем " + count + " карточек после фильтра");

        for (int i = 0; i < count; i++) {
            Locator card = cards.nth(i);
            String cardText = card.innerText();
            assertTrue(cardText != null && !cardText.isBlank(),
                    "У карточки №"
                            + (i + 1)
                            + " отсутствует текст"
            );

            int months = extractMonths(cardText);
            assertTrue(months >= fromMin && months <= toMax,
                    "Курс №"
                            + (i + 1)
                            + " имеет продолжительность "
                            + months
                            + " месяцев. "
                            + "Ожидалось от "
                            + fromMin
                            + " до "
                            + toMax
                            + ". Текст: "
                            + cardText.trim()
            );
        }
        System.out.println("Все курсы имеют продолжительность от " + fromMin + " до " + toMax + " месяцев");
    }

    private int extractMonths(String text) {
        Matcher matcher = Pattern.compile("(\\d+)\\s*месяц").matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("Не удалось извлечь продолжительность из текста: " + text);
    }

    public void resetFilters() {
        Locator resetButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Очистить фильтры"));
        assertThat(resetButton).isVisible();
        assertThat(resetButton).isEnabled();
        System.out.println("Нажимаем кнопку 'Очистить фильтры'");

        resetButton.click();
        assertThat(getFilterValue(getDirectionFilter())).hasText("Все направления");
        assertThat(getFilterValue(getLevelFilter())).hasText("Любой уровень");
        assertThat(getDurationFilter().locator(DURATION_DISPLAY)).hasText("От 0 до 15 месяцев");
        System.out.println("Фильтры сброшены");

        waitForCoursesLoaded();
    }

    public void verifyFiltersReset() {
        verifyDirectionFilterDefault();
        verifyLevelFilterDefault();
        assertThat(getDurationFilter().locator(DURATION_DISPLAY)).hasText("От 0 до 15 месяцев");
        System.out.println("Проверка: фильтры сброшены к значениям по умолчанию");
    }

    private void waitForCoursesLoaded() {
        page.waitForFunction(
                """
                () => {
                    const cards =
                        document.querySelectorAll(
                            'div.sc-18q05a6-1 a.sc-zzdkm7-0'
                        );

                    if (cards.length > 0) {
                        return true;
                    }

                    const bodyText =
                        document.body
                            ? document.body.innerText
                            : '';

                    return bodyText.includes(
                        'Ничего не найдено'
                    );
                }
                """
        );
    }

    private void waitForCatalogAfterFilter() {
        page.waitForTimeout(1000);
        page.waitForFunction(
                """
                () => {
                    const cards =
                        document.querySelectorAll(
                            'div.sc-18q05a6-1 a.sc-zzdkm7-0'
                        );

                    const skeletons =
                        document.body &&
                        document.body.innerText
                            ? document.body.innerText.includes('Skeleton')
                            : false;

                    return cards.length > 0 && !skeletons;
                }
                """
        );
        System.out.println("Каталог обновился после изменения фильтра");
    }

    public void verifyCoursesDisplayed() {
        Locator cards = getCourseCards();
        Locator emptyMessage = page.locator(EMPTY_MESSAGE);
        if (emptyMessage.isVisible()) {
            return;
        }

        assertTrue(cards.count() > 0, "Курсы не отображаются");

        System.out.println("Курсы отображаются в каталоге");
    }
}