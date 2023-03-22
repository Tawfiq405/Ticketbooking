package user;

public class Customer {
	private String name;
	private String email;
	private String mobile;
	private Integer userId;
	public Customer(String name, String email, String mobile, Integer userId) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getMobile() {
		return mobile;
	}
	public Integer getUserId() {
		return userId;
	}
	
}
