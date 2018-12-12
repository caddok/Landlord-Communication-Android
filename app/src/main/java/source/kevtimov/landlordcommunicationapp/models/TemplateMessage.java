package source.kevtimov.landlordcommunicationapp.models;

import java.io.Serializable;

public class TemplateMessage implements Serializable {

    private int templateMessageId;
    private String messageText;

    public TemplateMessage(){
        //default
    }

    public TemplateMessage(String messageText){
        setTemplateMessageId(templateMessageId);
        setMessageText(messageText);
    }

    public int getTemplateMessageId() {
        return templateMessageId;
    }

    public void setTemplateMessageId(int templateMessageId) {
        this.templateMessageId = templateMessageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
