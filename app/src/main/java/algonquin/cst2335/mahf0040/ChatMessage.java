package algonquin.cst2335.mahf0040;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

//    String message;
//    String timeSent;
//    boolean isSentButton;


    @ColumnInfo(name="message")
    protected String message;

    @ColumnInfo(name="timeSent")
    protected String timeSent;

    @ColumnInfo(name="isSentButton")
    protected boolean isSentButton;

//    public ChatMessage(){}
    public ChatMessage(String message, String timeSent, boolean isSentButton) {

        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    @PrimaryKey
    public int id;

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
