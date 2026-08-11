package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPage {

    private static final String URL = "https://otus.ru/catalog/courses";

    private final Page page;

    private final String directionFilterContainer = "div.sc-1w8jhjp-0.gitpfW:has(p:has-text('Направление'))";
    private final String levelFilterContainer = "div.sc-1w8jhjp-0.gitpfW:has(p:has-text('Уровень'))";
    private final String durationFilterContainer = "div.sc-1w8jhjp-0.gitpfW:has(p:has-text('Продолжительность'))";

    private final String filterValueSelector = "p.sc-i4g3a4-0.byWUUn";

    private final String checkboxArchitecture = "#default-filter-checkbox-option-architecture";

    private final String durationDisplaySelector = "div.sc-1x9oq14-0.sc-1i4kf3x-0.eMZyoN.cUgsii";

    private final String resetButtonSelector = "button.sc-1qig7zt-0.czpnNJ.sc-1x9oq14-0-I.sc-it2lwt-1.nWKUL.jsqshx";

    private final String catalogContainerSelector = "div.sc-18q05a6-1";
    private final String courseCardSelector = catalogContainerSelector + " a.sc-zzdkm7-0";
    private final String emptyMessageSelector = "text=Ничего не найдено";

    @Inject
    public CatalogPage(Page page) {
        this.page = page;
    }

    public CatalogPage open() {
        page.navigate(URL, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        System.out.println("✅ Страница каталога открыта");
        return this;
    }

    private Locator getDirectionFilter() {
        return page.locator(directionFilterContainer);
    }

    private Locator getLevelFilter() {
        return page.locator(levelFilterContainer);
    }

    private Locator getDurationFilter() {
        return page.locator(durationFilterContainer);
    }

    private Locator getFilterValue(Locator filterContainer) {
        return filterContainer.locator(filterValueSelector);
    }

    public void verifyDirectionFilterDefault() {
        Locator value = getFilterValue(getDirectionFilter());
        assertThat(value).hasText("Все направления");
        System.out.println("✅ Проверка: в фильтре направления выбрано 'Все направления'");
    }

    public void verifyLevelFilterDefault() {
        Locator value = getFilterValue(getLevelFilter());
        assertThat(value).hasText("Любой уровень");
        System.out.println("✅ Проверка: в фильтре уровня выбрано 'Любой уровень сложности'");
    }

    public void selectDuration(int fromMin, int toMax) {
        Locator durationFilter = getDurationFilter();
        Locator collapseContent = durationFilter.locator(".ReactCollapse--collapse");

        // Раскрываем фильтр, если свёрнут
        if (!collapseContent.isVisible()) {
            Locator toggleButton = durationFilter.locator("button.sc-1w8jhjp-3.dAywCQ");
            if (toggleButton.isVisible()) {
                toggleButton.click();
            }
        }

        // Прокручиваем к дорожке слайдера
        Locator track = durationFilter.locator("div.sc-1kvgqt9-1.ifTLyl");
        track.scrollIntoViewIfNeeded();
        assertThat(track).isVisible();

        // Получаем координаты через JavaScript
        Object result = page.evaluate("() => {\n" +
                "  const handles = document.querySelectorAll('[role=\"slider\"]');\n" +
                "  if (handles.length < 2) return null;\n" +
                "  const track = document.querySelector('div.sc-1kvgqt9-1.ifTLyl');\n" +
                "  const trackRect = track.getBoundingClientRect();\n" +
                "  const leftRect = handles[0].getBoundingClientRect();\n" +
                "  const rightRect = handles[1].getBoundingClientRect();\n" +
                "  return {\n" +
                "    trackX: trackRect.left,\n" +
                "    trackWidth: trackRect.width,\n" +
                "    y: leftRect.top + leftRect.height/2,\n" +
                "    leftX: leftRect.left + leftRect.width/2,\n" +
                "    rightX: rightRect.left + rightRect.width/2\n" +
                "  };\n" +
                "}");
        if (result == null) {
            throw new IllegalStateException("Не удалось найти слайдер или ползунки");
        }

        java.util.Map<String, Number> coords = (java.util.Map<String, Number>) result;
        double trackX = coords.get("trackX").doubleValue();
        double trackWidth = coords.get("trackWidth").doubleValue();
        double y = coords.get("y").doubleValue();
        double leftX = coords.get("leftX").doubleValue();
        double rightX = coords.get("rightX").doubleValue();

        double minVal = 0;
        double maxVal = 15;

        double targetLeftX = trackX + (fromMin - minVal) / (maxVal - minVal) * trackWidth;
        double targetRightX = trackX + (toMax - minVal) / (maxVal - minVal) * trackWidth;

        page.mouse().move(leftX, y);
        page.mouse().down();
        page.mouse().move(targetLeftX, y);
        page.mouse().up();

        page.mouse().move(rightX, y);
        page.mouse().down();
        page.mouse().move(targetRightX, y);
        page.mouse().up();

        page.evaluate("() => {\n" +
                "  const handles = document.querySelectorAll('[role=\"slider\"]');\n" +
                "  if (handles.length >= 2) {\n" +
                "    handles[0].dispatchEvent(new Event('input', { bubbles: true }));\n" +
                "    handles[0].dispatchEvent(new Event('change', { bubbles: true }));\n" +
                "    handles[1].dispatchEvent(new Event('input', { bubbles: true }));\n" +
                "    handles[1].dispatchEvent(new Event('change', { bubbles: true }));\n" +
                "  }\n" +
                "}");

        Locator display = durationFilter.locator(durationDisplaySelector);
        assertThat(display).hasText("От " + fromMin + " до " + toMax + " месяцев");
        System.out.println("✅ Выбрана продолжительность от " + fromMin + " до " + toMax + " месяцев");

        waitForCoursesLoaded();
    }

    public void selectArchitectureDirection() {
        page.locator(checkboxArchitecture).click();
        page.waitForTimeout(1000);
        assertThat(getFilterValue(getDirectionFilter())).hasText("Архитектура");
        System.out.println("✅ Выбрано направление 'Архитектура'");
        waitForCoursesLoaded();
    }

    public void verifyDirectionFilterArchitecture() {
        assertThat(getFilterValue(getDirectionFilter())).hasText("Архитектура");
        System.out.println("✅ Проверка: в фильтре направления выбрано 'Архитектура'");
    }


    public void resetFilters() {
        Locator resetBtn = page.locator(resetButtonSelector);
        assertThat(resetBtn).isEnabled();
        resetBtn.click();
        page.waitForTimeout(1000);
        System.out.println("✅ Фильтры сброшены");
        waitForCoursesLoaded();
    }


    public void verifyFiltersReset() {
        verifyDirectionFilterDefault();
        verifyLevelFilterDefault();
        Locator durationDisplay = getDurationFilter().locator(durationDisplaySelector);
        assertThat(durationDisplay).hasText("От 0 до 15 месяцев");
        System.out.println("✅ Проверка: фильтры сброшены к значениям по умолчанию");
    }

    public int getCoursesCount() {
        return page.locator(courseCardSelector).count();
    }

    public Locator getCourseCards() {
        return page.locator(courseCardSelector);
    }

    public void verifyCoursesDisplayed() {
        Locator cards = page.locator(courseCardSelector);
        if (page.locator(emptyMessageSelector).isVisible()) {
            return;
        }
        page.waitForSelector(courseCardSelector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.ATTACHED));
        assertTrue(cards.count() > 0);
        System.out.println("✅ Курсы отображаются в каталоге");
    }

    public void verifyCoursesChanged(int previousCount) {
        int currentCount = getCoursesCount();
        assertTrue(currentCount != previousCount);
        System.out.println("✅ Количество курсов изменилось: было " + previousCount + ", стало " + currentCount);
    }

    private void waitForCoursesLoaded() {
        page.waitForFunction(
                "() => document.querySelectorAll('a.sc-zzdkm7-0').length > 0 || document.querySelector('text=Ничего не найдено') !== null",
                null
        );
    }

    public void verifyAllCoursesDurationInRange(int fromMin, int toMax) {
        Locator cards = getCourseCards();
        int count = cards.count();
        assertTrue(count > 0, "Нет карточек для проверки продолжительности");

        for (int i = 0; i < count; i++) {
            Locator card = cards.nth(i);
            // Ищем элемент с продолжительностью внутри карточки
            Locator durationElement = card.locator(".sc-157icee-1 .sc-hrqzy3-1, .sc-157icee-1 .jEGzDf");
            String durationText = durationElement.textContent().trim();
            int months = extractMonths(durationText);
            assertTrue(months >= fromMin && months <= toMax,
                    "Курс имеет продолжительность " + months + " мес., ожидается от " + fromMin + " до " + toMax +
                            ". Текст: " + durationText);
        }
        System.out.println("✅ Все " + count + " курсов имеют продолжительность от " + fromMin + " до " + toMax + " месяцев");
    }

    private int extractMonths(String durationText) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*месяц");
        java.util.regex.Matcher matcher = pattern.matcher(durationText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("Не удалось извлечь продолжительность из текста: " + durationText);
    }
}