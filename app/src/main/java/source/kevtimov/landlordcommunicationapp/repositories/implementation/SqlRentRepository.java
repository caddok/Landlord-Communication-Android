package source.kevtimov.landlordcommunicationapp.repositories.implementation;

import java.io.IOException;

import source.kevtimov.landlordcommunicationapp.http.HttpRequester;
import source.kevtimov.landlordcommunicationapp.models.Rent;
import source.kevtimov.landlordcommunicationapp.parsers.base.JsonParser;
import source.kevtimov.landlordcommunicationapp.repositories.base.RentRepository;

public class SqlRentRepository implements RentRepository {

    private HttpRequester mHttpRequester;
    private JsonParser<Rent> mJsonParser;
    private String mServerUrl;

    public SqlRentRepository(String url, HttpRequester requester, JsonParser<Rent> jsonParser) {
        this.mServerUrl = url;
        this.mHttpRequester = requester;
        this.mJsonParser = jsonParser;
    }

    @Override
    public Rent registerNextRent(Rent rent) throws IOException {
        String requestBody = mJsonParser.toJson(rent);
        String currentUrl = mServerUrl + "/next";
        String responseBody = mHttpRequester.post(currentUrl, requestBody);
        return mJsonParser.fromJson(responseBody);
    }

    @Override
    public Rent registerFirstRent(Rent rent) throws IOException {
        String requestBody = mJsonParser.toJson(rent);
        String currentUrl = mServerUrl + "/first";
        String responseBody = mHttpRequester.post(currentUrl, requestBody);
        return mJsonParser.fromJson(responseBody);
    }

    @Override
    public Rent getRentByPlaceId(int placeId) throws IOException {
        String currentUrl = mServerUrl + "/" + placeId;
        String json = mHttpRequester.get(currentUrl);
        return mJsonParser.fromJson(json);
    }

    @Override
    public Rent updatePaidStatus(int rentId) throws IOException {
        String mServerUrlPut = mServerUrl + "/updatestatus/" + rentId;
        String responseBody = mHttpRequester.update(mServerUrlPut, "");
        return mJsonParser.fromJson(responseBody);
    }

    @Override
    public Rent updateRentRemainingAmount(int rentId, Rent rent) throws IOException {
        String requestBody = mJsonParser.toJson(rent);
        String mServerUrlPut = mServerUrl + "/updateremaining/" + rentId;
        String responseBody = mHttpRequester.update(mServerUrlPut, requestBody);
        return mJsonParser.fromJson(responseBody);
    }

    @Override
    public Rent editRent(Rent rent, int rentId) throws IOException {
        String requestBody = mJsonParser.toJson(rent);
        String mServerUrlPut = mServerUrl + "/edit/" + rentId;
        String responseBody = mHttpRequester.update(mServerUrlPut, requestBody);
        return mJsonParser.fromJson(responseBody);
    }
}
