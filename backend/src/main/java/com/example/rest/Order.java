package com.example.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.xml.bind.annotation.XmlElement;

public class Order {
	private
		@XmlElement(name="orderID")
		String id = "";
		@XmlElement(name="hotelID")
		String hotelId = "";
		@XmlElement(name="check_in_date")
		String startDate;
		@XmlElement(name="check_out_date")
		String endDate;
		@XmlElement
		String customerName, commentId;
		@XmlElement
		int totalPrice = 0, singleRoomNum = 0, doubleRoomNum = 0, quadRoomNum = 0;
		@XmlElement
		int isPaid = 0;
		
		ArrayList<String> roomIdList = new ArrayList<String>();
		
	public
		Order(){};

		// discussion
		Order(String StartDate, String EndDate, String CustomerName, String HotelID, ArrayList<Room> RoomList) {  //initialize
			id = UUID.randomUUID().toString().substring(0, 4);
			startDate = StartDate;
			endDate = EndDate;
			customerName = CustomerName;
			hotelId = HotelID;

			for(Room room : RoomList) {
				// Discussion
				roomIdList.add(room.GetRoomID());
				totalPrice += room.GetRoomPrice();
				room.AddOrder(id);
				
				switch(room.GetRoomType()) {
				case "Single":
					singleRoomNum += 1;
					break;
				case "Double":
					doubleRoomNum += 1;
					break;
				case "Quad":
					quadRoomNum += 1;
					break;
				}
			}
			
			totalPrice *= CalculateDays();  //calculate total price
		}
		
		// get order from database
		Order(String OrderID, String StartDate, String EndDate, String CustomerName, String HotelID, int TotalPrice, int SingleRoomNum, int DoubleRoomNum, int QuadRoomNum, int isPaid) {
			id = OrderID;
			startDate = StartDate;
			endDate = EndDate;
			customerName = CustomerName;
			hotelId = HotelID;
			totalPrice = TotalPrice;
			singleRoomNum = SingleRoomNum;
			doubleRoomNum = DoubleRoomNum;
			quadRoomNum = QuadRoomNum;
			this.isPaid = isPaid; 
		}
		
		void newOrder() throws Exception {
			
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
		    
		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
	
		    	String query = "INSERT INTO `hotelsystem`.`tb_order` (`OrderID`, `StartDate`, `EndDate`, `CustomerName`, `HotelID`,"
		    			+ " `TotalPrice`, `SingleRoomNum`, `DoubleRoomNum`, `QuadRoomNum`, `IsPaid`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, '0');";
		    	PreparedStatement pst = con.prepareStatement(query);
		    	
		    	pst.setString (1, id); // UUID
		    	pst.setString (2, startDate);
		    	pst.setString (3, endDate);
		    	pst.setString (4, customerName);
		    	pst.setString (5, hotelId);
		    	pst.setInt (6, totalPrice);
		    	pst.setInt (7, singleRoomNum);
		    	pst.setInt (8, doubleRoomNum);
		    	pst.setInt (9, quadRoomNum);
		
				
				pst.execute();
				
		        for(String roomId : roomIdList) {
		        	
		        	query = "INSERT INTO `hotelsystem`.`tb_orderid2roomid` (`orderID`, `roomID`) VALUES (?, ?);";
		        	pst = con.prepareStatement(query);
		        	
		        	pst.setString (1, id);
		        	pst.setString (2, roomId);
		        	
		        	pst.execute();		        	
		        }

		        con.close();
	        
		        
		    } catch (SQLException ex) {
				System.out.println(ex);
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
		}

		int CalculateDays() {
			int startMonth = Integer.parseInt(startDate.substring(4, 6)), endMonth = Integer.parseInt(endDate.substring(4, 6));
			int startDay = Integer.parseInt(startDate.substring(6)) + CalculateTotalDaysBeforeEachMonth(startMonth), endDay = Integer.parseInt(endDate.substring(6)) + CalculateTotalDaysBeforeEachMonth(endMonth);
			return endDay - startDay;
		}
		
