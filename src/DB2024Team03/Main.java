package DB2024Team03;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import static DB2024Team03.BasketController.deleteBasketItem;

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
					System.out.println(member.getName() + "님 환영합니다!");
					while(true) {
						System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
						System.out.print("[1]상품목록 [2]마이페이지 [3]장바구니 보러가기 >> ");
						
						int mainselect = sc.nextInt();

						// 1. 상품목록
						if (mainselect == 1) {
							Pdlistcontroller.displayProductList();

						}

						// 2. 마이페이지
						else if (mainselect == 2) {
							boolean keepGoing = true;
							while (keepGoing) {
								System.out.println("========================마이페이지=========================");
								System.out.println(member.getName());
								System.out.println(member.getAddress());
								System.out.print("[1]북마크 [2]장바구니 [3]작성했던 리뷰 목록 [4]주문내역 [5]메인페이지 >> ");

								int myselect = sc.nextInt();

								switch (myselect) {
									case 1:
										// 북마크
										System.out.println("================나의 북마크 목록=================");
										BookmarkController.displayBookmarkList(member.getId());
										System.out.print("[1]북마크 제거 [2]메인페이지 >> ");

										int bookmarkselect = sc.nextInt();

										if (bookmarkselect == 1){
											System.out.print("제거하고 싶은 상품ID를 입력해주세요 >>");
											int BookmarkId = sc.nextInt();
											BookmarkController.deleteBookmarkItem(member.getId(), BookmarkId);
										}
										else if(bookmarkselect == 2){
											continue;
										}
										else System.out.println("잘못 입력하셨습니다.");

										break;
									case 2:
										// 장바구니
										break;
									case 3:
										// 작성했던 리뷰 목록
										ReviewController reviewController = new ReviewController();
										reviewController.displayMemberReviews(member.getId());
										if (reviewController.promptReturnToMyPage(sc)) {
											keepGoing = true; // Maintain loop in MyPage if user inputs 'y'
										}
										break;
									case 4:
										// 주문 내역
										System.out.println("================나의 주문내역=================");
										OrdersController.displayOrdersList(member.getId(), sc);
										break;
									case 5:
										keepGoing = false; // Exit 마이페이지 loop
										break;
								}
							}
						}

						// 3. 장바구니
						else if (mainselect == 3){
							BasketController.showBasketList(member.getId());
							System.out.print("[1]모두 주문하기 [2]더 쇼핑하기 [3]상품 제거하기 [4]메인페이지 >> ");

							int basketselect = sc.nextInt();

							if (basketselect == 1){

								BasketController.updateOrderList(member.getId());//주문내역 업데이트
								BasketController.removeItems(member.getId()); //장바구니에 있던 물건들 없애기
								BasketController.stockUpdate(member.getId());//재고 줄이기

								System.out.println("장바구니 안의 상품이 모두 주문되었습니다.");

							}

							else if(basketselect == 2){
								//상품 리스트 화면으로 넘어가기
								Pdlistcontroller.displayProductList();

							}

							else if(basketselect == 3){
								//제거하고 싶은 상품 basketId 입력받기
								System.out.print("제거하고 싶은 상품ID를 입력해주세요 >>");
								int basketId = sc.nextInt();
								BasketController.deleteBasketItem(member.getId(), basketId);
							}
							else if (basketselect == 4){
								//메인페이지로 이동
								continue;
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
				//TODO : 회원 중복확인
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
