package source.kevtimov.landlordcommunicationapp.diconfig.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import source.kevtimov.landlordcommunicationapp.repositories.base.TemplateMessageRepository;
import source.kevtimov.landlordcommunicationapp.repositories.implementation.SQLiteTemplateMessageRepository;
import source.kevtimov.landlordcommunicationapp.sqlite.TemplateMessageSQLite;

@Module
public class TemplateMessageRepositoryModule {
    @Provides
    @Singleton
    public TemplateMessageRepository templateMessageRepository(TemplateMessageSQLite templateMessageSQLite) {
        return new SQLiteTemplateMessageRepository(templateMessageSQLite);
    }
}
