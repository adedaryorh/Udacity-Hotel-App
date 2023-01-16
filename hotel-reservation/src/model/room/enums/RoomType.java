package model.room.enums;

public enum RoomType {
    SINGLE("1"),
    DOUBLE("2");

    public final String s;
   //constructor
    private RoomType(String s) { this.s = s;}
    public static RoomType valueOfLabel (String s){
        for (RoomType roomType : values()){
            if (roomType.s.equals(s)){
                return roomType;
            }
        }
        throw new  IllegalArgumentException();
    }
}