		static ArrayList<Order> GetOrders(ArrayList<String> orderIdList) throws Exception{
			
			ArrayList<Order> orders = new ArrayList<Order>();
			//TODO: get all orders from database and create Order objects with constructor
			// DB connect information
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
			
		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
		    	
		    	for(String orderId : orderIdList) {

			    	String query = "SELECT * FROM tb_order where OrderID = ?";
			    	PreparedStatement pst = con.prepareStatement(query);
			    	
			    	pst.setString(1, orderId);
			    	ResultSet rs = pst.executeQuery();
			        
			        if (rs.next())
			        {	        
			        	orders.add(new Order(rs.getString("OrderID"), 
			        						 rs.getString("StartDate"), 
			        						 rs.getString("EndDate"), 
			        						 rs.getString("CustomerName"),
			        						 rs.getString("HotelID"),
			        						 rs.getInt("TotalPrice"),
			        						 rs.getInt("singleRoomNum"), 
			        						 rs.getInt("doubleRoomNum"), 
			        						 rs.getInt("quadRoomNum"),
			        						 rs.getInt("isPaid")));
			        }
		        
			        con.close();

		    	}
		    	
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
		    //test
			//orders.add(new Order(orderIdList.get(0), "20200103", "20200105", "Hao", "hotelID", 567, 0, 3, 1));
			//orders.add(new Order(orderIdList.get(1), "20201006", "20201008", "Hao", "hotelID", 789, 2, 0, 0));
			//end test
			
			return orders;
		}
		
