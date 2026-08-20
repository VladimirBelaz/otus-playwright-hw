package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.PaymentPage;
import pages.SubscriptionPage;

@ExtendWith(PlaywrightExtension.class)
public class Scenario4Test {

    @Inject
    private SubscriptionPage subscriptionPage;

    @Test
    void shouldVerifySubscriptionOptionsAndPayment() {
        subscriptionPage.open();

        subscriptionPage.verifySubscriptionCardsDisplayed();
        System.out.println("Варианты подписки отображаются");

        subscriptionPage.clickDetailsButton();
        subscriptionPage.verifyDescriptionExpanded();
        System.out.println("Описание развернуто");

        subscriptionPage.clickDetailsButton();
        subscriptionPage.verifyDescriptionCollapsed();
        System.out.println("Описание свернуто");

        PaymentPage paymentPage = subscriptionPage.clickBuyButton();
        System.out.println("Переход на страницу оплаты (модальное окно)");

        paymentPage.verifyPaymentPageOpened();
        System.out.println("Модальное окно открыто");

        paymentPage.closeAuthModal();
        System.out.println("Модальное окно закрыто");
    }
}