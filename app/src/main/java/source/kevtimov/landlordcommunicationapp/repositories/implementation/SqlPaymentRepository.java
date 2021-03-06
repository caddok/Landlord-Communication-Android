package source.kevtimov.landlordcommunicationapp.repositories.implementation;

import java.io.IOException;
import java.util.List;

import source.kevtimov.landlordcommunicationapp.http.HttpRequester;
import source.kevtimov.landlordcommunicationapp.models.Payment;
import source.kevtimov.landlordcommunicationapp.parsers.base.JsonParser;
import source.kevtimov.landlordcommunicationapp.repositories.base.PaymentRepository;

public class SqlPaymentRepository implements PaymentRepository {

    private HttpRequester mHttpRequester;
    private JsonParser<Payment> mJsonParser;
    private String mServerUrl;

    public SqlPaymentRepository(String url, HttpRequester requester, JsonParser<Payment> jsonParser) {
        this.mServerUrl = url;
        this.mHttpRequester = requester;
        this.mJsonParser = jsonParser;
    }

    @Override
    public Payment createPayment(Payment payment) throws IOException {
        String requestBody = mJsonParser.toJson(payment);
        String responseBody = mHttpRequester.post(mServerUrl, requestBody);
        return mJsonParser.fromJson(responseBody);
    }

    @Override
    public List<Payment> getAllPaymentsByLandlordId(int landlordId) throws IOException {
        String mServerUrlGet = mServerUrl + "/landlord/" + landlordId ;
        String paymentJson = mHttpRequester.get(mServerUrlGet);
        return mJsonParser.fromJsonArray(paymentJson);
    }

    @Override
    public List<Payment> getAllPaymentsByTenantId(int tenantId) throws IOException {
        String mServerUrlGet = mServerUrl + "/tenant/" + tenantId;
        String paymentJson = mHttpRequester.get(mServerUrlGet);
        return mJsonParser.fromJsonArray(paymentJson);
    }
}
