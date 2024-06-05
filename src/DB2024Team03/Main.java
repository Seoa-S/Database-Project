package DB2024Team03;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import static DB2024Team03.BasketController.deleteBasketItem;

public class Main {

	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		MemberController Mcon = new MemberController();
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

				//memberDTO에 memberController의 login 메서드에 id,pw 전달 후 return된 member객체 저장
				member = Mcon.login(id, pw);

				//로그인 성공 (만약 DTO의 값이 null이면 해당 회원의 정보가 없다는 뜻)
				if(member != null) {
					System.out.println(member.getName() + "님 환영합니다!");
					while(true) {
						System.out.println("==================자취생을 위한 밀키트 쇼핑몰==================");
						System.out.print("[1]상품목록 [2]마이페이지 [3]장바구니 보러가기 [4]로그아웃 >> ");
						
						int mainselect = sc.nextInt();

						// 1. 상품목록
						if (mainselect == 1) {
							Pdlistcontroller.displayProductList(member.getId());

						}

						// 2. 마이페이지
						else if (mainselect == 2) {
							boolean keepGoing = true;
							while (keepGoing) {
								System.out.println("========================마이페이지=========================");
								System.out.println(member.getName());
								System.out.println(member.getAddress());
								System.out.print("[1]북마크 [2]작성했던 리뷰 목록 [3]주문내역 [4]내 정보 수정 [5]메인페이지 >> ");

								int myselect = sc.nextInt();

								switch (myselect) {
									case 1:
										// 북마크
										System.out.println("================나의 북마크 목록=================");
										BookmarkController.displayBookmarkList(member.getId());
										System.out.print("[1]북마크 제거 [2]마이페이지 >> ");

										int bookmarkselect = sc.nextInt();

										if (bookmarkselect == 1){
											System.out.print("제거하고 싶은 상품ID를 입력해주세요 >>");
											int MealkitId = sc.nextInt();
											BookmarkController.deleteBookmarkItem(member.getId(), MealkitId);
										}
										else if(bookmarkselect == 2){
											continue;
										}
										else System.out.println("잘못 입력하셨습니다.");

										break;
									case 2:
										// 작성했던 리뷰 목록
										ReviewController reviewController = new ReviewController();
										reviewController.displayMemberReviews(member.getId());
										if (reviewController.promptReturnToMyPage(sc)) {
											keepGoing = true; // Maintain loop in MyPage if user inputs 'y'
										}
										break;
									case 3:
										// 주문 내역
										System.out.println("================나의 주문내역=================");
										OrdersController.displayOrdersList(member.getId(), sc);
										break;
									case 4:
										Mcon.changeAdd(member);
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

								Connection conn = null;
								try {
									conn = DBconnect.getConnection();
									conn.setAutoCommit(false);

									BasketController.updateOrderList(member.getId(), conn);//주문내역 업데이트
									BasketController.removeItems(member.getId(), conn); //장바구니에 있던 물건들 없애기
									BasketController.stockUpdate(member.getId(), conn);//재고 줄이기

									conn.commit();
									System.out.println("장바구니 안의 상품이 모두 주문되었습니다.");

								} catch (SQLException e) {
                                    //throw new RuntimeException(e);
									if (conn != null){
										conn.rollback();
									}
									e.printStackTrace();
                                } finally {
									if(conn != null){
										conn.setAutoCommit(true);
									}
								}


							}

							else if(basketselect == 2){
								//상품 리스트 화면으로 넘어가기
								Pdlistcontroller.displayProductList(member.getId());

							}

							else if(basketselect == 3){
								//장바구니에 상품이 존재하는지 확인
								int itemNum = BasketController.checkBasket(member.getId());
								if(itemNum > 0){
									//제거하고 싶은 상품 mealkitId 입력받기
									System.out.print("제거하고 싶은 상품ID를 입력해주세요 >>");
									int mealkitId = sc.nextInt();
									BasketController.deleteBasketItem(member.getId(), mealkitId);
								}
								else System.out.println("제거할 상품이 없습니다.");

							}
							else if (basketselect == 4){
								//메인페이지로 이동
								continue;
							}
							else
								System.out.println("잘못 입력하셨습니다.");

						}
						//로그아웃
						else if (mainselect == 4) {
							//memberDTO null로 바꾸기
							member = null;
							System.out.println("로그아웃 되었습니다.");
							break;
						}
						else
							System.out.println("잘못된 번호를 입력하셨습니다.");
					}
				}
			}
			else if(select == 2) {
				//회원가입
				sc.nextLine();
				String id;
				//중복되는 회원이 없을 때 까지 while문으로 id 입력받기
				while(true){
					System.out.print("아이디 : ");
					id = sc.nextLine();
					//중복되는 회원이 있는지 확인
					// MemberController의 MemberDuplicate 메소드에 id 매개변수로 넣어서 실행
					if(!Mcon.MemberDuplicate(id)){
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

				//id, pw, 이름, 주소 입력받아서 signup 메소드에 전달
				Mcon.signup(id, pw, name, addr);
			}
			else if(select == 3) {
				//종료하기 버튼 선택 시 db 종료
				DBconnect.closeConnection();
				break;
			}
			else System.out.println("올바르지 않은 입력입니다.");
		}
	}
}
