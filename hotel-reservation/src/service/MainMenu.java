package service;

import java.util.*;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import api.HotelResource;
import model.reservation.Reservation;
import  model.room.IRoom;

public class MainMenu {
    private static final String DEFAULT_DATE_FORMAT = "MM/DD/YY";
    private static final HotelResource hotelResource = HotelResource.getSingleton();
    public static void mainMenu() {
        String line = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try {
            do {
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1':
                            findAndReserveARoom();
                            break;
                        case '2':
                            ViewMyReservation();
                        case '3':
                            createAnAccount();
                        case '4':
                            AdminMenu.adminMenu();
                        case '5':
                            System.out.println("Exit");
                        default:
                            System.out.println("Unknown action");
                            break;
                    }
                } else {
                    System.out.println("Error: Invalid action");
                }
            } while (line.charAt(0) != '5' || line.length() != 1 );
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("NO valid input received. Exiting the program.");
        }
    }

    public static void printMainMenu(){
        System.out.println(".......................................\n" +
                ".....................WELCOME.......................\n" +
                "................This is ADEDAYO's Hotel..............\n"+
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an Account\n" +
                "4. Admin Page\n" +
                "5. Exit\n" +
                "Please select an option to proceed: \n");

    }
    private static Date getInputDate(final Scanner scanner) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(scanner.nextLine());
        }catch (ParseException ex) {
            System.out.println("Error: Invalid date");
            findAndReserveARoom();
        }
        return null;
    }
    private static void findAndReserveARoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter CheckedInDate in format mm/dd/yy '03/02/2023'");
        Date checkedInDate = getInputDate(scanner);

        System.out.println("Enter CheckedOutDate in format mm/dd/yy '03/07/2023'");
        Date checkedOutDate = getInputDate(scanner);

        if (checkedInDate != null && checkedOutDate != null) {
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkedInDate,checkedOutDate);

            if (availableRooms.isEmpty()){
                Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkedInDate,checkedOutDate);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("No rooms found");
                } else {
                    final Date alternativecheckedInDate = hotelResource.addDefaultPlusDays(checkedInDate);
                    final Date alternativecheckedOutDate = hotelResource.addDefaultPlusDays(checkedInDate);
                    System.out.println("But we have alternative options for your Suit: " +
                            "\n......Find below the available options...." +
                            "\nChecked In Date: " + alternativecheckedInDate +
                            "\nChecked Out Date: " + alternativecheckedOutDate);
                    printRooms(alternativeRooms);
                    reserveRoom(scanner, alternativecheckedInDate, alternativecheckedOutDate, alternativeRooms);
                }
            } else {
                printRooms(availableRooms);
                reserveRoom(scanner, checkedInDate, checkedOutDate, availableRooms);
            }
        }
    }

    private static void printRooms(final Collection<IRoom> rooms){
        if (rooms.isEmpty()){
            System.out.println("There's nil room available");
        }
    }

    private static void reserveRoom(final Scanner scanner, final Date checkedInDate, final Date checkedOutDate, final Collection<IRoom> rooms ){
        System.out.println("Choose if you wanna book a room Y/N");
        final Scanner sc = new Scanner(System.in);
        final String bookNewRoom = scanner.nextLine();
        //Y= YES While N= No

        if ("Y".equals(bookNewRoom)){
            System.out.println("Do you have a previous account? Y/N");
            final String haveAccount = sc.nextLine();

            if ("Y".equals(haveAccount)){
                System.out.println("Enter your Email Address");
                final String customerEmail = sc.nextLine();

                if ((hotelResource.getCustomer(customerEmail) == null)){
                    System.out.println("Customer did not exist, You can choose to create an Account");
               }else {
                    System.out.println("what room number do u wanna reserve?");
                    final String roomNumber = sc.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        final IRoom room = hotelResource.getRoom(roomNumber);
                        final Reservation reservation = hotelResource.bookNewRoom(customerEmail, room, checkedInDate, checkedOutDate);
                        System.out.println("Reservation created");
                    }else {
                        System.out.println("Sorry: This room is contained. ");
                    }
                }
                printMainMenu();
            } else  {
                System.out.println("Kindly create account for ease of available room notifications");
                printMainMenu();
            }
        } else if ("N".equals(bookNewRoom)) {
            System.out.println("Do have a nice day");
            printMainMenu();
        } else {
            reserveRoom(scanner, checkedInDate, checkedOutDate, rooms );
        }
    }

    private static void ViewMyReservation(){
        final Scanner scanner = new Scanner (System.in);

        System.out.println("Supply your Email: name@gmail.com");
        final String customerEmail = scanner.nextLine();

        printReservation(hotelResource.getCustomersReservation(customerEmail));
    }

    private static void printReservation(final Collection<Reservation> reservations){
        if (reservations == null  || reservations.isEmpty()){
            System.out.println("Empty Reservation record");
        } else {
            reservations.forEach(reservation -> System.out.println("Please find your" + reservation));
        }
    }

    private static void createAnAccount(){
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Supply Email Address");
        final String email = scanner.nextLine();

        System.out.println("Supply firstName");
        final String firstName = scanner.nextLine();

        System.out.println("Supply lastName");
        final String lastName = scanner.nextLine();

        try{
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account successfully created");

            printMainMenu();
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
            createAnAccount();
        }
    }


}
