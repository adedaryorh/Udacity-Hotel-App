package service;

import api.AdminResource;
import model.customer.Customer;
import model.room.enums.RoomType;
import model.room.Room;
import model.room.IRoom;
import model.room.Room;
import java.util.Scanner;

import java.util.*;
public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getSingleton();
    public  static void adminMenu(){
        String line = "";
        final Scanner scanner = new Scanner(System.in);

        printMenu();
        try {
            do {
                line = scanner.nextLine();
                if (line.length() == 1){
                    switch (line.charAt(0)){
                        case '1':
                            displayAllCustomers();
                            break;
                        case '2':
                            displayAllRooms();
                            break;
                        case '3':
                            showAllAvailableReservations();
                            break;
                        case '4':
                            addRoom();
                            break;
                        case '5':
                            MainMenu.printMainMenu();
                            break;
                        default:
                            System.out.println("Selection not Recognized");
                            break;
                    }
                } else {
                    System.out.println("Error: Invalid action");
                }
            } while (line.charAt(0) != '5' || line.length() != 1 );
        }  catch (StringIndexOutOfBoundsException ex){
            System.out.println("NO input received. Exiting the program.");
        }
    }
    private static void printMenu() {
        System.out.println("\t\nAdmin Menu\n" +
                "....................................\n" +
                "\n.....Welcome to the Admin Page.........\n" +
                "1. See all Customers\n" +
                "\t2. See all Rooms\n" +
                "\t3. See all Reservations\n" +
                "\t4. Add a Room\n" +
                "\t5. Back to Main Menu\n" +
                ".........................................\n" +
                "Kindly select an option to proceed\n: ");
    }
    private static void addRoom(){
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a room number: ");
        final String roomNumber = scanner.nextLine();

        System.out.println("Enter price per each night: ");
        final double roomPrice = enterRoomPrice(scanner);

        System.out.println("Enter the room type: 1 for single bed && 2 for a double bed: ");
        final RoomType roomType = enterRoomType(scanner);

        final Room room = new Room(roomNumber, roomPrice, roomType);
        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room reserved successfully");

        System.out.println("Would you like additional reservation Y/N");
        addAnotherRoom();
    }

    //Error handling: Input price format must be double
    private static double enterRoomPrice(final Scanner scanner){
       try {
           return Double.parseDouble(scanner.nextLine());
       } catch (NumberFormatException ex) {
           System.out.println("Invalid price supplied!! Please enter a double number. " +
                   "Decimals separated by . " + "$20.4");
           return enterRoomPrice(scanner);
       }
    }

    //Error handling: Input room type selection should be either single/double bed
    private static RoomType enterRoomType(final Scanner scanner) {
        try {
            return RoomType.valueOfLabel(scanner.nextLine());
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid Room type supplied!!, Please choose either single bed or double bed: ");
            return enterRoomType(scanner);
        }
    }

    private static void addAnotherRoom() {
        final Scanner scanner = new Scanner(System.in);
        //I should checkout this scanner method later  by accepting input with a character
    //    try {

           // char anotherRoom;
          //  char scanner = sc.next().charAt(0);
           // while ((scanner.charAt() != 'Y' && anotherRoom.next().charAt() != 'N')
              //      || anotherRoom.length() != 1) {
             //   System.out.println("please enter Y OR N");
                //anotherRoom = scanner.nextLine();
             //   anotherRoom = scanner.next().charAt(0);

        try {

            String anotherRoom;
            anotherRoom = scanner.nextLine();
            while ((anotherRoom.charAt(0) != 'Y' && anotherRoom.charAt(0) != 'N')
                    || anotherRoom.length() != 1) {
                System.out.println("please enter Y OR N");
                anotherRoom = scanner.nextLine();
            }
            if(anotherRoom.charAt(0) == 'Y') {
                addRoom();
            } else if (anotherRoom.charAt(0) == 'N') {
                printMenu();
            }else {
                addAnotherRoom();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Your input is out of required bound");
            addAnotherRoom();
        }
    }

    private static void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("No available room");
        } else {
            adminResource.getAllRooms().forEach(System.out::println);
        }
    }

    private  static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomer();
        if (customers.isEmpty()) {
            System.out.println("No customer record found");
        } else {
            adminResource.getAllCustomer().forEach(System.out::println);
        }
    }

    private static  void showAllAvailableReservations(){
        adminResource.showAllAvailableReservations();
    }

}
