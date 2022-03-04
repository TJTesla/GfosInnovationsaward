package gfos.beans;

public class Application {
    private int userId;
    private int offerId;
    private String text;
    private int status;
    private int resumeId;

    public Application(int userId, int offerId, String text, int status, int resumeId) {
        this.userId = userId;
        this.offerId = offerId;
        this.text = text;
        this.status = status;
        this.resumeId = resumeId;
    }

    public Application() {
        this.userId = 0;
        this.offerId = 0;
        this.text = "";
        this.status = 0;
        this.resumeId = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }
}
