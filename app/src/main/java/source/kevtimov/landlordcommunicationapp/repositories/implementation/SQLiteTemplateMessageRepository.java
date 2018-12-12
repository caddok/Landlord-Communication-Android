package source.kevtimov.landlordcommunicationapp.repositories.implementation;

import android.database.Cursor;

import java.util.List;

import source.kevtimov.landlordcommunicationapp.repositories.base.TemplateMessageRepository;
import source.kevtimov.landlordcommunicationapp.sqlite.TemplateMessageSQLite;

public class SQLiteTemplateMessageRepository implements TemplateMessageRepository {

    private TemplateMessageSQLite mTemplateMessageSQLite;

    public SQLiteTemplateMessageRepository(TemplateMessageSQLite sqliteRepository){
        this.mTemplateMessageSQLite = sqliteRepository;
    }

    @Override
    public Cursor getAllTemplateMessages() {
        return mTemplateMessageSQLite.getAllTemplateMessages();
    }

    @Override
    public void addTemplateMessage(String message) {
        mTemplateMessageSQLite.addTemplateMessage(message);
    }

    @Override
    public void closeCursorAndDatabase() {
        mTemplateMessageSQLite.closeCursorAndDatabase();
    }
}
