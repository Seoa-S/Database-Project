package DB2024Team03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewController {
    public List<ReviewDTO> getMemberReviews(int memberId) {
        List<ReviewDTO> reviews = new ArrayList<>(); // ReviewDTO 객체를 저장할 리스트 생성

        String query = "SELECT R.review_id, R.content, R.date, R.mealkit_id, M.name AS product_name " +
                "FROM DB2024_Review R INNER JOIN DB2024_Mealkit M ON R.mealkit_id = M.mealkit_id " +
                "WHERE R.member_id = ?"; // 리뷰 조회 SQL 쿼리

        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId); // 매개변수로 받은 memberId 설정
            ResultSet rs = pstmt.executeQuery(); // 쿼리 실행
            while (rs.next()) {
                reviews.add(new ReviewDTO(rs.getInt("review_id"), rs.getInt("mealkit_id"),
                        rs.getString("product_name"), rs.getString("content"), rs.getDate("date"))); // ReviewDTO 객체 생성 및 추가
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리
        }
        return reviews; // 결과 리스트 반환
    }

    public void showMemberReviews(int memberId) {
        List<ReviewDTO> reviews = getMemberReviews(memberId); // 회원의 리뷰 리스트 가져오기
        if (reviews.isEmpty()) { // 리뷰가 없는 경우
            System.out.println();
            System.out.println("작성된 리뷰가 없습니다.");
        } else { // 리뷰가 존재하는 경우 각 리뷰 출력
            for (ReviewDTO review : reviews) {
                System.out.println("\n[상품 id] " + review.getProductId());
                System.out.println("[상품이름]" + review.getProductName());
                System.out.println("[리뷰내용]" + review.getContent());
                System.out.println("[작성날짜]" + review.getDate().toString());
            }
            System.out.println("=====================");
        }
    }

    public void showReview(int mealkitId) {
        String query = "SELECT name, price, stock FROM DB2024_Mealkit WHERE mealkit_id = ?"; // 상품 정보 조회 쿼리
        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, mealkitId); // 매개변수로 받은 mealkit 설정
            ResultSet rs = pstmt.executeQuery(); // 쿼리 실행
            if (rs.next()) { // 결과가 존재하는 경우
                System.out.println("[상품 이름] " + rs.getString("name"));
                System.out.println("[가격] " + rs.getInt("price"));
                System.out.println("[재고] " + rs.getInt("stock"));
            } else {
                System.out.println("해당 상품이 존재하지 않습니다."); // 결과가 존재하지 않는 경우
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    public void createReview(int memberId, int mealkitId, String content, Scanner sc) {
        String checkQuery = "SELECT COUNT(*) FROM DB2024_Review WHERE member_id = ? AND mealkit_id = ?"; // 기존 리뷰 존재 확인 쿼리
        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement checkPstmt = conn.prepareStatement(checkQuery)) {
            conn.setAutoCommit(false); // 자동 커밋 비활성화
            checkPstmt.setInt(1, memberId); // 매개변수로 받은 memberId 설정
            checkPstmt.setInt(2, mealkitId); // 매개변수로 받은 mealkitId 설정
            ResultSet rs = checkPstmt.executeQuery(); // 쿼리 실행
            if (rs.next() && rs.getInt(1) > 0) { // 이미 리뷰가 존재하는 경우
                // 리뷰 존재하면 삭제
                String deleteQuery = "DELETE FROM DB2024_Review WHERE member_id = ? AND mealkit_id = ?";
                try (PreparedStatement deletePstmt = conn.prepareStatement(deleteQuery)) {
                    deletePstmt.setInt(1, memberId); // memberId 설정
                    deletePstmt.setInt(2, mealkitId); // mealkitId 설정
                    deletePstmt.executeUpdate();
                }
                System.out.println("기존 리뷰가 삭제되었습니다."); // 삭제 완료 메시지 출력
            }

            // 새로운 리뷰 삽입
            String insertQuery = "INSERT INTO DB2024_Review (content, date, mealkit_id, member_id) VALUES (?, CURRENT_DATE(), ?, ?)";
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertQuery)) {
                insertPstmt.setString(1, content); // 리뷰 내용 설정
                insertPstmt.setInt(2, mealkitId); // mealkitId 설정
                insertPstmt.setInt(3, memberId); // memberId 설정
                int result = insertPstmt.executeUpdate(); // 쿼리 실행
                if (result > 0) {
                    conn.commit(); // 트랜잭션 커밋
                    System.out.println("새 리뷰가 성공적으로 등록되었습니다."); // 성공 메시지 출력
                } else {
                    conn.rollback(); // 실패한 경우 롤백
                    System.out.println("리뷰 등록에 실패했습니다."); // 실패 메시지 출력
                }
            }
            conn.setAutoCommit(true); // 자동 커밋 활성화
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    //사용자의 리뷰 목록에서 원하는 상품 ID에 해당하는 리뷰를 삭제하는 함수
    public void deleteReview(int id, int mealkitId) {
        String deleteReview = "DELETE FROM DB2024_Review WHERE member_id=? AND mealkit_id=?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = DBconnect.getConnection(); //데이터베이스 연결
            conn.setAutoCommit(false); // 트랜잭션 시작

            statement = conn.prepareStatement(deleteReview);  //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성
            statement.setInt(1, id); //피라미터로 받은 id를 sql문에 넣기
            statement.setInt(2, mealkitId); //피라미터로 받은 mealkitId를 sql문에 넣기

            try {
                //DB2024_Review 데이터에 입력한 상품 ID가 존재하는 경우
                if (UtilController.checkIdExist(mealkitId, id, "DB2024_Review", conn)) {
                    statement.executeUpdate(); //SQL 쿼리문 실행 (리뷰 목록에서 삭제)
                    System.out.print("리뷰가 제거되었습니다.\n"); //안내문 출력
                    conn.commit(); // 변경사항 커밋
                } else {  //DB2024_Review 데이터에 입력한 상품 ID가 존재하지 않는 경우
                    System.out.print("해당 상품ID의 리뷰가 존재하지 않습니다.\n"); //안내문 출력
                    conn.commit(); // 트랜잭션 완료 (실제로 삭제할 항목이 없는 경우에도)
                }
            } catch (SQLException e) {
                conn.rollback(); // 예외 발생 시 롤백
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // 자동 커밋 모드 재설정
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                    try {
                        conn.close(); // 연결 닫기
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close(); // PreparedStatement 닫기
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) { //예외처리
            throw new RuntimeException(e);
        }
    }
}
