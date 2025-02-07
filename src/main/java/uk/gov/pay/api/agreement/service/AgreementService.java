package uk.gov.pay.api.agreement.service;

import org.apache.http.HttpStatus;
import uk.gov.pay.api.agreement.model.AgreementCreatedResponse;
import uk.gov.pay.api.agreement.model.CreateAgreementRequest;
import uk.gov.pay.api.auth.Account;
import uk.gov.pay.api.exception.CancelAgreementException;
import uk.gov.pay.api.exception.CreateAgreementException;
import uk.gov.pay.api.service.ConnectorUriGenerator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.json;

public class AgreementService {

    private final Client client;
    private final ConnectorUriGenerator connectorUriGenerator;

    @Inject
    public AgreementService(Client client, ConnectorUriGenerator connectorUriGenerator) {
        this.client = client;
        this.connectorUriGenerator = connectorUriGenerator;
    }

    public AgreementCreatedResponse create(Account account, CreateAgreementRequest createAgreementRequest) {
        var response = createAgreement(account, createAgreementRequest);

        if (response.getStatus() != HttpStatus.SC_CREATED) {
            throw new CreateAgreementException(response);
        }
        return response.readEntity(AgreementCreatedResponse.class);
    }

    public Response cancel(Account account, String agreementId) {
        Response connectorResponse = client.target(connectorUriGenerator.cancelAgreementURI(account, agreementId)).request().post(null);
        if (connectorResponse.getStatus() != HttpStatus.SC_NO_CONTENT) {
            throw new CancelAgreementException(connectorResponse);
        }

        connectorResponse.close();
        return Response.noContent().build();
    }

    private Response createAgreement(Account account, CreateAgreementRequest agreementCreateRequest) {
        return client
                .target(connectorUriGenerator.getAgreementURI(account))
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(buildCreateAgreementRequestPayload(agreementCreateRequest));
    }

    private Entity buildCreateAgreementRequestPayload(CreateAgreementRequest requestPayload) {
        return json(requestPayload.toConnectorPayload());
    }
}
