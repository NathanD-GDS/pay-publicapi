package uk.gov.pay.api.model.directdebit.mandates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MandateStatus {

    @JsonProperty("status")
    private final String status;

    @JsonProperty("details")
    private final String details;

    private MandateStatus(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public static MandateStatus valueOf(MandateState state) {
        return new MandateStatus(state.getStatus(), state.getDetails());
    }
}
