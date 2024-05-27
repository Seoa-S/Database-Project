package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Pddetail {
    public static void displayProductdetail() {

        Scanner sc = new Scanner(System.in);
        System.out.print("상품 id를 입력하세요: ");
        int PDid = sc.nextInt();
        //상품상세
        String query = "SELECT M.mealkit_id, M.name, M.price, M.category, M.info, M.stock, COUNT(B.mealkit_id) AS bookmarknum " +
                "FROM DB2024_Mealkit M LEFT JOIN DB2024_Bookmark B ON M.mealkit_id = B.mealkit_id " +
                "WHERE M.mealkit_id = ? GROUP BY M.mealkit_id";
        //해당리뷰목록
        //String query2="SELE

        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, PDid); // 상품 id를 sql문에 넣어줌
            System.out.println("==================상품 상세==================");
            System.out.println("상품번호:\t\t상품이름:\t\t\t가격:\t\t카테고리:\t\t밀키트 설명:\t\t\t\t\t\t\t\t\t 재고량:\t북마크 수:");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
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
                int mselect =sc.nextInt();
                //장바구니
                if (mselect==1){
                    Pddetail.displayProductdetail();
                }
                //북마크
                else if (mselect==2) {
                    return;

                }//리뷰목록
                else if (mselect==3) {

                }//상품목록
                else if (mselect==4) {
                    Pdlistcontroller.displayProductList();

                } else System.out.println("올바르지 않은 입력입니다.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