		static Order GetOrder(String orderId) throws Exception {
			
			//TODO: get only one order detail from database and create Order object with constructor, below is an example
			// For updating order
			Order order = null;
			
			String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
			
		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
	
		    	// HotelID Filter
		    	String query = "SELECT * FROM tb_order where OrderID = ?";
		    	PreparedStatement pst = con.prepareStatement(query);
		    	
		    	//  Need to pass value from controller
		    	pst.setString(1, orderId);
		    				        
		        // execute the query, and get a java resultset
		        ResultSet rs = pst.executeQuery();
		        
		        if(rs.next())
		        {
		        	order = new Order(rs.getString("OrderID"), 
 			   						  rs.getString("StartDate"), 
 			   						  rs.getString("EndDate"), 
			   						  rs.getString("CustomerName"),
			   						  rs.getString("HotelID"),
			   						  rs.getInt("TotalPrice"),
			   						  rs.getInt("singleRoomNum"), 
			   						  rs.getInt("doubleRoomNum"), 
			   						  rs.getInt("quadRoomNum"),
			   						  rs.getInt("isPaid"));
		        }
		        else
		        {
		        	order = new Order();
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
			return order;
		}

		// Map<String, String> GetOrderDetail(){
		// 	Map<String, String> orderDetail = new HashMap<String, String>();
		// 	orderDetail.put("OrderID", id);
		// 	orderDetail.put("HotelID", hotelId);
		// 	orderDetail.put("CommentID", commentId);
		// 	orderDetail.put("StartDate", startDate);
		// 	orderDetail.put("EndDate", endDate);
		// 	orderDetail.put("CustomerName", customerName);
		// 	orderDetail.put("TotalPrice", Integer.toString(totalPrice));
		// 	orderDetail.put("IsPaid", Boolean.toString(isPaid));
		// 	orderDetail.put("SingleRoomNumber", Integer.toString(singleRoomNum));
		// 	orderDetail.put("DoubleRoomNumber", Integer.toString(doubleRoomNum));
		// 	orderDetail.put("QuadRoomNumber", Integer.toString(quadRoomNum)); 
		// 	return orderDetail;
		// }
	
		void updateOrder(int isPaid) throws Exception {
			
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
			
		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
	
		    	String query = "UPDATE `hotelsystem`.`tb_order` SET `IsPaid` = '1' WHERE (`OrderID` = ?)";
		    	PreparedStatement pst = con.prepareStatement(query);
		    	pst.setString(1, id);
		    	pst.executeUpdate();

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
			
		}

		boolean CheckPayment() {
			if (isPaid == 0)
				return false;
			return true;
		}
		
		void ConfirmPayment() {
			isPaid = 1;
		}	
		
		boolean IsOccupied(String startDate_input, String endDate_input) {
			int startMonth = Integer.parseInt(startDate.substring(4, 6)), endMonth = Integer.parseInt(endDate.substring(4, 6));
			int startDay = Integer.parseInt(startDate.substring(6)) + CalculateTotalDaysBeforeEachMonth(startMonth), endDay = Integer.parseInt(endDate.substring(6)) + CalculateTotalDaysBeforeEachMonth(endMonth);
			
			int startMonth_input = Integer.parseInt(startDate_input.substring(4, 6)), endMonth_input = Integer.parseInt(endDate_input.substring(4, 6));
			int startDay_input = Integer.parseInt(startDate_input.substring(6)) + CalculateTotalDaysBeforeEachMonth(startMonth_input), endDay_input = Integer.parseInt(endDate_input.substring(6)) + CalculateTotalDaysBeforeEachMonth(endMonth_input);
			
			if(startDay_input <= startDay && endDay <= endDay_input) {
				return true;  //earlier check in and later check out
			}
			else if(startDay_input <= startDay && endDay_input <= endDay && startDay <= endDay_input) {
				return true;  //earlier check in but check out conflict
			}
			else if(startDay <= startDay_input && startDay_input <= endDay && endDay <= endDay_input) {
				return true;  //later check out but check in conflict
			}
			else if(startDay <= startDay_input && endDay_input <= endDay) {
				return true;  //later check in and earlier check out
			}
			return false;
		}
		
		int CalculateTotalDaysBeforeEachMonth(int month) {
			switch(month) {
			case 1:
				return 0;
			case 2:
				return 31;
			case 3: 
				return 31+28;
			case 4:
				return 31+28+31;
			case 5:
				return 31+28+31+30;
			case 6:
				return 31+28+31+30+31;
			case 7:
				return 31+28+31+30+31+30;
			case 8:
				return 31+28+31+30+31+30+31;
			case 9:
				return 31+28+31+30+31+30+31+31;
			case 10:
				return 31+28+31+30+31+30+31+31+30;
			case 11:
				return 31+28+31+30+31+30+31+31+30+31;
			case 12:
				return 31+28+31+30+31+30+31+31+30+31+30;
			default:
				return 0;
			}
		}
		
		void CancelOrder() throws Exception {
			
			//TODO: remove this order in database
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;
		    
		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
	
		    	// HotelID Filter
		    	String query = "delete from tb_order where OrderID = ?";
		    	PreparedStatement pst = con.prepareStatement(query);
		    	pst.setString(1, id);
		    	pst.executeUpdate();
				query = "delete from tb_orderid2roomid where OrderID = ?";
		    	pst = con.prepareStatement(query);
		    	pst.setString(1, id);
		    	pst.executeUpdate();
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
		}
		
		void AddComment(Comment comment) throws Exception{
			
			//TODO: write comment to database within hotelID
			//TODO: remove this order in database
		    String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";
		    String username = "OOAD";
		    String password = "caece";
		    Connection con = null;

		    try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	con = DriverManager.getConnection(url, username, password);
	
		    	String query = "INSERT INTO `hotelsystem`.`tb_comment` (`HotelID`, `Comment`, `Star`, `OrderID`)"
		    			+ " VALUES (?, ?, ?, ?);";
		    	PreparedStatement pst = con.prepareStatement(query);
		    	pst.setString(1, hotelId);
				pst.setString(2, comment.GetDescription());
				pst.setInt(3, comment.GetStarRate());
				pst.setString(4, this.id);
		    	pst.execute();
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
		}

		String GetCustomerName() {
			return this.customerName;
		}

		String GetHotelID() {
			return this.hotelId;
		}
		
		String GetID() {
			return this.id;
		}

		String GetStartDate() {
			return this.startDate;
		}

		String GetEndDate() {
			return this.endDate;
		}

		ArrayList<String> GetRoomIDList() {
			return this.roomIdList;
		}

		int GetTotalPrice() {
			return this.totalPrice;
		}

}
