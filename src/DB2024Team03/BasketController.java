package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//장바구니 관련 기능이 들어있는 클래스
public class BasketController {

    //장바구니 목록을 보여주는 메소드
    public static void showBasketList(int id) {
        // DB2024_Basket 테이블에서 사용자가 담은 상품의 mealkit_id, 밀키트 이름, 가격, 카테고리를 DB2024_Mealkit테이블에서 가져와 보여준다.
        String basketlist = "SELECT DB2024_Mealkit.mealkit_id, name, price, category " +
                "From DB2024_Mealkit, DB2024_Basket " +
                "WHERE DB2024_Basket.mealkit_id=DB2024_Mealkit.mealkit_id " +
                "AND DB2024_Basket.member_id = ?";

        try (
             Connection conn = DBconnect.getConnection(); // DB 연결을 위한 정보를 가져온다.
             PreparedStatement statement = conn.prepareStatement(basketlist); // SQL 쿼리를 준비한다.
        ){
            statement.setInt(1, id); // 쿼리의 ? 부분을 해당 사용자 ID로 설정한다.

            try{
                if(UtilController.checkItemNum(id, "DB2024_Basket", conn) > 0){ // 해당 사용자의 장바구니에 상품이 있는지 확인한다.
                    ResultSet resultSet = statement.executeQuery(); // 쿼리를 실행하고 결과를 가져온다.
                    // 결과를 출력하기 위한 헤더를 출력한다.
                    System.out.println("==================장바구니 목록=====================");
                    System.out.println("[상품 번호]\t[상품명]\t\t\t[가격]\t\t[카테고리]");
                    while (resultSet.next()) { // 결과를 순회하며 각 행을 출력한다.
                        // 각 컬럼의 값을 가져온다.
                        int mealkitId = resultSet.getInt("mealkit_id"); //컬럼명이 "mealkit_id"인 것의 값을 변수 mealkitId에 대입한다.
                        String name = resultSet.getString("name"); //컬럼명이 "name"인 것의 값을 변수 name에 대입한다.
                        int price = resultSet.getInt("price"); //컬럼명이 "price"인 것의 값을 변수 price에 대입한다.
                        String category = resultSet.getString("category"); //컬럼명이 "category"인 것의 값을 변수 category에 대입한다.

                        System.out.printf("%d\t\t\t%s\t\t%d\t\t%s\n", mealkitId, name, price, category); // 결과를 출력한다.
                    }
                    System.out.println("=================================================");  // 테이블 하단의 구분선을 출력한다.
                }else { //장바구니에 담긴 상품이 없는 경우
                    System.out.println("장바구니가 비었습니다."); //장바구니가 비었다는 메세지를 보여준다.
                }
            } catch (SQLException e) {
                // SQL 쿼리 실행 중 예외가 발생한 경우 예외를 던진다.
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            // DB 연결 중 예외가 발생한 경우 예외 정보를 출력한다.
            e.printStackTrace();
        }
    }

    public static void deleteBasketItem(int id, int mealkitId) {

        String deleteItem = "DELETE FROM DB2024_Basket WHERE member_id=? AND mealkit_id=?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = DBconnect.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            statement = conn.prepareStatement(deleteItem);
            statement.setInt(1, id);
            statement.setInt(2, mealkitId);

            try {
                if (UtilController.checkIdExist(mealkitId, id, "DB2024_Basket", conn)) {

                    statement.executeUpdate();
                    System.out.print("상품이 제거되었습니다.\n");
                    conn.commit(); // 변경사항 커밋
                } else {
                    System.out.print("해당 상품ID가 장바구니에 존재하지 않습니다.\n");
                    conn.commit(); // 트랜잭션 완료 (실제로 삭제할 항목이 없는 경우에도)
                }
            } catch (SQLException e) {
                conn.rollback(); // 예외 발생 시 롤백
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // 자동 커밋 모드 재설정
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        conn.close(); // 연결 닫기
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close(); // PreparedStatement 닫기
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //주문을 했을 때 주문 내역을 업데이트하는 메소드. memberId를 변수 id로 입력받는다.
    public static void updateOrderList(int id, Connection conn){
        // 장바구니에서 주문 가능한(mealkit의 stock이 0보다 큰) 상품을 주문 목록에 삽입한다.
        String updateOrderList = "INSERT INTO DB2024_Orders (mealkit_id, member_id, orderdate) " +
                "SELECT B.mealkit_id, B.member_id, CURRENT_DATE() AS orderdate " +
                "FROM DB2024_Basket B " +
                "JOIN DB2024_Mealkit M ON B.mealkit_id = M.mealkit_id " +
                "WHERE B.member_id = ? AND M.stock > 0 ";

        try (
                PreparedStatement statement = conn.prepareStatement(updateOrderList) // 전달받은 Connection 객체를 사용하여 PreparedStatement를 생성한다.
        ) {
            statement.setInt(1, id); // 쿼리의 첫 번째 '?'에 memberId를 설정한다.

            try{
                statement.executeUpdate(); // 쿼리를 실행하여 주문 목록을 업데이트한다.
            } catch (SQLException e) {
                // SQL 쿼리 실행 중 발생한 예외를 던진다.
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            // PreparedStatement 준비 중 발생한 예외를 던진다.
            throw new RuntimeException(e);
        }
    }

    //주문한 상품들을 사용자의 장바구니에서 삭제하는 메소드
    public static void removeItems(int id, Connection conn){
        // DB2024_Basket 테이블에서 사용자의 장바구에서 재고가 0 이상인 즉, 장바구니에 담겨 있던 상품들을 삭제한다.
        String removeItems = "DELETE FROM DB2024_Basket " +
                "WHERE member_id = ? AND mealkit_id IN (" +
                "    SELECT mealkit_id" +
                "    FROM DB2024_Mealkit" +
                "    WHERE stock > 0)";

        try (
                PreparedStatement statement = conn.prepareStatement(removeItems) // 전달받은 Connection 객체를 사용하여 PreparedStatement를 생성한다.
        ) {
            statement.setInt(1, id); // 쿼리의 첫 번째 '?'에 memberID를 설정한다.

            try{
                statement.executeUpdate(); // 쿼리를 실행하여 조건에 맞는 장바구니 항목을 삭제한다.
            } catch (SQLException e) {
                // SQL 쿼리 실행 중 발생한 예외를 던진다.
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            // PreparedStatement 준비 중 발생한 예외를 던진다.
            throw new RuntimeException(e);
        }
    }


    //상품 나열 화면에서 사용자가 주문한 상품의 재고를 줄인다.
    public static void stockUpdate(int id, Connection conn){
        // DB2024_Basket 테이블과 DB2024_Mealkit 테이블을 조인한 후, 해당 사용자의 장바구니에 있는 밀키트의 상품(stock)를 1만큼 감소시킨다.
        String stockUpdate = "UPDATE DB2024_Mealkit M " +
                "INNER JOIN DB2024_Basket B ON M.mealkit_id = B.mealkit_id " +
                "SET M.stock = M.stock - 1 " +
                "WHERE B.member_id = ? AND stock >= 0";

        try (
                PreparedStatement statement = conn.prepareStatement(stockUpdate) // PreparedStatement 객체를 생성한다. 이 객체는 SQL 쿼리를 실행하는 데 사용된다.
        ) {
            statement.setInt(1, id); // 쿼리의 첫 번째 매개변수(?)에 id 값을 설정한다.

            try {
                statement.executeUpdate(); // 쿼리를 실행하여 데이터베이스를 업데이트한다.

            } catch (SQLException e) {
                // 쿼리 실행 중에 발생한 SQL 예외를 처리한다.
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            // PreparedStatement 객체 생성 중에 발생한 SQL 예외를 처리한다.
            throw new RuntimeException(e);
        }
    }

}
