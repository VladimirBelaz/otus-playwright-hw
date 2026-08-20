package di;

import com.google.inject.AbstractModule;
import pages.BusinessPage;
import pages.CatalogPage;
import pages.ClickHousePage;
import pages.PaymentPage;
import pages.SubscriptionPage;
import pages.components.TeacherPopup;
import pages.components.TeachersComponent;

public class PageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BusinessPage.class);
        bind(CatalogPage.class);
        bind(ClickHousePage.class);
        bind(PaymentPage.class);
        bind(SubscriptionPage.class);
        bind(TeacherPopup.class);
        bind(TeachersComponent.class);
    }
}