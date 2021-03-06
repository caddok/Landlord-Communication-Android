package source.kevtimov.landlordcommunicationapp.views.login.placemanagement;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import source.kevtimov.landlordcommunicationapp.models.Place;
import source.kevtimov.landlordcommunicationapp.models.Rent;
import source.kevtimov.landlordcommunicationapp.models.User;

public interface ContractsPlaceManagement {

    interface View {

        void setPresenter(Presenter presenter);

        void setNavigator(Navigator navigator);

        void showLoading();

        void hideLoading();

        void showError(Throwable error);

        void setUser(User user);

        void navigateUserToAddPlace();

        void navigateUserToSelectPlace();

        void addPlaceFail();

        void updateAddPlaces(Bundle places);

        void registerRent(int placeId);

        void updateSelectPlaces(ArrayList<Place> places);
    }

    interface Presenter {

        void subscribe(View view);

        void unsubscribe();

        void registerPlace(Place place);

        void registerRent(Rent rent);

        void allowNavigationToAddPlace();

        void allowNavigationToSelectPlace();
    }

    interface Navigator {

        void navigateToAddPlaceActivity();

        void navigateToSelectPlaceActivity();
    }
}
