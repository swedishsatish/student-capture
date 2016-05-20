package studentcapture.usersettings;

/**
 * Created by c13elt on 2016-05-16.
 */

public class Settings {
    private String language;
    private String email;
    private boolean mailUpdate;
    private int textSize;

    public boolean getMailUpdate() {
        return mailUpdate;
    }

    public void setMailUpdate(boolean mailUpdate) {
        this.mailUpdate = mailUpdate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "language='" + language + '\'' +
                ", email='" + email + '\'' +
                ", mailUpdate=" + mailUpdate +
                ", textSize=" + textSize +
                '}';
    }
}