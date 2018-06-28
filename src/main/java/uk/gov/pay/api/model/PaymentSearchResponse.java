package uk.gov.pay.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.pay.api.model.links.PaymentSearchNavigationLinks;

import java.util.List;

public class PaymentSearchResponse implements IPaymentSearchPagination {

    @JsonProperty("total")
    private int total;

    @JsonProperty("count")
    private int count;

    @JsonProperty("page")
    private int page;

    @JsonProperty("results")
    private List<ChargeFromResponse> payments;

    @JsonProperty("_links")
    private PaymentSearchNavigationLinks links = new PaymentSearchNavigationLinks();

    public List<ChargeFromResponse> getPayments() {
        return payments;
    }
    @Override
    public int getTotal() {
        return total;
    }
    @Override
    public int getCount() {
        return count;
    }
    @Override
    public int getPage() {
        return page;
    }
    @Override
    public PaymentSearchNavigationLinks getLinks() {
        return links;
    }
}
