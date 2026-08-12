package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PaymentPage extends BasePage {

    private static final String AUTH_MODAL_SELECTOR =
            "div.sc-azclpt-0.sc-1alnis6-0.fade-enter-done";

    private static final String CLOSE_BUTTON_SELECTOR =
            "div.sc-1alnis6-2.daSFPw";

    @Inject
    public PaymentPage(Page page) {
        super(page);
    }

    public void verifyPaymentPageOpened() {
        Locator modal = page.locator(AUTH_MODAL_SELECTOR);

        assertThat(modal).isVisible();
    }

    public void closeAuthModal() {
        Locator modal = page.locator(AUTH_MODAL_SELECTOR);

        page.locator(CLOSE_BUTTON_SELECTOR).click();

        modal.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }
}