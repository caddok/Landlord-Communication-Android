package source.kevtimov.landlordcommunicationapp.repositories.base;

import android.database.Cursor;


public interface TemplateMessageRepository {

    Cursor getAllTemplateMessages();

    void addTemplateMessage(String message);

    void closeCursorAndDatabase();
}
