package by.touchsoft.vasilyevanatali.Model;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Natali
 * Class contains information about sender message, time and context of message
 */

public class ChatMessage {

    /**
     * Sender name
     */
    private String senderName;

    /**
     * Time of message
     */
    private LocalDateTime time;

    /**
     * Message's text
     */
    private String text;

    /**
     * Constructor
     */
    public ChatMessage() {

    }

    /**
     * Constractor with parameters
     * @param senderName - who send the message
     * @param time -  time of message
     * @param text - context
     */
    public ChatMessage(String senderName, LocalDateTime time, String text) {
        this.senderName = senderName;
        this.time = time;
        this.text = text;
    }


    /**
     * Get sender name
     * @return sender name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     *
     * @param senderName - set senders name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * get time of message
     * @return localDateTime
     */
    public LocalDateTime getTime() {
        return time;
    }


    /**
     * set time of message
     * @param time - time of message
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Get text of message
     * @return context of message
     */

    public String getText() {
        return text;
    }


    /**
     * Set text of message
     * @param text - set context
     */
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
