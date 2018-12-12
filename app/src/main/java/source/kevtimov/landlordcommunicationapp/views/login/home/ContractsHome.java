package source.kevtimov.landlordcommunicationapp.views.login.home;

import java.util.Calendar;

import source.kevtimov.landlordcommunicationapp.models.Rent;
import source.kevtimov.landlordcommunicationapp.models.User;

public interface ContractsHome {

    interface View {

        void setPresenter(Presenter presenter);

        void showLoading();

        void hideLoading();

        void setNavigator(Navigator navigator);

        void showError(Throwable error);

        void sendNotification(int year, int month, int day, Rent rent);

        void navigateToChats();

        void navigateToPayments();

        void navigateToUsers();

        void navigateToPlaces();
    }

    interface Presenter {

        void subscribe(View view);

        void unsubscribe();

        void manageNotifications();

        void allowNavigationToChats();

        void allowNavigationToPayments();

        void allowNavigationToUsers();

        void allowNavigationToPlaces();

        void setUser(User user);
    }

    interface Navigator {

        void navigateToChats();

        void navigateToPayments();

        void navigateToUsers();

        void navigateToPlaces();

        void sendNotification(Rent rent, Calendar mCalendar);

    }

}
