package source.kevtimov.landlordcommunicationapp.chat.templatemessage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import source.kevtimov.landlordcommunicationapp.R;


public class MyCursorAdapter extends CursorAdapter {


    private List<String> messages = new ArrayList<String>();

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public String getItem(int position) {
        return messages.get(position);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        String message = cursor.getString(cursor.getColumnIndexOrThrow("template_message_text"));
        messages.add(message);
        TextView mTextViewMessage = view.findViewById(R.id.tv_templ_message);
        mTextViewMessage.setText(message);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.template_message_adapter_view, parent, false);
    }
}
