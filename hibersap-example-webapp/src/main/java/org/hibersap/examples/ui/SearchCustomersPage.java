package org.hibersap.examples.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.examples.dao.CustomerDao;
import org.hibersap.examples.model.Customer;
import org.hibersap.examples.model.CustomerInfo;
import org.hibersap.examples.model.CustomerSearchFields;

import java.util.List;

/**
 * The Wicket page to search and display flight customers.
 */
public class SearchCustomersPage extends WebPage
{
    private static final long serialVersionUID = 1L;

    private final CustomerSearchFields customerSearchFields = new CustomerSearchFields("*", 50);
    private final CustomerListModel customerListModel;

    public SearchCustomersPage()
    {
        add(new StyleSheetReference("stylesheet", SearchCustomersPage.class, "styles.css"));

        final IModel<CustomerSearchFields> model = new CompoundPropertyModel<CustomerSearchFields>(customerSearchFields);
        add(new Form<CustomerSearchFields>("form", model)
        {
            {
                add(new TextField<String>("customerNamePattern"));
                add(new TextField<Integer>("maxRows", Integer.class));
                add(new Button("searchButton"));
            }

            @Override
            protected void onSubmit()
            {
                customerListModel.setSearchFields(customerSearchFields);
            }
        });

        add(new FeedbackPanel("feedback"));

        customerListModel = new CustomerListModel();
        add((ListView<Customer>) new ListView<Customer>("customerList", customerListModel)
        {
            public void populateItem(final ListItem item)
            {
                final Customer customerData = (Customer) item.getModelObject();
                item.add(new Label("number", customerData.getNumber()));
                item.add(new Label("formOfAddress", customerData.getFormOfAddress()));
                item.add(new Label("name", customerData.getName()));
                item.add(new Label("street", customerData.getStreet()));
                item.add(new Label("country", customerData.getCountry()));
                item.add(new Label("postalCode", customerData.getPostalCode()));
                item.add(new Label("city", customerData.getCity()));
                item.add(new Label("telephoneNumber", customerData.getTelephoneNumber()));
                item.add(new Label("email", customerData.getEmail()));
            }
        });
    }

    private class CustomerListModel extends LoadableDetachableModel<List<Customer>>
    {
        private CustomerSearchFields searchFields = null;

        private void setSearchFields(CustomerSearchFields searchFields)
        {
            this.searchFields = searchFields;
        }

        @Override
        protected List<Customer> load()
        {
            return searchFields == null ? null : loadCustomers();
        }

        private List<Customer> loadCustomers()
        {
            final CustomerDao dao = WicketApplication.get().getCustomerDao();
            final CustomerInfo customerInfo = dao.findCustomerInfo(searchFields);
            info(customerInfo.getCustomers().size() + " customers found.");
            showSapMessages(customerInfo.getReturnMessages());
            return customerInfo.getCustomers();
        }

        private void showSapMessages(List<BapiRet2> returnMessages)
        {
            for (BapiRet2 returnMessage : returnMessages)
            {
                info(returnMessage.getMessage());
            }
        }
    }
}
