package source.kevtimov.landlordcommunicationapp.sqlite;

import android.database.Cursor;


public interface TemplateMessageSQLite {

    Cursor getAllTemplateMessages();

    boolean addTemplateMessage(String message);

    void closeCursorAndDatabase();
}
