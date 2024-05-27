package DB2024Team03;

import java.util.Date;

public class ReviewDTO {
    private int reviewId;
    private String productName;
    private String content;
    private Date date;

    public ReviewDTO(int reviewId, String productName, String content, Date date) {
        this.reviewId = reviewId;
        this.productName = productName;
        this.content = content;
        this.date = date;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
