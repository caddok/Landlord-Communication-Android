package source.kevtimov.landlordcommunicationapp.services.base;

import android.database.Cursor;


public interface TemplateMessageService {

    Cursor getAllTemplateMessages();

    void addTemplateMessage(String message);

    void closeCursorAndDatabase();
}
