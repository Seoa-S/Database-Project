package DB2024Team03;

public class MemberDTO {
	private int id;	//회원 id(PK)
	private String name; //회원 이름

	private String address;	//회원 주소

	//memeberDTO를 생성할 때 필요한 정보를 저장하는 생성자
	public MemberDTO(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	//getter setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
