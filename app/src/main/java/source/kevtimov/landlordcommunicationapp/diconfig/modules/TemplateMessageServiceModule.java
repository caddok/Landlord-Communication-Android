package source.kevtimov.landlordcommunicationapp.diconfig.modules;

import dagger.Module;
import dagger.Provides;
import source.kevtimov.landlordcommunicationapp.repositories.base.TemplateMessageRepository;
import source.kevtimov.landlordcommunicationapp.services.base.TemplateMessageService;
import source.kevtimov.landlordcommunicationapp.services.implementation.SQLiteTemplateMessageService;

@Module
public class TemplateMessageServiceModule {

    @Provides
    public TemplateMessageService getService(TemplateMessageRepository repo){
        return new SQLiteTemplateMessageService(repo);
    }
}
