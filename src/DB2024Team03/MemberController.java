package DB2024Team03;

import java.sql.*;
import java.util.Scanner;

public class MemberController {
	Scanner sc = new Scanner(System.in);

	//로그인 기능 - 로그인 성공시 MemberDTO에 member정보 담아서 return
	public MemberDTO login(String id, String pw) {
		//사용자에게 입력받은 id와 pw로 회원 찾는 SQL문
		String loginquery = "SELECT member_id, member_name, address FROM DB2024_Member WHERE id = ? AND password = ?";

		try (
				//미리 만들어준 클래스 사용해서 db와 연결하기
				Connection conn = DBconnect.getConnection();
				//SQL 문장을 실행하기 위해 preparedStatement 객체 생성
				PreparedStatement statement = conn.prepareStatement(loginquery)
		) {
			//SQL문에 ?매개변수에 대한 값 정하기
			statement.setString(1, id);
			statement.setString(2, pw);

			//쿼리 실행
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// 로그인 성공한다면 회원 정보 memberDTO에 저장 후 return
					int member_id = resultSet.getInt("member_id");
					String member_name = resultSet.getString("member_name");
					String address = resultSet.getString("address");
                    return new MemberDTO(member_id, member_name, address);	//쿼리 성공하면 로그인한 회원의 id return
                } else {
                    System.out.println("일치하는 아이디 혹은 비밀번호가 없습니다.");
					// 쿼리 실행 후 일치하는 결과가 없다면 해당 회원이 없다는 뜻이므로 memberDTO 객체 대신 null 전달
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 오류가 발생했습니다.");
            return null;
        }
    }


	//회원가입 기능
	public void signup(String id, String pw, String name, String address) {
		//사용자에게 id, password, 이름, 주소 값을 입력받아 member 테이블에 저장하는 INSERT SQL문
		String insertQuery = "INSERT INTO DB2024_Member (id, password, member_name, address) VALUES (?, ?, ?, ?)";

		try(
				//db와 연결
				Connection connection = DBconnect.getConnection();
				//SQL 문장을 실행하기 위해 preparedStatement 객체 생성
				PreparedStatement insertStatement = connection.prepareStatement(insertQuery)
		){
			//transaction 시작
			connection.setAutoCommit(false);

			//SQL문에 ?매개변수에 대한 값 정하기
			insertStatement.setString(1, id);
			insertStatement.setString(2, pw);
			insertStatement.setString(3, name);
			insertStatement.setString(4, address);

			try{
				//SQL문 실행
				insertStatement.executeUpdate();
				//트랜잭션 커밋
				connection.commit();
				System.out.println("회원가입 성공");
			} catch(SQLException e) {
				e.printStackTrace();
				//에러가 생기면 트랜잭션 롤백
				connection.rollback();
				System.out.println("회원가입에 실패했습니다.");
			} finally {
				// AutoCommit true로 설정
				connection.setAutoCommit(true);
			}

		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("데이터베이스 오류가 발생했습니다.");
		}
	}

	//회원가입 시 중복된 회원이 존재하는지 확인하기 위한 메서드
	public boolean MemberDuplicate(String id){
		//사용자에게 입력받은 id를 가진 member가 존재하는지 확인하는 SQL문
		String idCheckquery = "SELECT * FROM DB2024_Member WHERE id = ?";

		try(
				//db와 연결
				Connection conn = DBconnect.getConnection();
				//SQL 문장을 실행하기 위해 preparedStatement 객체 생성
				PreparedStatement statement = conn.prepareStatement(idCheckquery)
		) {
			//SQL문에 ?매개변수에 대한 값 정하기
			statement.setString(1, id);

			//SQL문 실행
			try(ResultSet resultSet = statement.executeQuery()){
				//회원이 존재하면 true, 없으면 false
				return resultSet.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("데이터베이스 오류가 발생했습니다.");
			return false;
		}
	}


	//회원의 주소 변경
	public void changeAdd(MemberDTO member) {
		System.out.print("변경할 주소를 입력하세요 >> ");
		String add = sc.nextLine();
		//사용자의 주소를 변경하는 UPDATE SQL문
		String changeAddress = "UPDATE DB2024_Member SET address = ? WHERE member_id = ?";

		try(
				//db와 연결
				Connection conn = DBconnect.getConnection();
				//SQL 문장을 실행하기 위해 preparedStatement 객체 생성
				PreparedStatement statement = conn.prepareStatement(changeAddress);
		) {
			//transaction 시작
			conn.setAutoCommit(false);

			//SQL문에 ?매개변수에 대한 값 정하기
			statement.setString(1, add);
			statement.setInt(2, member.getId());

			try{
				//SQL 문 실행
				statement.executeUpdate();
				//MemberDTO의 주소 정보 변경
				member.setAddress(add);
				System.out.println("주소 수정이 완료되었습니다.");
				//트랜잭션 커밋
				conn.commit();
			} catch (SQLException e) {
				//에러가 생기면 트랜잭션 롤백
				conn.rollback();
				throw new RuntimeException(e);
			} finally {
				// AutoCommit true로 설정
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}