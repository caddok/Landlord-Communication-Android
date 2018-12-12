package source.kevtimov.landlordcommunicationapp.chat.templatemessage;

import android.database.Cursor;

import javax.inject.Inject;

import source.kevtimov.landlordcommunicationapp.services.base.TemplateMessageService;

public class TemplateMessagePresenter implements ContractsTemplateMessage.Presenter {

    private ContractsTemplateMessage.View mView;
    private TemplateMessageService mTemplateMessageService;

    @Inject
    public TemplateMessagePresenter(TemplateMessageService service){
        this.mTemplateMessageService = service;
    }

    @Override
    public void subscribe(ContractsTemplateMessage.View view) {
        this.mView = view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void allowNavigationToChat(String message) {
        mView.navigateToChat(message);
    }

    @Override
    public Cursor loadTemplateMessages() {
        return mTemplateMessageService.getAllTemplateMessages();
    }

    @Override
    public void addTemplateMessage(String message) {
        mTemplateMessageService.addTemplateMessage(message);
    }

    @Override
    public void closeCursorAndDatabase() {
        mTemplateMessageService.closeCursorAndDatabase();
    }
}
