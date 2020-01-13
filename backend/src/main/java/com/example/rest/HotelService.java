package com.example.rest;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/hotels")
public class HotelService {
  // show available hotels in the region
  @GET
  @Produces("application/json")
  public Hotel[] showAvailableHotels(@QueryParam("region") String region) throws Exception {
    ArrayList<Hotel> hotels = SearchControl.ShowHotels(region);

    return hotels.toArray(new Hotel[0]);
  }
  
  @GET
  @Path("/{hotelID}/comments")
  @Produces("application/json")
  public Comment[] showHotelComments(@PathParam("hotelID") String hotelID) throws Exception {
    ArrayList<Comment> comments = Hotel.GetComments(hotelID);
    return comments.toArray(new Comment[0]);
  }

  // show available rooms
  @GET
  @Path("/{hotelID}")
  @Produces("application/json")
  public Response showAvailableRooms(
    @PathParam("hotelID") String hotel_id,
    @QueryParam("check_in_date") String check_in_date,
    @QueryParam("check_out_date") String check_out_date
  ) throws Exception
  {
    HashMap<String, Integer> result = SearchControl.ShowAvailableRooms(hotel_id, check_in_date, check_out_date);

    Gson gson = new Gson();
		String jsonString = gson.toJson(result);

		return Response.status(Response.Status.OK).entity(jsonString).build();
  }
  
  @POST
  @Path("/{hotelID}")
  @Produces("applicagtion/json")
  @Consumes("application/json")
  public Order ConfirmBooking(
    @PathParam("hotelID") String hotelID, Order order
  ) throws Exception
  {
    HashMap<String, Integer> requirement = new HashMap<String, Integer>();
    if(order.singleRoomNum != 0){
      requirement.put("Single", order.singleRoomNum);
    }
    if(order.doubleRoomNum != 0){
        requirement.put("Double", order.doubleRoomNum);
    }
    if(order.quadRoomNum != 0){
        requirement.put("Quad", order.quadRoomNum);
    }

    System.out.println(order.GetCustomerName());
    Order newOrder = SearchControl.addOrder(order.startDate, order.endDate, order.customerName, hotelID, requirement);
    newOrder.newOrder();

    return newOrder;
  }
}