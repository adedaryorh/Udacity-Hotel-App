package api;

import java.util.*;
import java.util.Collection;
import model.room.IRoom;
import model.reservation.Reservation;
import model.customer.Customer;
import service.forcustomer.CustomerService;
import service.forReservation.ReservationService;

public class HotelResource {
    private static final HotelResource SINGLETON = new HotelResource();
    private final CustomerService customerService =  CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    private HotelResource(){ }

    public static HotelResource getSingleton(){return SINGLETON;}
    public Customer getCustomer(String email) {return customerService.getCustomer(email);}

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }
    public IRoom getRoom(String roomNumber){ return reservationService.getARoom(roomNumber);}
    public Reservation bookNewRoom(String customerEmail, IRoom room, Date checkedInDate, Date checkedOutDate){
        return reservationService.reserveARoom(getCustomer(customerEmail), room, checkedInDate, checkedOutDate);
    }
    public Collection<Reservation> getCustomersReservation(String customerEmail) {
        final Customer customer = getCustomer(customerEmail);

        if (customer == null) {
            return Collections.emptyList();
        }
        return reservationService.getCustomersReservation(getCustomer(customerEmail));
    }
    public Collection<IRoom> findARoom(final Date checkedIn, final Date checkedOut) {
        return reservationService.findRooms(checkedIn,checkedOut);
    }
    public Collection<IRoom> findAlternativeRooms(final Date checkedIn, final Date checkedOut) {
        return reservationService.findAlternativeRooms(checkedIn, checkedOut);
    }
    public Date addDefaultPlusDays(final Date date){return reservationService.addDefaultDays(date);}
}
