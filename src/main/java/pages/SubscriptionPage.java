package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionPage {

    private static final String URL = "https://otus.ru/subscription";

    private final Page page;

    private final String subscriptionCardSelector = "div.sc-1a5myy-0.wXPNv";
    private final String detailsButtonSelector = "button.sc-1qig7zt-0.gYFcfu.sc-1a5myy-3.cFWJVr";
    private final String buyButtonSelector = "button.sc-1qig7zt-0.czpnNJ.sc-56aorr-7.ebCtsq:has-text('Купить')";

    @Inject
    public SubscriptionPage(Page page) {
        this.page = page;
    }

    public SubscriptionPage open() {
        page.navigate(URL, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    public Locator getSubscriptionCards() {
        return page.locator(subscriptionCardSelector);
    }

    public void verifySubscriptionCardsDisplayed() {
        assertThat(getSubscriptionCards().first()).isVisible();
        assertTrue(getSubscriptionCards().count() > 0);
    }

    public Locator getDetailsButtonOnFirstCard() {
        return getSubscriptionCards().first().locator(detailsButtonSelector).nth(1);
    }

    public Locator getBuyButtonOnFirstCard() {
        return getSubscriptionCards().first().locator(buyButtonSelector);
    }

    public void clickDetailsButton() {
        getDetailsButtonOnFirstCard().click();
        page.waitForTimeout(300);
    }

    public void verifyDescriptionExpanded() {
        assertThat(getDetailsButtonOnFirstCard()).hasText("Свернуть");
    }

    public void verifyDescriptionCollapsed() {
        assertThat(getDetailsButtonOnFirstCard()).hasText("Подробнее");
    }

    public PaymentPage clickBuyButton() {
        Locator buyButton = getBuyButtonOnFirstCard();
        assertThat(buyButton).isEnabled();

        buyButton.click();
        page.waitForSelector("div.sc-azclpt-0.sc-1alnis6-0.fade-enter-done",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000));

        return new PaymentPage(page);
    }
}