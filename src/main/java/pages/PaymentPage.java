package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PaymentPage {

    private final Page page;

    private final String authModalSelector = "div.sc-azclpt-0.sc-1alnis6-0.fade-enter-done";
    private final String closeButtonSelector = "div.sc-1alnis6-2.daSFPw";

    @Inject
    public PaymentPage(Page page) {
        this.page = page;
    }

    public void verifyPaymentPageOpened() {
        Locator modal = page.locator(authModalSelector);
        assertThat(modal).isVisible();
        System.out.println("✅ Модальное окно авторизации открыто");
    }

    public void closeAuthModal() {
        page.locator(closeButtonSelector).click();
        page.waitForSelector(authModalSelector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }
}