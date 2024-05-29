package DB2024Team03;

import java.sql.*;
import java.util.Scanner;

public class Pdlistcontroller{
    public static void displayProductList(int memberId) {
        String query = "SELECT * FROM ProductWithBookmark_view";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("==================상품 목록==================");
            System.out.println("상품번호:\t\t상품이름:\t\t\t가격:\t\t북마크 수:");
            while (rs.next()) {
                int id = rs.getInt("mealkit_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int bookmark_count = rs.getInt("bookmark_count");

                System.out.printf("%d\t\t\t%s\t\t%d\t\t%d%n", id, name, price, bookmark_count);


            }
            System.out.print("[1]상품상세 [2]메인페이지 >> ");
            Scanner sc = new Scanner(System.in);
            int mselect =sc.nextInt();
            //상품 상세로 이동
            if (mselect==1){
                Pddetail.displayProductdetail(memberId);
            }
            //메인페이지로 이동
            else if (mselect==2) {
                return;
            }else System.out.println("올바르지 않은 입력입니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
