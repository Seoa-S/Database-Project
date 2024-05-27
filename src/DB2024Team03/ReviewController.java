package DB2024Team03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewController {
    public List<ReviewDTO> getMemberReviews(int memberId) {
        List<ReviewDTO> reviews = new ArrayList<>();
        String query = "SELECT * FROM DB2024_Review WHERE member_id = ?";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new ReviewDTO(rs.getInt("review_id"), rs.getString("content"), rs.getDate("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
