package DB2024Team03;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		MemberController con = new MemberController();
		MemberDTO member;
		
		while(true) {
			System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
			System.out.print("[1]로그인 [2]회원가입 [3]종료 >> ");
			
			int select = sc.nextInt();
			
			if(select == 1) {
				//로그인
				sc.nextLine();
				System.out.print("아이디 : ");
				String id = sc.nextLine();
				System.out.print("비밀번호 : ");
				String pw = sc.nextLine();

				//memberDTO에 현재 로그인한 회원정보 넣기
				member = con.login(id, pw);
				
				//로그인 성공
				if(member != null) {
					while(true) {
						System.out.println(member.getName() + "님 환영합니다!");
						System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
						System.out.println("[1]상품목록 [2]마이페이지 [3]장바구니 보러가기 [4]북마크 >> ");
						
						int mainselect = sc.nextInt();
						////이 밑으로 계속 연결하기!!!
					}
				}
			}
			else if(select == 2) {
				//회원가입
				sc.nextLine();
				String id;
				while(true){
					System.out.print("아이디 : ");
					id = sc.nextLine();
					//중복되는 회원이 있는지 확인
					if(!con.MemberDuplicate(id)){
						//중복되는 회원이 없다면
						System.out.println("사용 가능한 아이디입니다.");
						break;
					} else{
						System.out.println("이미 존재하는 id입니다.");
					}
				}
				System.out.print("비밀번호 : ");
				String pw = sc.nextLine();
				System.out.print("본인이름 : ");
				String name = sc.nextLine();
				System.out.print("주소 : ");
				String addr = sc.nextLine();

				con.signup(id, pw, name, addr);
			}
			else if(select == 3) {
				DBconnect.closeConnection();
				break;
			}
			else System.out.println("올바르지 않은 입력입니다.");
		}
	}
}
