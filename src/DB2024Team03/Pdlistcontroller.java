package DB2024Team03; // 패키지 선언

import java.sql.*; // SQL 관련 클래스 import
import java.util.Scanner; // 사용자 입력을 받기 위한 Scanner 클래스 import

public class Pdlistcontroller { // 클래스 선언
    // 상품 목록을 보여주는 메서드
    public static void showProductList(int memberId) {
        // 상품 목록을 조회하는 쿼리
        String query = "SELECT * FROM DB2024_ProductWithBookmark_view";
        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement pstmt = conn.prepareStatement(query); //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성
             ResultSet rs = pstmt.executeQuery()) { // 쿼리 실행 및 결과 집합 받기

            // 상품 목록 출력 헤더
            System.out.println("==================상품 목록==================");
            System.out.println("[상품 ID]\t[상품이름]\t\t[가격]\t\t[북마크 수]");
            while (rs.next()) { // 결과 집합을 순회하며 각 상품 출력
                int id = rs.getInt("mealkit_id"); // 상품 ID 가져오기
                String name = rs.getString("name"); // 상품 이름 가져오기
                int price = rs.getInt("price"); // 상품 가격 가져오기
                int bookmark_count = rs.getInt("bookmark_count"); // 북마크 수 가져오기

                // 상품 정보 출력
                System.out.printf("%d\t\t\t%s\t\t%d\t\t%d%n", id, name, price, bookmark_count);
            }

            // 사용자에게 선택지 제공
            System.out.print("[1]상품상세 [2]메인페이지 >> ");
            Scanner sc = new Scanner(System.in); // 사용자 입력을 위한 Scanner 객체 생성
            int mselect = sc.nextInt(); // 사용자 선택을 입력받음

            // 선택에 따라 다른 동작 수행
            if (mselect == 1) { // 상품 상세로 이동
                PddetailController.showProductdetail(memberId); // 상품 상세 정보를 보여주는 메서드 호출
            } else if (mselect == 2) { // 메인 페이지로 이동
                return; // 메인 페이지로 돌아감
            } else { // 잘못된 입력
                System.out.println("올바르지 않은 입력입니다.");
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }
}
