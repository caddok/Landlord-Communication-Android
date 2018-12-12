package source.kevtimov.landlordcommunicationapp.chat.templatemessage;


import android.database.Cursor;

public interface ContractsTemplateMessage {

    interface View {

        void setPresenter(ContractsTemplateMessage.Presenter presenter);

        void navigateToChat(String message);

        void setNavigator(ContractsTemplateMessage.Navigator navigator);

    }

    interface Presenter {

        void subscribe(ContractsTemplateMessage.View view);

        void unsubscribe();

        void allowNavigationToChat(String message);

        Cursor loadTemplateMessages();

        void addTemplateMessage(String message);

        void closeCursorAndDatabase();
    }

    interface Navigator{
        void navigateBackToChat(String templateMessage);
    }
}
