package managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.Customer;

public class Tools {
	public static Connection DB;
	
//	public static int insert(Customer cus) throws SQLException{
//		try {
//			PreparedStatement ps = Tools.DB.prepareStatement("insert into user (name,email,mobile,password) values (?,?,?,?)");
//			ps.setString(1, cus.getName());
//			ps.setString(2, cus.getEmail());
//			ps.setString(3, cus.getMobile());
//			ps.setString(4, cus.getPass());
//			ps.execute();
//			ps = Tools.DB.prepareStatement("insert into role values (?,?)");
//			int i=getId(cus);
//			ps.setInt(1, i);
//			ps.setString(2, "C");
//			ps.execute();
//			return i;
//		}catch(SQLException ex) {
//			throw new SQLException(ex.getMessage());
//		}
//	}
//	
//	public static synchronized int getId(Customer cus) throws SQLException {
//		try {
//		PreparedStatement ps = Tools.DB.prepareStatement("select userId from user where email = ? and mobile = ? ;");
//		ps.setString(1, cus.getEmail());
//		ps.setString(2, cus.getMobile());
//		ResultSet rs = ps.executeQuery();
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		}catch(SQLException ex) {
//			throw new SQLException(ex.getMessage());
//		}
//		return 0;
//	}
}
