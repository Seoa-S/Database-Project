package DB2024Team03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewController {
    public List<ReviewDTO> getMemberReviews(int memberId) {
        List<ReviewDTO> reviews = new ArrayList<>();
        String query = "SELECT R.review_id, R.content, R.date, M.name AS product_name " +
                " FROM Review R INNER JOIN Mealkit M ON R.mealkit_id = M.mealkit_id " +
                "WHERE R.member_id = ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new ReviewDTO(rs.getInt("review_id"), rs.getString("product_name"), rs.getString("content"), rs.getDate("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void displayMemberReviews(int memberId) {
        List<ReviewDTO> reviews = getMemberReviews(memberId);
        if (reviews.isEmpty()) {
            System.out.println("작성된 리뷰가 없습니다.");
        } else {
            System.out.print("=====================작성했던 리뷰 목록======================");
            for (ReviewDTO review : reviews) {
                System.out.println("\n상품이름:" + review.getProductName());
                System.out.println("리뷰내용:" + review.getContent());
                System.out.println("작성날짜:" + review.getDate().toString());
            }
            System.out.println("=====================");
        }
    }

    public void promptReturnToMyPage(Scanner sc) {
        System.out.print("마이페이지로 돌아가시겠습니까? (y) >> ");
        sc.nextLine(); // Clear the buffer after reading an integer
        String back = sc.nextLine();
        while (!back.equalsIgnoreCase("y")) {
            System.out.println("잘못된 값을 입력하셨습니다.");
            System.out.print("마이페이지로 돌아가시겠습니까? (y) >> ");
            back = sc.nextLine();
        }
    }

}
