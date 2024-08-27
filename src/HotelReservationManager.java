import java.sql.*;
import java.util.Scanner;

public class HotelReservationManager {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Pranjal#23";

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in)){
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");

                System.out.println("1. Get a Reservation");
                System.out.println("2. View Reservations");
                System.out.println("3. Update Reservation");
                System.out.println("4. Delete Reservation");
                System.out.println("0. Exit");
                int choice;
                System.out.print("Enter your choice - ");
                choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        makeReservation(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection, scanner);
                        break;
                    case 3:
                        updateReservation(connection, scanner);
                        break;
                    case 4:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Enter valid choice.");
                        break;
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void makeReservation(Connection con, Scanner scanner){
        System.out.print("Enter Guest name - ");
        String name = scanner.next();
        System.out.print("Enter Table number - ");
        int table_no = scanner.nextInt();
        System.out.print("Enter contact number - ");
        String contact_no = scanner.next();
        String query = "INSERT INTO reservation(name, table_number, contact_no) VALUES ('"+ name +"','" + table_no +"', '" + contact_no +"')";

        try(Statement statement = con.createStatement()){
            int affectedRows = statement.executeUpdate(query);
            if(affectedRows > 0) {
                System.out.println("Reservation added successfully");
            } else {
                System.out.println("Reservation failed");
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewReservations(Connection con, Scanner scanner){
        String query = "SELECT * FROM reservation";
        try(Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            while(rs.next()){
                System.out.println();
                System.out.println("ID - " + rs.getInt("id"));
                System.out.println("GUEST NAME - " + rs.getString("name"));
                System.out.println("TABLE NO. - " + rs.getInt("table_number"));
                System.out.println("CONTACT NO. - " + rs.getString("contact_no"));
                System.out.println("DATE/TIME - " + rs.getString("date"));
                System.out.println("*****************************************************");
            }
            System.out.println("No Reservations!!");
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection con, Scanner scanner){
        System.out.print("Enter id to delete - ");
        int id = scanner.nextInt();
        if(reservationExits(con, id)){
            System.out.println("Reservation do not exist.");
            return;
        }
        String query = "DELETE FROM reservation WHERE id = " + id + ";";
        try(Statement statement = con.createStatement()){
            int affectedRows = statement.executeUpdate(query);
            if(affectedRows > 0){
                System.out.println("Reservation deleted successfully");
            } else {
                System.out.println("Deletion failed");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    private static void updateReservation(Connection con, Scanner scanner){
        try(Statement statement = con.createStatement()){
            System.out.print("Enter reservation id - ");
            int id = scanner.nextInt();
            String query = "";
            if(reservationExits(con, id)){
                System.out.println("Reservation Id do not exist.");
                return;
            }
            System.out.println("What do you want to update - ");
            System.out.println("1. Name");
            System.out.println("2. Contact no.");
            System.out.println("3. Table no.");
            System.out.println("0. Exit");
            System.out.println("Enter your choice - ");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    System.out.print("Enter new name - ");
                    String name = scanner.next();
                    query = "UPDATE reservation SET name =  '" + name + "' WHERE id = " + id;
                    break;
                case 2:
                    System.out.print("Enter new contact no - ");
                    String contact_no = scanner.next();
                    query = "UPDATE reservation SET contact_no ='" + contact_no + "' WHERE id = " + id;
                    break;
                case 3:
                    System.out.print("Enter new table no - ");
                    int table_no = scanner.nextInt();
                    query = "UPDATE reservation SET table_number = " + table_no + " WHERE id = " + id + ";";
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
            int affectedRows = statement.executeUpdate(query);
            if(affectedRows > 0){
                System.out.println("Data updated successfully");
            } else {
                System.out.println("Data updation failed");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExits(Connection con, int id){
        String query = "SELECT * FROM reservation WHERE id = " + id;
        try(Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            return rs.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return true;
        }
    }

    private static void exit(){
        System.out.print("Exiting");
        int i = 3;
        while(i != 0){
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.print(".");
            i--;
        }
        System.out.println("\nThanks for using!");
    }
}