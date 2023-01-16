package service.forReservation;

import java.util.*;
import java.util.stream.Collectors;

import model.reservation.Reservation;
import model.room.IRoom;
import model.customer.Customer;

public class ReservationService {
   public static final ReservationService SINGLETON = new ReservationService();
   //value declaration
   public static final int RECOMMENDED_ROOM_CHECK_DAYS_SPACE = 7;
   //mapping and hastMap tag for IRoom and Reservation.
   private final Map<String, IRoom> rooms = new HashMap<>();
   private final Map<String, Collection<Reservation>> reservation = new HashMap<>();
   private ReservationService() {}
   public static ReservationService getSingleton(){return SINGLETON;}
   public void addRoom(final IRoom room) {rooms.put(room.getRoomNumber(),room); }
   public IRoom getARoom(final String roomId){return rooms.get(roomId);}
   public  Collection<IRoom>getAllRooms(){return rooms.values();}

   //note a return type is required in this method.
   public Reservation reserveARoom(final Customer customer, final IRoom room, final Date checkedInDate,  final  Date checkedOutDate){
      final Reservation reservation = new Reservation(customer, room, checkedInDate, checkedOutDate);
      Collection<Reservation> customerReservation = getCustomersReservation(customer);

      if (customerReservation == null) {
         customerReservation = new LinkedList<>();
      }// else();
         //System.out.println("Check your reservation here");
      customerReservation.add(reservation);
      reservation.put(customer.getEmail(),customerReservation);

      System.out.println("This is your reservation details: " + reservation);
      return reservation;
   }

   public Collection<IRoom> findRooms(final Date checkedInDate, final Date checkedOutDate){
      return findAvailableRooms(checkedInDate,checkedOutDate);
   }
   public Collection<IRoom>findAlternativeRooms(final Date checkedInDate, final Date checkedOutDate){
      return findAlternativeRooms(addDefaultDays(checkedInDate), addDefaultDays(checkedOutDate));
   }
   private Collection<IRoom> findAvailableRooms(final Date checkedInDate, final Date checkedOutDate){
      final Collection<Reservation>allReservations = getAllReservations();
      final Collection<IRoom> nonAvailableRooms = new LinkedList<>();

      for (Reservation reservation : allReservations) {
         if (reservationOverlaps(reservation, checkedInDate, checkedOutDate)){
            nonAvailableRooms.add(reservation.getRoom());
         }
      }
      return rooms.values().stream().filter(room -> nonAvailableRooms.stream()
              .noneMatch(nonAvailableRoom -> nonAvailableRooms.equals(rooms)))
              .collect(Collectors.toList());
   }
   public Date addDefaultDays(final Date date){
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(Calendar.DATE, RECOMMENDED_ROOM_CHECK_DAYS_SPACE);
      return calendar.getTime();
   }
   private boolean reservationOverlaps(final Reservation reservation, final Date checkedInDate, final Date checkedOutDate) {

      return checkedInDate.before(reservation.getCheckOutDate())
              && checkedOutDate.after(reservation.getCheckInDate());
   }
   //retrieving a customer reservation
   public Collection<Reservation> getCustomersReservation(final Customer customer){
      return reservation.get(customer.getEmail());
   }
   //customer viewing their reservations
   public void printAllReservation(){
      final Collection<Reservation> reservations = getAllReservations();
         if (reservations.isEmpty()){
            System.out.println("You don't have any reservation yet");
         }else{
            for (Reservation reservation : reservations){
               System.out.println("----------Kindly find your active reservation---------");
               System.out.println(reservation + "\t\n");}
            }
         }
   private Collection<Reservation> getAllReservations() {
      final Collection<Reservation> allReservation = new LinkedList<>();
      for (Collection<Reservation>reservations : reservation.values()) {
         allReservation.addAll(reservations);
      }
      return  allReservation;
   }
}
