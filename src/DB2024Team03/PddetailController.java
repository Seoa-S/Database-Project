package DB2024Team03; // 패키지 선언

import java.sql.Connection; // SQL 연결 클래스 import
import java.sql.PreparedStatement; // SQL 준비된 문장 클래스 import
import java.sql.ResultSet; // SQL 결과 집합 클래스 import
import java.sql.SQLException; // SQL 예외 클래스 import
import java.util.Scanner; // 사용자 입력을 받기 위한 Scanner 클래스 import

public class PddetailController { // 클래스 선언

    // 상품 상세 정보를 보여주는 메서드
    public static void showProductdetail(int memberId) {
        Scanner sc = new Scanner(System.in); // 사용자 입력을 위한 Scanner 객체 생성
        System.out.print("상품 ID를 입력하세요: "); // 사용자에게 상품 ID를 입력받음
        int PDid = sc.nextInt(); // 입력받은 상품 ID를 PDid변수에 저장

        // 상품 상세 쿼리
        String query1 = "SELECT M.mealkit_id, M.name, M.price, M.category, M.info, M.stock, COUNT(B.mealkit_id) AS bookmarknum " +
                "FROM DB2024_Mealkit M LEFT JOIN DB2024_Bookmark B ON M.mealkit_id = B.mealkit_id " +
                "WHERE M.mealkit_id = ? GROUP BY M.mealkit_id";

        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement pstmt = conn.prepareStatement(query1)) { //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성

            pstmt.setInt(1, PDid); // 첫 번째 자리표시자(?)에 상품 ID(PDid)를 설정(입력받은 상품 ID를 쿼리에 설정)
            System.out.println("==================상품 상세==================");

            try (ResultSet rs = pstmt.executeQuery()) { // 쿼리 실행 및 결과 집합 받기
                if (rs.next()) { // 결과가 존재하는지 확인
                    int id = rs.getInt("mealkit_id"); // 상품 ID 가져오기
                    String name = rs.getString("name"); // 상품 이름 가져오기
                    int price = rs.getInt("price"); // 상품 가격 가져오기
                    int stock = rs.getInt("stock"); // 재고량 가져오기
                    int bookmarknum = rs.getInt("bookmarknum"); // 북마크 수 가져오기
                    String category = rs.getString("category"); // 카테고리 가져오기
                    String info = rs.getString("info"); // 상품 설명 가져오기

                    // 상품 정보 출력
                    System.out.println("[상품 ID]" + id);
                    System.out.println("[상품 이름]" + name);
                    System.out.println("[가격]" + price);
                    System.out.println("[재고량]" + stock);
                    System.out.println("[북마크 수]" + bookmarknum);
                    System.out.println("[카테고리]" + category);
                    System.out.println("[밀키트 설명]" + info);
                }

                // 사용자에게 선택지 제공
                System.out.print("[1]장바구니 추가 [2]북마크 추가 [3]리뷰목록 [4]상품 목록  >> ");
                int mselect = sc.nextInt(); // 사용자 선택을 입력받음

                // 선택에 따라 다른 동작 수행
                if (mselect == 1) { // 장바구니에 추가
                    System.out.print("장바구니에 넣으시겠습니까? (y를 입력해야 추가됩니다.): ");
                    sc.nextLine(); // 버퍼 비우기
                    String confirm = sc.nextLine(); // 확인 입력받기
                    if ("y".equalsIgnoreCase(confirm)) {
                        addToBasket(PDid, memberId); // 장바구니에 추가하는 메서드 호출
                    } else {
                        System.out.println("상품 목록으로 돌아갑니다.");
                        Pdlistcontroller.showProductList(memberId); // 상품 목록으로 돌아감
                    }
                } else if (mselect == 2) { // 북마크에 추가
                    System.out.print("북마크에 추가하시겠습니까? (y를 입력해야 추가됩니다.): ");
                    sc.nextLine(); // 버퍼 비우기
                    String confirm = sc.nextLine(); // 확인 입력받기
                    if ("y".equalsIgnoreCase(confirm)) {
                        addToBookmark(PDid, memberId); // 북마크에 추가하는 메서드 호출
                    } else {
                        System.out.println("상품 목록으로 돌아갑니다.");
                        Pdlistcontroller.showProductList(memberId); // 상품 목록으로 돌아감
                    }
                } else if (mselect == 3) { // 해당 상품에 대한 리뷰 목록 보기
                    showReviews(PDid); // 해당 상품에 대한 리뷰 목록을 보여주는 메서드 호출
                } else if (mselect == 4) { // 상품 목록으로 돌아가기
                    Pdlistcontroller.showProductList(memberId); // 상품 목록으로 돌아감
                } else { // 잘못된 입력
                    System.out.println("올바르지 않은 입력입니다.");
                }
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }

    // 해당 상품(PDid)에 대한 리뷰를 출력하는 메서드
    private static void showReviews(int PDid) {
        // 해당 상품(PDid)에 대한 리뷰 목록 쿼리
        String query2 = "SELECT MM.id AS member_id, R.content, R.date, M.name AS product_name " +
                "FROM DB2024_Review R " +
                "INNER JOIN DB2024_Mealkit M ON R.mealkit_id = M.mealkit_id " +
                "INNER JOIN DB2024_Member MM ON R.member_id = MM.member_id " +
                "WHERE R.mealkit_id = ?";

        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement pstmt = conn.prepareStatement(query2)) { //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성

            pstmt.setInt(1, PDid); // 입력받은 상품 ID를 쿼리에 설정
            System.out.println("==================해당 상품 리뷰==================");

            try (ResultSet rs = pstmt.executeQuery()) { // 쿼리 실행 및 결과 집합 받기
                if (!rs.isBeforeFirst()) { // 리뷰가 없는 경우(결과 집합이 비어 있는 경우)
                    System.out.println("아직 등록된 리뷰가 없습니다.");
                } else { // 리뷰가 있는 경우
                    while (rs.next()) { // 결과 집합을 순회하며 리뷰 출력
                        String productName = rs.getString("product_name"); // 상품 이름 가져오기
                        String content = rs.getString("content"); // 리뷰 내용 가져오기
                        String date = rs.getString("date"); // 작성 날짜 가져오기
                        String member_id = rs.getString("member_id"); // 작성자 ID 가져오기
                        // 해당 상품에 대한 리뷰 출력
                        System.out.println("[상품이름] " + productName);
                        System.out.println("[리뷰내용] " + content);
                        System.out.println("[작성날짜] " + date);
                        System.out.println("[작성자 ID] " + member_id);
                        System.out.println();
                    }
                }
            } catch (SQLException e) { // SQL 예외 처리
                throw new RuntimeException(e);
            }

        } catch (SQLException e) { // SQL 예외 처리
            e.printStackTrace();
        }
    }

