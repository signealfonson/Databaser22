import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final Scanner s = new Scanner(System.in);

    private static Connection connect(){
        String url = "jdbc:sqlite:/Users/signe/labb3.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
       mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            switch (showMainMenuAndGetUserInput()) {
                case "1" -> selectAll();
                case "2" -> addDog();
                case "3" -> updateDog();
                case "4" -> removeDog();
                case "5" -> countDogs();
                case "E", "e" -> System.exit(0);
                default -> incorrectInput();
            }
        }
    }
    private static String showMainMenuAndGetUserInput() {
        System.out.println("Signes hundregister");
        System.out.println("--------------");
        System.out.println("1. Visa hundregister");
        System.out.println("2. Lägg till en ny hund");
        System.out.println("3. Uppdatera en hunds uppgifter");
        System.out.println("4. Ta bort en hund");
        System.out.println("5. Se antal hundar");

        return s.nextLine();
    }

    private static void selectAll() {
        String sql = "SELECT * FROM hund";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("chipnummer") +  "\t" +
                        rs.getString("hundnamn") + "\t" +
                        rs.getString("hundagare") + "\t" +
                        rs.getString("hundras"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void addDog() {
        System.out.println("Vad har hunden för chipnummer? ");
        int inputChipnummer = s.nextInt();
        s.nextLine();
        System.out.println("Vad heter hunden? ");
        String inputNamn = s.nextLine();
        System.out.println("Vem äger hunden? ");
        String inputAgare = s.nextLine();
        System.out.println("Vilken ras är hunden? ");
        String inputRas = s.nextLine();
        insert(inputChipnummer, inputNamn, inputAgare, inputRas);
    }
    private static void insert(int chipnummer, String namn, String agare, String ras) {
        String sql = "INSERT INTO hund(chipnummer, hundnamn, hundagare, hundras) VALUES(?,?,?,?)";

        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chipnummer);
            pstmt.setString(2, namn);
            pstmt.setString(3, agare);
            pstmt.setString(4, ras);

            pstmt.executeUpdate();
            System.out.println("Ny hund tillagd");
            mainMenu();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateDog(){
        System.out.println("Vad har hunden för chipnummer? ");
        int inputChipnummer = s.nextInt();
        s.nextLine();
        System.out.println("Vad heter hunden? ");
        String inputNamn = s.nextLine();
        System.out.println("Vem äger hunden? ");
        String inputAgare = s.nextLine();
        System.out.println("Vilken ras är hunden? ");
        String inputRas = s.nextLine();
        update(inputChipnummer,inputNamn, inputAgare, inputRas);
    }
    private static void update(int chipnummer, String namn, String agare, String ras) {


        String sql = "UPDATE hund SET hundnamn = ? , "
                + "hundagare = ? , "
                + "hundras = ? "
                + "WHERE chipnummer = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, namn);
            pstmt.setString(2, agare);
            pstmt.setString(3, ras);
            pstmt.setInt(4, chipnummer);
            pstmt.executeUpdate();
            System.out.println("Uppgifterna är nu uppdaterade");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void removeDog(){
        System.out.println("Skriv in chipnumret på hunden som ska tas bort: ");
        int inputChipnummer = s.nextInt();
        deleteDog(inputChipnummer);
        s.nextLine();
    }

    private static void deleteDog(int chipnummer) {
        String sql = "DELETE FROM hund WHERE chipnummer = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, chipnummer);
            pstmt.executeUpdate();
            System.out.println("Hund borttagen ur registret");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void countDogs() {
        String sql = "SELECT COUNT(*) FROM hund";

        try (Connection conn = connect();
             Statement query = conn.createStatement()) {
            ResultSet rs = query.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Nuvarande antal hundar i registret: " + rs.getInt("COUNT(*)"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void incorrectInput() {
        System.out.println("Incorrect input, please try again!");
    }
}