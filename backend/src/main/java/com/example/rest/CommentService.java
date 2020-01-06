package com.example.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/comments")
public class CommentService {
    // add comment
    @POST
    @Path("/add")
    @Consumes("application/json")
    public Response addComment(Comment comment) throws Exception {
        System.out.println(comment.GetDescription());
        // Order order = Order.GetOrder(orderID);
        // order.AddComment(comment);
        
        return Response.ok().build();
    }
}
