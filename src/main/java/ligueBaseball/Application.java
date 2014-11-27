package ligueBaseball;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import ligueBaseball.exceptions.FailedToConnectToDatabaseException;

public class Application
{
    public static Connection connection;

    static {
        try {
            connectToDatabase("university.optimaweb.co", "tp3", "tp", "Eb0laAttack");
        } catch (FailedToConnectToDatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to the database with the given informations.
     *
     * @param hostname
     * @param database
     * @param username
     * @param password
     * @return
     * @throws FailedToConnectToDatabaseException
     */
    public static boolean connectToDatabase(String hostname, String database, String username, String password) throws FailedToConnectToDatabaseException
    {
        try {
            String connectionString = "jdbc:postgresql://" + hostname + ":5432/" + database;
            Properties connectionParameters = new Properties();
            connectionParameters.setProperty("user", username);
            connectionParameters.setProperty("password", password);
            connection = DriverManager.getConnection(connectionString, connectionParameters);
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            throw new FailedToConnectToDatabaseException(database, e);
        }

        return true;
    }

    /**
     * Disconnect from the database.
     *
     * @return
     */
    public static boolean disconnectFromDatabase()
    {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            connection = null;
        }
    }

    public static void Main()
    {
        new Application();
    }
}
