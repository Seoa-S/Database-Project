package DB2024Team03;

import java.sql.*;
import java.util.Scanner;

public class OrdersController {
    public static void showOrdersList(int member_id, Scanner sc) {
        String ordersquery = "SELECT M.name, O.orderdate, M.price, O.mealkit_id " +
                "FROM DB2024_Orders O INNER JOIN DB2024_Mealkit M ON O.mealkit_id = M.mealkit_id " +
                "WHERE O.member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(ordersquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[주문내역]");
            System.out.println("[상품 ID]\t[상품 이름]\t\t[가격]\t[주문 날짜]");
            while (rs.next()) {
                System.out.printf("%d\t\t\t%s\t\t%d\t%s%n",
                        rs.getInt("mealkit_id"),
                        rs.getString("name"),
                        rs.getInt("price"),  // Assuming you want to display the price
                        rs.getDate("orderdate"));
            }

            System.out.print("[1]리뷰하기 [2]마이페이지로 돌아가기 >> ");
            int action = sc.nextInt(); // 사용자 선택 입력 받기
            if (action == 1) { // 리뷰 작성 선택 시
                System.out.print("리뷰할 상품 ID 입력 >> ");
                int productId = sc.nextInt(); // 리뷰할 상품 ID 입력 받기
                sc.nextLine(); // 입력 버퍼 클리어
                ReviewController reviewController = new ReviewController(); // 리뷰 컨트롤러 객체 생성
                reviewController.showReview(productId); // 선택한 상품의 상세 정보 보여주기
                System.out.println("리뷰를 작성하세요 (엔터를 누르면 작성 완료): ");
                String reviewContent = sc.nextLine(); // 리뷰 내용 입력 받기
                reviewController.createReview(member_id, productId, reviewContent, sc); // 리뷰 생성
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외로 전환
        }
    }
}
