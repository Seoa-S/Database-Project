package DB2024Team03;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

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
					System.out.println(member.getName() + "님 환영합니다!");
					mainloop:
					while(true) {
						System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
						System.out.print("[1]상품목록 [2]마이페이지 [3]장바구니 보러가기 [4]북마크 >> ");
						
						int mainselect = sc.nextInt();
						////이 밑으로 계속 연결하기!!!
						// 1. 상품목록
						if (mainselect == 1) {
							PdlistDTO.displayProductList();

						}

						// 2. 마이페이지
						else if (mainselect == 2) {
							boolean keepGoing = true;
							while (keepGoing) {
								System.out.println("========================마이페이지=========================");
								System.out.println(member.getName());
								System.out.println(member.getAddress());
								System.out.print("[1]북마크 [2]장바구니 [3]작성했던 리뷰 목록 [4]주문내역 [5]메인페이지로 이동 >> ");

								int myselect = sc.nextInt();

								switch (myselect) {
									case 1:
										// 북마크
										break;
									case 2:
										// 장바구니
										break;
									case 3:
										// 작성했던 리뷰 목록
										ReviewController reviewController = new ReviewController();
										List<ReviewDTO> reviews = reviewController.getMemberReviews(member.getId());
										System.out.print("=====================작성했던 리뷰 목록======================");
										for (ReviewDTO review : reviews) {
											System.out.println("\n상품이름:"+ review.getProductName());
											System.out.println("리뷰내용:"+ review.getContent());
											System.out.println("작성날짜:"+ review.getDate().toString());
										}
										System.out.println("=====================");
										System.out.print("마이페이지로 돌아가시겠습니까? (y) >> ");
										sc.nextLine(); // Clear the buffer after reading an integer
										String back = sc.nextLine();
										while (!back.equalsIgnoreCase("y")) {
											System.out.println("잘못된 값을 입력하셨습니다.");
											System.out.print("마이페이지로 돌아가시겠습니까? (y) >> ");
											back = sc.nextLine();
										}
										if (back.equalsIgnoreCase("y")) {
											keepGoing = false;
										}
										break;
									case 4:
										// 주문내역 관련 코드
										break;
									case 5:
										keepGoing = false; // Exit 마이페이지 loop
										break;
								}
							}
						}

						else if (mainselect == 3){
							BasketController.showBasketList(member.getId());
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
