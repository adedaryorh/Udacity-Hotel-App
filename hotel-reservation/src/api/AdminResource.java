package api;

import java.util.*;
import java.util.Collection;
import model.room.IRoom;
import model.reservation.Reservation;
import model.customer.Customer;
import service.forcustomer.CustomerService;
import service.forReservation.ReservationService;
public class AdminResource {
    private static final AdminResource SINGLETON = new AdminResource();
    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    private AdminResource() { }
    public static AdminResource getSingleton() { return SINGLETON; }
    public  Customer getCustomer(String email){ return customerService.getCustomer(email);}
    public  void addRoom(List<IRoom> rooms) { rooms.forEach(reservationService::addRoom);}

    public Collection<IRoom> getAllRooms() { return reservationService.getAllRooms();}

    //public Collection<IRoom> getAllRooms() { (getAllRooms().forEach()) return reservationService.getAllRooms();}
    public Collection<Customer> getAllCustomer(){ return customerService.getAllCustomer();}
    public void showAllAvailableReservations(){ reservationService.printAllReservation();}
}
