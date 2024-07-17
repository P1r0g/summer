import java.sql.*;

public class Main {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost/";
    private static final String DATABASE_NAME = "summerPractick";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost/summerPractick";
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "Pirog2005";

    public static void main(String[] args) {
        checkDriver();
        checkDB();
        System.out.println("Все проверки пройдены)" + '\n');

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {
            getFullNameWaiters(connection);
            System.out.println();
            getWaiterName(connection);
            System.out.println();
            getThreeBiggestOrder(connection);
            System.out.println();
//            addNewWaiter(connection, "Женька", "Гречишный", 11000, 1);
//            System.out.println();
//            deleteWaiter(connection, 1);
//            System.out.println();
//            updateGradeForWaiter(connection);
//            System.out.println();
//            addNewTable(connection, 2, 1);
            System.out.println();
            getAllVistor(connection);
            System.out.println();
//            addVisistor(connection, "никита", 3);
            System.out.println();
            deleteTable(connection, 1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getFullNameWaiters(Connection connection) throws SQLException {
        // значения ячеек
        String param0 = null;

        Statement statement = connection.createStatement();                 // создаем оператор для простого запроса (без параметров)
        ResultSet rs = statement.executeQuery("SELECT waiter.first_name || ' ' || waiter.second_name \n " +
                "AS \"Full Name\" FROM waiter;");  // выполняем запроса на поиск и получаем список ответов
        System.out.println("Официанты:");
        while (rs.next()) {  // пока есть данные
            param0 = rs.getString(1); // значение ячейки, можно также получить по порядковому номеру (начиная с 1)
            System.out.println(param0);
        }
    }

    private static void getWaiterName(Connection connection) throws SQLException {
        String param0 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT waiter.first_name FROM waiter \n"
                + "WHERE payment BETWEEN 10000 AND 30000;");
        System.out.println("Слабачки:");
        while (rs.next()) {
            param0 = rs.getString(1);
            System.out.println(param0);
        }
    }

    private static void getThreeBiggestOrder(Connection connection) throws SQLException {
        String param0 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT food_names FROM \"order\"\n" +
                "ORDER BY price DESC;");
        System.out.println("Наибольшие счета: ");
        for (int i = 1; i <= 3; i++) {
            rs.next();
            param0 = rs.getString(1);
            System.out.println(param0);
        }
    }

    private static void addNewWaiter(Connection connection, String name, String secondName, int payment, int grade) throws SQLException {
        if (name == null || name.isBlank() || secondName == null || secondName.isBlank() || payment < 0 || grade < 0)
            return;

        PreparedStatement statement = connection.prepareStatement("INSERT INTO waiter(first_name, second_name, payment, grade)\n" +
                "VALUES (?, ?, ?, ?) returning id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, secondName);
        statement.setInt(3, payment);
        statement.setInt(4, grade);

        int count = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("ДОБАВЛЕН " + count + " НОВЫЙ ОФИЦИАНТ)))))");
    }

    private static void deleteWaiter(Connection connection, int id) throws SQLException {
        if (id <= 0) return;

        PreparedStatement statement = connection.prepareStatement("DELETE FROM waiter\n" +
                "WHERE id = ?;");
        statement.setInt(1, id);
        int count = statement.executeUpdate();
        System.out.println("УВОЛЕН " + count + " СОТРУДИК");
    }

    private static void updateGradeForWaiter(Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("UPDATE waiter SET grade=grade+1;");
        int count = statement.executeUpdate();  // выполняем запрос на коррекцию и возвращаем количество измененных строк
        System.out.println("Изменен " + count + " waiter");
    }

    private static void addNewTable(Connection connection, int waiterId, int visitorId) throws SQLException {
        if (waiterId <= 0 || visitorId <= 0) return;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"table\"(id_waiter, id_visitor)\n" +
                "VALUES (?, ?) returning id;", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, waiterId);
        statement.setInt(2, visitorId);
        statement.executeUpdate();
        statement.getGeneratedKeys();
        System.out.println("Добавлен новый стол");
    }

    private static void getAllVistor(Connection connection) throws SQLException {
        String param0 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT visitor.name FROM visitor;");
        System.out.println("Гости:");
        while (rs.next()) {
            param0 = rs.getString(1);
            System.out.println(param0);
        }
    }

    private static void addVisistor(Connection connection, String name, int orderId) throws SQLException {
        if (name == null || name.isBlank() || orderId <= 0) return;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO visitor(name, id_order)\n" +
                "VALUES (?, ?) returning id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setInt(2, orderId);
        statement.executeUpdate();
        statement.getGeneratedKeys();
        System.out.println("Добавлен новый посетитель");
    }

    private static void deleteTable(Connection connection, int id) throws  SQLException{
        if (id <= 0) return;

        PreparedStatement statement = connection.prepareStatement("DELETE FROM \"table\"\n" +
                "WHERE id = ?;");
        statement.setInt(1, id);
        int count = statement.executeUpdate();
        System.out.println("удален " + count + " столик");
    }

    public static void checkDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }


    public static void checkDB() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
        } catch (SQLException e) {
            System.out.println("Нет базы данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }
}