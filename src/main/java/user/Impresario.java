package user;

public class Impresario extends Customer{

	private double accountbalance;

	

	public Impresario(String name, String email, String mobile, Integer userId, double accountbalance) {
		super(name, email, mobile, userId);
		this.accountbalance = accountbalance;
	}



	public double getAccountbalance() {
		return accountbalance;
	}



	@Override
	public String toString() {
		return "Impresario [accountbalance=" +this.getName()+ accountbalance + "]";
	}



	
	
	
}
