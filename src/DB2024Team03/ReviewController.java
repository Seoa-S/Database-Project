package DB2024Team03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}
