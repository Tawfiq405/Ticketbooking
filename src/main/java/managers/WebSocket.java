package managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint(value = "/websocket/{id}")
public class WebSocket {

	private static Map<Integer,List<Session>> users = new HashMap<>();
	
	@OnOpen
	public void open(Session ses,@PathParam("id") String id) {
		System.out.println("open");
		synchronized (WebSocket.class) {
			Integer Id = Integer.parseInt(id);
			if(users.containsKey(Id)) {
				List<Session> sessions = users.get(Id);
				int size = sessions
							.stream()
							.filter(n->n.getId() == ses.getId())
							.toList()
							.size();
				
				
				if(size==0) {
					sessions.add(ses);
					users.put(Id, sessions);
				}
					
			}else {
				List<Session> sessions = new ArrayList<>();
				sessions.add(ses);
				users.put(Id, sessions);
			}
		}
	}
	
	@OnClose
	public void close(Session ses,@PathParam("id") String id) {
		System.out.println("close");
		Integer Id = Integer.parseInt(id);
		List<Session> sessions = users.get(Id);
		sessions.remove(ses);
		users.put(Id, sessions);
	}
	
	@SuppressWarnings("unchecked")
	@OnMessage
	public void message(@PathParam("id") String id,String message,Session ses) {
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(message);
		}catch(ParseException pe) {
			pe.printStackTrace();
		}
		String purpose = ""+json.get("purpose");
		json.put("id", id);
		switch(purpose) {
		case "ADDTHEATRE" :
			if(impresariodb.addtheatre(json).equals("200")) {
				users.get(Integer.parseInt(id)).forEach(n->{
					try {
						n.getBasicRemote().sendText(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}else {
				try {
					ses.getBasicRemote().sendText("ERROR");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case "ADDSCREEN":
			if(impresariodb.addscreen(json).equals("200")) {
				json.put("resolutions", json.get("Resolution"));
				users.get(Integer.parseInt(id)).forEach(n->{
					try {
						n.getBasicRemote().sendText(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}else {
				try {
					ses.getBasicRemote().sendText("ERROR");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		}
		
	}
	
}
