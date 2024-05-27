package DB2024Team03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewController {
    public List<ReviewDTO> getMemberReviews(int memberId) {
        List<ReviewDTO> reviews = new ArrayList<>();

        String query = "SELECT R.review_id, R.content, R.date, M.name AS product_name " +
                " FROM DB2024_Review R INNER JOIN DB2024_Mealkit M ON R.mealkit_id = M.mealkit_id " +
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

    public void displayReview(int mealkitId) {
        String query = "SELECT name, price, stock FROM DB2024_Mealkit WHERE mealkit_id = ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, mealkitId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("상품 이름: " + rs.getString("name"));
                System.out.println("가격: " + rs.getInt("price"));
                System.out.println("재고: " + rs.getInt("stock"));
            } else {
                System.out.println("해당 상품이 존재하지 않습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createReview(int memberId, int mealkitId, String content) {
        String query = "INSERT INTO DB2024_Review (content, date, mealkit_id, member_id) VALUES (?, CURRENT_DATE(), ?, ?)";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, mealkitId);
            pstmt.setInt(3, memberId);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("리뷰가 성공적으로 등록되었습니다.");
            } else {
                System.out.println("리뷰 등록에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Prints the stack trace for debugging
        }
    }
}
