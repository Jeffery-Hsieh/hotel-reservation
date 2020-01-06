package com.example.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Hotel {
	@XmlElement
	private String id, region, address;
	@XmlElement
	private int hotelStar;

	private int singleRoomNum, doubleRoomNum, quadRoomNum;
	private int singleRoomPrice, doubleRoomPrice, quadRoomPrice;
	ArrayList<String> singleRoomIdList, doubleRoomIdList, quadRoomIdList, commandIdList;
	ArrayList<Room> bookedRooms;


	public
		Hotel(){};
		Hotel(String HotelID, int HotelStar, String Locality, String Street_Address, int singleRoomNum, int doubleRoomNum, int quadRoomNum,
			  int singleRoomPrice, int doubleRoomPrice, int quadRoomPrice) {
			id = HotelID;
			hotelStar = HotelStar;
			region = Locality;
			address = Street_Address;
			this.singleRoomNum = singleRoomNum;
			this.doubleRoomNum = doubleRoomNum;
			this.quadRoomNum = quadRoomNum;
			this.singleRoomPrice = singleRoomPrice;
			this.doubleRoomPrice = doubleRoomPrice;
			this.quadRoomPrice = quadRoomPrice;
			singleRoomIdList = new ArrayList<String>();
			doubleRoomIdList = new ArrayList<String>();
			quadRoomIdList = new ArrayList<String>();
			commandIdList = new ArrayList<String>();
			
			//create all singleRooms ID list
			for(int i = 1; i <= singleRoomNum; ++i) {
				singleRoomIdList.add(id + "_S_" + i);
			}
			//create all doubleRooms ID list
			for(int i = 1; i <= doubleRoomNum; ++i) {
				doubleRoomIdList.add(id + "_D_" + i);
			}
			//create all quadRooms ID list
			for(int i = 1; i <= quadRoomNum; ++i) {
				quadRoomIdList.add(id + "_Q_" + i);
			}
		}
		
		static Hotel GetHotel(String HotelID) throws Exception {
			
			Hotel hotel = null;
			
			String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
		    
			try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
			    	con = DriverManager.getConnection(url, username, password);
 	
			    	// HotelID Filter
			    	String query = "SELECT * FROM tb_hotel where HotelID = ?";
			    	PreparedStatement pst = con.prepareStatement(query);
			    	
			    	//  Need to pass value from controller
			    	pst.setString(1, HotelID);
			    				        
			        // execute the query, and get a java resultset
			        ResultSet rs = pst.executeQuery();
			        			        			        
			        if(rs.next())
			        {
				        hotel = new Hotel(rs.getString("HotelID"), 
			     						  rs.getInt("HotelStar"), 
			    						  rs.getString("Locality"), 
			    						  rs.getString("Street_Address"), 
			    						  rs.getInt("singleRoomNum"), 
			    						  rs.getInt("doubleRoomNum"), 
			    						  rs.getInt("quadRoomNum"),
			    						  rs.getInt("singleRoomPrice"), 
			    						  rs.getInt("doubleRoomPrice"), 
			    						  rs.getInt("quadRoomPrice"));
			        }
			        con.close();
			        
			    } catch (SQLException ex) {
			        throw new Error("Error ", ex);
			    } finally {
			      try {
			        if (con != null) {
			            con.close();
			        }
			      } catch (SQLException ex) {
			          System.out.println(ex.getMessage());
			      }
			}
			return hotel;
		}

		static ArrayList<Hotel> GetHotels(String locality) throws Exception {
			ArrayList<Hotel> hotels = new ArrayList<Hotel>();
			
			// TODO: get all hotel from database and create Hotel objects with constructor
		
			// DB connect information
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
			try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
			    	con = DriverManager.getConnection(url, username, password);

			    	// System.out.println("Connected!");
			    	
			    	// SQL query
			    	// if you only need a few columns, specify them by name instead of using "*"
			    	/* 
			    	String query = "SELECT * FROM tb_hotel";
			    	create the java statement
			    	Statement st = con.createStatement();
			    	*/
			    	
			    	// Locality Filter
			    	String query = "SELECT * FROM tb_hotel where Locality = ?";
			    	PreparedStatement pst = con.prepareStatement(query);
			    	
			    	//  Need to pass value from controller
			    	pst.setString(1, locality);
		    	
			        // execute the query, and get a java resultset
			        ResultSet rs = pst.executeQuery();
			        
			        while (rs.next())
			        {	        
			        	hotels.add(new Hotel(rs.getString("HotelID"), 
			        						 rs.getInt("HotelStar"), 
			        						 rs.getString("Locality"), 
			        						 rs.getString("Street_Address"), 
			        						 rs.getInt("singleRoomNum"), 
			        						 rs.getInt("doubleRoomNum"), 
			        						 rs.getInt("quadRoomNum"),
			        						 rs.getInt("singleRoomPrice"), 
			        						 rs.getInt("doubleRoomPrice"), 
			        						 rs.getInt("quadRoomPrice")));
			        }
			        con.close();
			        
			    } catch (SQLException ex) {
			        throw new Error("Error ", ex);
			    } finally {
			      try {
			        if (con != null) {
			            con.close();
			        }
			      } catch (SQLException ex) {
			          System.out.println(ex.getMessage());
			      }
			}
			
			return hotels;
		}
		
		Map<String, String> GetHotelDetail(){
			Map<String, String> hotelDetail = new HashMap<String, String>();
			hotelDetail.put("HotelID", id);
			hotelDetail.put("Locality", region);
			hotelDetail.put("Street_Address", address);
			hotelDetail.put("HotelStar", Integer.toString(hotelStar));
			hotelDetail.put("singleRoomNum", Integer.toString(singleRoomNum));
			hotelDetail.put("doubleRoomNum", Integer.toString(doubleRoomNum));
			hotelDetail.put("quadRoomNum", Integer.toString(quadRoomNum));
			hotelDetail.put("singleRoomPrice", Integer.toString(singleRoomPrice));
			hotelDetail.put("doubleRoomPrice", Integer.toString(doubleRoomPrice));
			hotelDetail.put("quadRoomPrice", Integer.toString(quadRoomPrice));
			return hotelDetail;
		}
		
		static ArrayList<Comment> GetComments(String HotelID) throws Exception{

			ArrayList<Comment> comments = new ArrayList<Comment>();
			
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
			try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
			    	con = DriverManager.getConnection(url, username, password);

			    	String query = "SELECT * FROM tb_comment where HotelID = ?";
			    	PreparedStatement pst = con.prepareStatement(query);
			    	
			    	pst.setString(1, HotelID);
		    	
			        // execute the query, and get a java resultset
			        ResultSet rs = pst.executeQuery();
			        
			        while (rs.next())
			        {	        
						Comment comment = new Comment(rs.getString("Comment"), rs.getInt("Star"));
			        	comments.add(comment);
			        }
			        con.close();
			        
			    } catch (SQLException ex) {
			        throw new Error("Error ", ex);
			    } finally {
			      try {
			        if (con != null) {
			            con.close();
			        }
			      } catch (SQLException ex) {
			          System.out.println(ex.getMessage());
			      }
			}
			
			return comments;
		}

		ArrayList<Room> GetAvailableRooms(String startDate, String endDate, String roomType) throws Exception{  //Search for only one room type at a time
			ArrayList<Room> availableRooms = new ArrayList<Room>();
			switch(roomType) {
			case "Single":
				ArrayList<Room> singleRooms = Room.GetRooms(singleRoomIdList, id);
				for(Room room : singleRooms) {
					if(room.CheckAvailable(startDate, endDate))
						availableRooms.add(room);
				}
			case "Double":
				ArrayList<Room> doubleRooms = Room.GetRooms(doubleRoomIdList, id);
				for(Room room : doubleRooms) {
					if(room.CheckAvailable(startDate, endDate))
						availableRooms.add(room);
				}
			case "Quad":
				ArrayList<Room> quadRooms = Room.GetRooms(quadRoomIdList, id);
				for(Room room : quadRooms) {
					if(room.CheckAvailable(startDate, endDate)){
						availableRooms.add(room);
					}
				}
			}
			return availableRooms;
		}
}
