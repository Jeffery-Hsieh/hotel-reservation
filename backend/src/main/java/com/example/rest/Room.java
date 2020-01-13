package com.example.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Room {
	private
		String id, roomType;
		int price;
		ArrayList<String> orderIdList = new ArrayList<String>();
	
	public
		Room(String RoomID, String RoomType, int RoomPrice, ArrayList<String> OrderIdList) {
			id = RoomID;
			roomType = RoomType;
			price = RoomPrice;
			orderIdList = OrderIdList;
		}
	
		static ArrayList<Room> GetRooms(ArrayList<String> roomIdList, String HotelID) throws Exception{ 
			
			ArrayList<Room> rooms = new ArrayList<Room>();
			//TODO: get all rooms from database and create Room objects with constructor
			//RoomId = HotelID_RoomType_RoomNum, e.g., 6_S_4 means fourth single room of Hotel_6
			//Need to gather orderIdList of this room while gathering other information
			
			Connection con = DBUtil.getConnection();

			// HotelID Filter
	    	String query = "SELECT * FROM tb_hotel where HotelID = ?";
	    	PreparedStatement pst = con.prepareStatement(query);
	    	
	    	//  Need to pass value (HotelId) ******************************
	    	pst.setString(1, HotelID);
	    			        
	        // execute the query, and get a java resultset
	        ResultSet rs = pst.executeQuery();
	        
	        ArrayList<Integer> RoomPrice = new ArrayList<Integer>();			        
	        if(rs.next())
	        {
	        	RoomPrice.add(rs.getInt("singleRoomPrice")); 
	        	RoomPrice.add(rs.getInt("doubleRoomPrice"));  
	        	RoomPrice.add(rs.getInt("quadRoomPrice"));
	        }

			for(String roomId : roomIdList) {
					
		        // find OrderId from RoomID
		        query = "SELECT * FROM tb_orderid2roomid where roomID = ?";
		        pst = con.prepareStatement(query);
		        pst.setString(1, roomId);
		        rs = pst.executeQuery();
		        
		        ArrayList<String> orderIdList = new ArrayList<String>();
		        while (rs.next())
		        {	        
		        	orderIdList.add(rs.getString("orderID"));
		        }
		    
				switch(roomId.substring(roomId.indexOf('_') + 1, roomId.indexOf('_') + 2)) {
				case "S":
					rooms.add(new Room(roomId, "Single", RoomPrice.get(0), orderIdList));
					break;
				case "D":
					rooms.add(new Room(roomId, "Double", RoomPrice.get(1), orderIdList));
					break;
				case "Q":
					rooms.add(new Room(roomId, "Quad", RoomPrice.get(2), orderIdList));
					break;
				}
			}

			con.close();
			
			return rooms;
		}
		
		Map<String, String> GetRoomDetail(){
			Map<String, String> roomDetail = new HashMap<String, String>();
			roomDetail.put("RoomID", id);
			roomDetail.put("RoomType", roomType);
			roomDetail.put("RoomPrice", Integer.toString(price));
			return roomDetail;
		}
	 
		boolean CheckAvailable(String startDate, String endDate) throws Exception {
			ArrayList<Order> ordersOfThisRoom = Order.GetOrders(orderIdList);
			for(Order order : ordersOfThisRoom) {
				if(order.IsOccupied(startDate, endDate)) {
					return false;
				}
			}
			return true;
		}
		
		void AddOrder(String orderId) {
			orderIdList.add(orderId);
		}
		
		/*void RemoveOrder(String orderId) {  //no need, Room object shouldn't exist
			orderIdList.remove(orderId);
		}*/		
		
		String GetRoomID() {
			return id;
		}
		
		int GetRoomPrice() {
			return price;
		}
		
		String GetRoomType() {
			return roomType;
		}
}
