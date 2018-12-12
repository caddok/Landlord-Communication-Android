package source.kevtimov.landlordcommunicationapp.diconfig.modules;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import source.kevtimov.landlordcommunicationapp.sqlite.TemplateMessageDatabaseHelper;
import source.kevtimov.landlordcommunicationapp.sqlite.TemplateMessageSQLite;

@Module
public class TemplateMessageSQLiteModule {
    @Provides
    public TemplateMessageSQLite getDatabaseHelper(Context context) {
        return new TemplateMessageDatabaseHelper(context);
    }
}
