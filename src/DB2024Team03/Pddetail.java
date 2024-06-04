package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Pddetail {

    public static void displayProductdetail(int memberId) {
        Scanner sc = new Scanner(System.in);
        System.out.print("상품 id를 입력하세요: ");
        int PDid = sc.nextInt();

        // 상품상세 쿼리
        String query1 = "SELECT M.mealkit_id, M.name, M.price, M.category, M.info, M.stock, COUNT(B.mealkit_id) AS bookmarknum " +
                "FROM DB2024_Mealkit M LEFT JOIN DB2024_Bookmark B ON M.mealkit_id = B.mealkit_id " +
                "WHERE M.mealkit_id = ? GROUP BY M.mealkit_id";

        // 해당리뷰목록 쿼리
        /*String query2 = "SELECT R.member_id, R.content, R.date, M.name AS product_name " +
                "FROM DB2024_Review R INNER JOIN DB2024_Mealkit M ON R.mealkit_id = M.mealkit_id " +
                "WHERE R.mealkit_id = ?";*/

        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query1)) {

            pstmt.setInt(1, PDid); // 상품 id를 sql문에 넣어줌
            System.out.println("==================상품 상세==================");
            System.out.println("[상품번호]\t\t[상품이름]\t\t\t[가격]\t\t[카테고리]\t\t[밀키트 설명]\t\t\t\t\t\t\t\t\t[재고량]\t[북마크 수]");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("mealkit_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    String category = rs.getString("category");
                    String info = rs.getString("info");
                    int stock = rs.getInt("stock");
                    int bookmarknum = rs.getInt("bookmarknum");

                    System.out.printf("%d\t\t\t%s\t\t%d\t\t%s\t\t%s\t\t%d\t\t%d%n", id, name, price, category, info, stock, bookmarknum);
                }

                System.out.print("[1]장바구니 [2]북마크 [3]리뷰목록 [4]상품 목록  >> ");
                int mselect = sc.nextInt();
                // 장바구니에 추가
                if (mselect == 1) {
                    System.out.print("장바구니에 넣으시겠습니까? (y): ");
                    sc.nextLine(); // 버퍼 비우기
                    String confirm = sc.nextLine();
                    if ("y".equalsIgnoreCase(confirm)) {
                        addToBasket(PDid, memberId);
                    } else {
                        System.out.println("상품 목록으로 돌아갑니다.");
                        // 상품 목록으로 돌아가는 코드
                        Pdlistcontroller.displayProductList(memberId);
                    }
                } // 북마크에 추가
                else if (mselect == 2) {
                    System.out.print("북마크에 추가하시겠습니까? (y): ");
                    sc.nextLine(); // 버퍼 비우기
                    String confirm = sc.nextLine();
                    if ("y".equalsIgnoreCase(confirm)) {
                        addToBookmark(PDid, memberId);
                    } else {
                        System.out.println("마이페이지로 돌아갑니다.");

                    }
                } // 리뷰목록
                else if (mselect == 3) {
                    displayReviews(PDid);
                } // 상품목록으로 돌아가기
                else if (mselect == 4) {
                    Pdlistcontroller.displayProductList(memberId);
                } // 1~4를 입력하지 않았을 때 출력 후 메인페이지로 돌아감
                else {
                    System.out.println("올바르지 않은 입력입니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 해당 상품(PDid)에 대한 리뷰 출력하는 코드
    private static void displayReviews(int PDid) {
        String query2 = "SELECT R.member_id, R.content, R.date, M.name AS product_name " +
                "FROM DB2024_Review R INNER JOIN DB2024_Mealkit M ON R.mealkit_id = M.mealkit_id " +
                "WHERE R.mealkit_id = ?";

        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query2)) {

            pstmt.setInt(1, PDid); // 상품 id를 sql문에 넣어줌
            System.out.println("==================해당 상품 리뷰==================");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("product_name");
                    String content = rs.getString("content");
                    String date = rs.getString("date");
                    int member_id = rs.getInt("member_id");
                    System.out.println("[상품이름]" + productName);
                    System.out.println("[리뷰내용]" + content);
                    System.out.println("[작성날짜]" + date);
                    System.out.println("[작성자 id]" + member_id);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 장바구니에 상품 추가하는 메서드
    private static void addToBasket(int PDid, int memberId) {
        String query = "INSERT INTO DB2024_Basket (mealkit_id, member_id) VALUES (?, ?)";

        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, PDid); // 상품 id를 sql문에 넣어줌
            pstmt.setInt(2, memberId); // 로그인한 사용자 id를 sql문에 넣어줌

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("상품이 장바구니에 추가되었습니다.");
            } else {
                System.out.println("상품을 장바구니에 추가하지 못했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addToBookmark(int PDid, int memberId) {
        String query = "INSERT INTO DB2024_Bookmark (mealkit_id, member_id) VALUES (?, ?)";

        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, PDid); // 상품 id를 sql문에 넣어줌
            pstmt.setInt(2, memberId); // 로그인한 사용자 id를 sql문에 넣어줌

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("상품이 북마크에 추가되었습니다.");
            } else {
                System.out.println("상품을 북마크에 추가하지 못했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
