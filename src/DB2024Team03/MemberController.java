package DB2024Team03;

import java.sql.*;

public class MemberController {

	//로그인 기능 - 로그인 성공시 회원id(member_id) 리턴
	public MemberDTO login(String id, String pw) {
        String loginquery = "SELECT member_id, member_name, address FROM DB2024_Member WHERE id = ? AND password = ?";

        try (
        	//미리 만들어준 db와 연결하는 코드 불러오기
            Connection conn = DBconnect.getConnection();
            PreparedStatement statement = conn.prepareStatement(loginquery)
        ) {
            statement.setString(1, id);
            statement.setString(2, pw);

            //쿼리 실행
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("로그인 성공");
					int member_id = resultSet.getInt("member_id");
					String member_name = resultSet.getString("member_name");
					String address = resultSet.getString("address");
                    return new MemberDTO(member_id, member_name, address);	//쿼리 성공하면 로그인한 회원의 id return
                } else {
                    System.out.println("일치하는 아이디 혹은 비밀번호가 없습니다.");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 오류가 발생했습니다.");
            return null;
        }
    }

	
	public void signup(String id, String pw, String name, String address) {
		String maxIdQuery = "SELECT MAX(member_id) AS id FROM DB2024_Member";
		String insertquery = "INSERT INTO DB2024_Member (member_id, id, password, member_name, address) VALUES (?, ?, ?, ?, ?)";

		try(
				//db와 연결
				Connection connection = DBconnect.getConnection();
				PreparedStatement maxIdstatement = connection.prepareStatement(maxIdQuery);
				PreparedStatement insertstatement = connection.prepareStatement(insertquery)
		){
			//transaction 시작
			connection.setAutoCommit(false);
			
			int MaxMemberId = 0;
	        try (ResultSet maxIdresult = maxIdstatement.executeQuery()) {
	            if (maxIdresult.next()) {
	                MaxMemberId = maxIdresult.getInt("id");
	            }
	        }

	        // MaxMemberId 값이 0이라면 쿼리 결과가 비어있는 경우이므로,
	        // 회원 아이디를 1로 설정해줍니다.
	        MaxMemberId = MaxMemberId + 1;

	        insertstatement.setInt(1, MaxMemberId);
	        insertstatement.setString(2, id);
	        insertstatement.setString(3, pw);
	        insertstatement.setString(4, name);
	        insertstatement.setString(5, address);
	        
            try{
            	insertstatement.executeUpdate();
				//트랜잭션 커밋
				connection.commit();
                System.out.println("회원가입 성공");
            } catch(SQLException e) {
        		e.printStackTrace();
				// 트랜잭션 롤백
				connection.rollback();
        		System.out.println("회원가입에 실패했습니다.");
        	}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("데이터베이스 오류가 발생했습니다.");
		}
	}

	public boolean MemberDuplicate(String id){
		//해당 id를 가진 member가 존재하는지 확인하는 코드
		String idCheckquery = "SELECT * FROM DB2024_Member WHERE id = ?";

		try(
				//db와 연결
				Connection conn = DBconnect.getConnection();
				PreparedStatement statement = conn.prepareStatement(idCheckquery)
		) {
			statement.setString(1, id);

			try(ResultSet resultSet = statement.executeQuery()){
				return resultSet.next();	//회원이 존재하면 true, 없으면 false
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("데이터베이스 오류가 발생했습니다.");
			return false;
		}
	}
}
