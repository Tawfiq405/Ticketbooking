package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.simple.JSONObject;



import managers.Tools;
import user.Customer;



public class LoginManager {
	public static Map<Integer,Integer> otpmap = new HashMap<>();
	
	public static Boolean isUser(String email) {
		System.out.println("-------------------------------");
		try {
			ResultSet rs = null;
			synchronized (LoginManager.class) {
				PreparedStatement userobj = Tools.DB.prepareStatement("select * from user where email = ?;");
				userobj.setString(1, email);
				rs = userobj.executeQuery();
			}
			int i=0;
			if(rs.next()) {
				i=rs.getInt(1);
				
			}else {
				synchronized (LoginManager.class) {
					i= DBmanager.insert(new Customer(email.split("@")[0],email,"0000000000", null));
					PreparedStatement updatesession = Tools.DB.prepareStatement("insert into role values (?,?)");
					updatesession.setInt(1, i);
					updatesession.setString(2, "C");
					updatesession.executeUpdate();
				}
			}
			int otp = getotp();
			System.out.println(otp);
			otpmap.put(i,otp);
			while(!Mail.send(email,"Login message from Mtbookings","your otp : "+otp)) {
			}
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private static int getotp() {
		return  (int) Math.floor(Math.random()*1000000);
	}

	@SuppressWarnings("unchecked")
	public static SimpleEntry<Boolean, JSONObject> checkotp(String email, String otp) {
		try {
			JSONObject json = new JSONObject();
			PreparedStatement userobj=null;
			ResultSet rs = null;
			synchronized (LoginManager.class) {
				userobj = Tools.DB.prepareStatement("select * from user,role where email = ? and role.userId=user.id;");
				userobj.setString(1, email);
				rs = userobj.executeQuery();
				
			}
			if(rs.next()) {
				Integer sendedotp = otpmap.get(rs.getInt(1));
				sendedotp = (Integer.parseInt(otp)==0)?0:sendedotp;
				if(sendedotp==Integer.parseInt(otp)) {
					String sessionId = getSessionId();
					userobj = Tools.DB.prepareStatement("insert into sessions values (?,?)");
					userobj.setInt(1, rs.getInt(1));
					userobj.setString(2, sessionId);
					userobj.execute();
					json.put("role", rs.getString(6));
					json.put("sessionId", sessionId);
					json.put("name", rs.getString(2));
					otpmap.remove(rs.getInt(1));
					return new SimpleEntry<Boolean,JSONObject>(true,json);
				}else {
					System.out.println(1);
					return new SimpleEntry<Boolean,JSONObject>(false,null);
				}
			}else {
				System.out.println(2);
				return new SimpleEntry<Boolean,JSONObject>(false,null);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println(3);
			return new SimpleEntry<Boolean,JSONObject>(false,null);
		}
	}

	private static String getSessionId() {
		return UUID.randomUUID().toString();
	}
}
