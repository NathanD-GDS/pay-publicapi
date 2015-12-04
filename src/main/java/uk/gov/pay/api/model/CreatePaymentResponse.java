package uk.gov.pay.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.jackson.JsonSnakeCase;

@JsonSnakeCase
public class CreatePaymentResponse extends LinksResponse {
    private final String paymentId;
    private final long amount;
    private final String status;
    private final String returnUrl;
    private final String description;
    private final String reference;

    public static CreatePaymentResponse createPaymentResponse(JsonNode payload) {
        return new CreatePaymentResponse(
                payload.get("charge_id").asText(),
                payload.get("amount").asLong(),
                payload.get("status").asText(),
                payload.get("return_url").asText(),
                payload.get("description").asText(),
                payload.get("reference").asText()
        );
    }

    private CreatePaymentResponse(String chargeId, long amount, String status, String returnUrl, String description, String reference) {
        this.paymentId = chargeId;
        this.amount = amount;
        this.status = status;
        this.returnUrl = returnUrl;
        this.description = description;
        this.reference = reference;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public long getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "CreatePaymentResponse{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
