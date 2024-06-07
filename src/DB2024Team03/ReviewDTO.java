package DB2024Team03;

import java.util.Date;

public class ReviewDTO {
    private int reviewId; // 리뷰 ID
    private int productId; // 밀키트 ID
    private String productName; // 밀키트 이름
    private String content; // 리뷰 내용
    private Date date; // 작성 날짜

    // ReviewDTO 객체를 생성할 때 필요한 정보를 설정하는 생성자
    public ReviewDTO(int reviewId, int productId, String productName, String content, Date date) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
        this.content = content;
        this.date = date;
    }

    // 리뷰 ID를 반환하는 getter 메서드
    public int getReviewId() {
        return reviewId;
    }

    // 리뷰 ID를 설정하는 setter 메서드
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    // 밀키트 ID를 반환하는 getter 메서드
    public int getProductId() {  // Getter for the product ID
        return productId;
    }

    // 밀키트 ID를 설정하는 setter 메서드
    public void setProductId(int productId) {
        this.productId = productId;
    }

    // 밀키트 이름을 반환하는 getter 메서드
    public String getProductName() {
        return productName;
    }
    
    // 밀키트 이름을 설정하는 setter 메서드
    public void setProductName(String productName) {
        this.productName = productName;
    }

    // 리뷰 내용을 반환하는 getter 메서드
    public String getContent() {
        return content;
    }

    // 리뷰 내용을 설정하는 setter 메서드
    public void setContent(String content) {
        this.content = content;
    }

    // 작성 날짜를 반환하는 getter 메서드
    public Date getDate() {
        return date;
    }

    // 작성 날짜를 설정하는 setter 메서드
    public void setDate(Date date) {
        this.date = date;
    }
}
