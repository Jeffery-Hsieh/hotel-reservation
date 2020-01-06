package com.example.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/orders")
public class OrderService {
    // show order detail
    @GET
    @Path("/{orderID}")
    @Produces("application/json")
    public Order showOrderDetail(@PathParam("orderID") String orderID) throws Exception 
    {
        Order order = Order.GetOrder(orderID);
        return order;
    }

    // update order
    @POST
    @Path("/{orderID}")
    @Produces("application/json")
    public Order updateOrder(@PathParam("orderID") String orderID, Order tmpOrder) throws Exception
    {

        Order order = Order.GetOrder(orderID);
        order.CancelOrder();

        HashMap<String, Integer> requirement = new HashMap<String, Integer>();
        if(tmpOrder.singleRoomNum != 0){
            requirement.put("Single", tmpOrder.singleRoomNum);
        }
        if(tmpOrder.doubleRoomNum != 0){
            requirement.put("Double", tmpOrder.doubleRoomNum);
        }
        if(tmpOrder.quadRoomNum != 0){
            requirement.put("Quad", tmpOrder.quadRoomNum);
        }
        System.out.println(requirement);
        
        Order newOrder = SearchControl.addOrder(tmpOrder.GetStartDate(), tmpOrder.GetEndDate(), order.GetCustomerName(), order.GetHotelID(), requirement);
        newOrder.newOrder();

        return newOrder;
    }

    @PUT
    @Path("{orderID}/pay")
    @Produces("application/json")
    public Response payOrder(
        @PathParam("orderID") String orderID,
        Order newOrder
    ) throws Exception {
        Order order = Order.GetOrder(orderID);
        order.updateOrder(newOrder.isPaid);

        return Response.ok().build();
    }

    @DELETE
    @Path("{orderID}")
    @Produces("application/json")
    public Response deleteOrder(@PathParam("orderID") String orderID) throws Exception 
    {
        Order order = Order.GetOrder(orderID);
        order.CancelOrder();

        return Response.ok().build();
    }
}
