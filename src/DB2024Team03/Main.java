package DB2024Team03;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import static DB2024Team03.BasketController.deleteBasket;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
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
				
				MemberController logincon = new MemberController();
				member = logincon.login(id, pw);
				
				//로그인 성공
				if(member != null) {
					while(true) {
						System.out.println(member.getName() + "님 환영합니다!");
						System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
						System.out.println("[1]상품목록 [2]마이페이지 [3]장바구니 보러가기 [4]북마크 >> ");
						
						int mainselect = sc.nextInt();
						////이 밑으로 계속 연결하기!!!
						// 1. 상품목록
						if (mainselect == 1) {
							PdlistDTO.displayProductList();

						}

						// 2. 마이페이지
						else if (mainselect == 2) {
							System.out.println("========================마이페이지=========================");
							System.out.println(member.getName());
							System.out.println(member.getAddress());
							System.out.print("[1]북마크 [2]장바구니 [3]작성했던 리뷰 목록 [4]주문내역 [5]메인페이지로 이동 >> ");

							int myselect = sc.nextInt();

							// 1. 북마크
							if (myselect == 1) {

							}

							// 2. 장바구니
							else if (myselect == 2) {

							}

							// 3. 작성했던 리뷰 목록
							else if (myselect == 3) {

							}
						}

						else if (mainselect == 3){
							BasketController.showBasketList(member.getId());
							System.out.print("[1]모두 주문하기 [2]더 쇼핑하기 [3]상품 제거하기 [4]마이페이지 >> ");

							int basketselect = sc.nextInt();

							if (basketselect == 1){
								System.out.println("장바구니 안의 상품이 모두 주문되었습니다.");
								//장바구니에 있던 물건들 없애기
								//재고 줄이기
							}

							else if(basketselect == 2){
								//상품 리스트 화면으로 넘어가기
								PdlistDTO.displayProductList();

							}

							else if(basketselect == 3){
								BasketController.deleteBasket();
								//제거하고 싶은 상품 id 입력받기
								//입력된 상품 제거하기
								//업데이트된 장바구니 리스트 보여주기
								//다시 메뉴 보여주기

							}
							else if (basketselect == 4){
								//마이페이지로 이동
							}
							else
								System.out.println("잘못 입력하셨습니다.");

						}
					}
				}
			}
			else if(select == 2) {
				//회원가입
				sc.nextLine();
				System.out.print("아이디 : ");
				String id = sc.nextLine();
				System.out.print("비밀번호 : ");
				String pw = sc.nextLine();
				//TODO : 회원 중복확인
				System.out.print("본인이름 : ");
				String name = sc.nextLine();
				System.out.print("주소 : ");
				String addr = sc.nextLine();
				
				MemberController signupcon = new MemberController();
				signupcon.signup(id, pw, name, addr);
			}
			else if(select == 3) {
				DBconnect.closeConnection();
				break;
			}
			else System.out.println("올바르지 않은 입력입니다.");
		}
	}
}