    // 장바구니에 상품을 추가하는 메서드
    private static void addToBasket(int PDid, int memberId) {
        // 상품이 이미 장바구니에 있는지 확인하는 쿼리
        String checkQuery = "SELECT COUNT(*) FROM DB2024_Basket WHERE mealkit_id = ? AND member_id = ?";
        // 장바구니에 상품을 추가하는 쿼리
        String insertQuery = "INSERT INTO DB2024_Basket (mealkit_id, member_id) VALUES (?, ?)";

        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery); // //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성(첫 번째 쿼리 준비)
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) { // //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성(두 번째 쿼리 준비)

            conn.setAutoCommit(false); // 트랜잭션 시작 (자동 커밋 비활성화)

            checkStmt.setInt(1, PDid); // 입력받은 상품 ID를 쿼리에 설정
            checkStmt.setInt(2, memberId); // 입력받은 회원 ID를 쿼리에 설정
            ResultSet rs = checkStmt.executeQuery(); // 쿼리 실행 및 결과 집합 받기
            if (rs.next() && rs.getInt(1) > 0) { // 상품이 이미 장바구니에 있는 경우
                System.out.println("이미 장바구니에 있는 상품입니다.");
                conn.rollback(); // 트랜잭션 롤백
                Pdlistcontroller.showProductList(memberId); // 상품 목록으로 돌아감
            } else { // 상품이 장바구니에 없는 경우
                insertStmt.setInt(1, PDid); // 첫 번째 자리표시자(?)에 상품 ID(PDid)를 설정(입력받은 상품 ID를 쿼리에 설정)
                insertStmt.setInt(2, memberId); // 두 번째 자리표시자(?)에 회원 ID(memberId)를 설정(입력받은 회원 ID를 쿼리에 설정)
                int rowsAffected = insertStmt.executeUpdate(); // 쿼리 실행 및 영향 받은 행 수 반환
                if (rowsAffected > 0) { // 상품이 성공적으로 추가된 경우
                    System.out.println("상품이 장바구니에 추가되었습니다.");
                    conn.commit(); // 트랜잭션 커밋
                } else { // 상품이 추가되지 않은 경우
                    System.out.println("상품을 장바구니에 추가하지 못했습니다.");
                    conn.rollback(); // 트랜잭션 롤백
                }
            }
        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        } finally {
            try (Connection conn = DBconnect.getConnection()) { // 데이터베이스 연결
                conn.setAutoCommit(true); // 자동 커밋 모드로 되돌리기
            } catch (Exception e) { // 예외 처리
                e.printStackTrace();
            }
        }
    }

    // 북마크에 상품을 추가하는 메서드
    private static void addToBookmark(int PDid, int memberId) {
        // 상품이 이미 북마크에 있는지 확인하는 쿼리
        String checkQuery = "SELECT COUNT(*) FROM DB2024_Bookmark WHERE mealkit_id = ? AND member_id = ?";
        // 북마크에 상품을 추가하는 쿼리
        String insertQuery = "INSERT INTO DB2024_Bookmark (mealkit_id, member_id) VALUES (?, ?)";

        try (Connection conn = DBconnect.getConnection(); // 데이터베이스 연결
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery); //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) { //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성

            conn.setAutoCommit(false); // 트랜잭션 시작

            checkStmt.setInt(1, PDid); // 첫 번째 자리표시자(?)에 상품 ID(PDid)를 설정(입력받은 상품 ID를 쿼리에 설정)
            checkStmt.setInt(2, memberId); // 두 번째 자리표시자(?)에 회원 ID(memberId)를 설정(입력받은 회원 ID를 쿼리에 설정)
            ResultSet rs = checkStmt.executeQuery(); // 쿼리 실행 및 결과 집합 받기
            if (rs.next() && rs.getInt(1) > 0) { // 상품이 이미 북마크에 있는 경우
                System.out.println("이미 북마크에 있는 상품입니다.");
                conn.rollback(); // 트랜잭션 롤백
                Pdlistcontroller.showProductList(memberId); // 상품 목록으로 돌아감
            } else { // 상품이 북마크에 없는 경우
                insertStmt.setInt(1, PDid); // 첫 번째 자리표시자(?)에 상품 ID(PDid)를 설정(입력받은 상품 ID를 쿼리에 설정)
                insertStmt.setInt(2, memberId); // 두 번째 자리표시자(?)에 회원 ID(memberId)를 설정(입력받은 회원 ID를 쿼리에 설정)
                int rowsAffected = insertStmt.executeUpdate(); // 쿼리 실행 및 영향 받은 행 수 반환
                if (rowsAffected > 0) { // 상품이 성공적으로 추가된 경우
                    System.out.println("상품이 북마크에 추가되었습니다.");
                    conn.commit(); // 트랜잭션 커밋
                } else { // 상품이 추가되지 않은 경우
                    System.out.println("상품을 북마크에 추가하지 못했습니다.");
                    conn.rollback(); // 트랜잭션 롤백
                }
            }
        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        } finally {
            try (Connection conn = DBconnect.getConnection()) { // 데이터베이스 연결
                conn.setAutoCommit(true); // 자동 커밋 모드로 되돌리기
            } catch (Exception e) { // 예외 처리
                e.printStackTrace();
            }
        }
    }
}
