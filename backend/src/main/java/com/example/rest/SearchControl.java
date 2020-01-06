package com.example.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class SearchControl {
    public static HashMap<String, Integer> ShowAvailableRooms(String hotel_id, String startDate, String endDate) throws Exception {
        String[] roomType = {"Single", "Double", "Quad"};
        Hotel hotel = Hotel.GetHotel(hotel_id);
        HashMap<String, Integer> availableRooms = new HashMap<String, Integer>();

        for(String type: roomType) {
            ArrayList<Room> rooms = hotel.GetAvailableRooms(startDate, endDate, type);
            availableRooms.put(type.toLowerCase() + "RoomNum", rooms.size());
        }

        return availableRooms;
    }

    public static ArrayList<Hotel> ShowHotels(String region) throws Exception {
        ArrayList<Hotel> hotels = Hotel.GetHotels(region);
        return hotels;
    }

    static Order addOrder(String startDate, String endDate, String customerName, String hotelID, HashMap<String, Integer> requirement) throws Exception {
        Hotel hotel = Hotel.GetHotel(hotelID);
        ArrayList<Room> availRooms = new ArrayList<Room>();
        boolean isAvailable = true;

        // check if the required rooms are available
        for(Map.Entry<String, Integer> entry : requirement.entrySet()) {
            ArrayList<Room> rooms = hotel.GetAvailableRooms(startDate, endDate, entry.getKey());
            if(rooms.size() < entry.getValue()) {
                isAvailable = false;
                break;
            }
            else {
                for(int i = 0;i < entry.getValue();i++) {
                    availRooms.add(rooms.get(i));
                }
            }

            // if one of the available number of rooms is less than the requirement, move to the next hotel
        }
        if(isAvailable == true) {
            Order order = new Order(startDate, endDate, customerName, hotelID, availRooms);
            System.out.println(order.GetTotalPrice());
            return order;
        }
        else {
            return new Order();
        }
        
    }

}