package by.touchsoft.vasilyevanatali.Model;



import java.time.LocalDateTime;
import java.util.Objects;

public class ChatMessage {
    private String senderName;
    private LocalDateTime time;
    private String text;

    public ChatMessage() {

    }

    public ChatMessage(String senderName, LocalDateTime time, String text) {
        this.senderName = senderName;
        this.time = time;
        this.text = text;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(senderName, that.senderName) &&
                Objects.equals(time, that.time) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderName, time, text);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senderName='" + senderName + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                '}';
    }

}
