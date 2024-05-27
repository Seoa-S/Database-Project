package DB2024Team03;

import java.util.Date;

public class ReviewDTO {
    private int reviewId;
    private String content;
    private Date date;

    public ReviewDTO(int reviewId, String content, Date date) {
        this.reviewId = reviewId;
        this.content = content;
        this.date = date;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
