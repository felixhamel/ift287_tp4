package ligueBaseball.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.Logger;
import ligueBaseball.Logger.LOG_TYPE;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrieveNextKeyFromSequenceException;
import ligueBaseball.exceptions.FailedToSaveEntityException;

@XmlRootElement
public class Official extends DatabaseEntity
{
    private String firstName;
    private String lastName;

    // We use the cache feature because we often use those data and very rarely update them.
    private static Map<Integer, Official> cache = new HashMap<>();

    /**
     * Get all the officials.
     *
     * @param databaseConnection - Connection with database
     * @return List - All the officials.
     */
    public static List<Official> getAllOfficials()
    {
        List<Official> officials = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM arbitre ORDER BY arbitreprenom ASC;");

            ResultSet officialResult = statement.executeQuery();
            while (officialResult.next()) {
                officials.add(getEntityFromResultSet(officialResult));
            }

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());

        } finally {
            closeStatement(statement);
        }

        return officials;
    }

    /**
     * Get the official with the given ID.
     *
     * @param databaseConnection - Connection with database
     * @param id - ID of the official.
     * @return Official - If found, otherwise return null.
     */
    public static Official getOfficialWithId(int id)
    {
        PreparedStatement statement = null;

        if (!cache.isEmpty() && cache.containsKey(id)) {
            return cache.get(id);
        }

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM arbitre WHERE arbitreid = ?;");
            statement.setInt(1, id);

            ResultSet officialResult = statement.executeQuery();
            if (!officialResult.next()) {
                return null;
            }
            cache.put(id, getEntityFromResultSet(officialResult));
            return cache.get(id);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the official with the given name.
     *
     * @param databaseConnection - Connection with database
     * @param firstName - First name of the official.
     * @param lastName - Last name of the official.
     * @return Official - If found, otherwise return null.
     */
    public static Official getOfficialWithName(Connection databaseConnection, String firstName, String lastName)
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM arbitre WHERE arbitreprenom = ? AND arbitrenom = ?;");
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet officialResult = statement.executeQuery();
            if (!officialResult.next()) {
                return null;
            }
            return getEntityFromResultSet(officialResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    protected static Official getEntityFromResultSet(ResultSet resultSet) throws SQLException
    {
        Official entity = new Official();
        entity.id = resultSet.getInt("arbitreid");
        entity.firstName = resultSet.getString("arbitreprenom");
        entity.lastName = resultSet.getString("arbitrenom");

        return entity;
    }

    @Override
    protected void create() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            id = getNextIdForTable("arbitre", "arbitreid");
            statement = databaseConnection.prepareStatement("INSERT INTO arbitre (arbitreid, arbitreprenom, arbitrenom) VALUES(?, ?, ?);");
            statement.setInt(1, id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.execute();
            databaseConnection.commit();

        } catch (SQLException | FailedToRetrieveNextKeyFromSequenceException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }

        // Add to cache
        cache.put(id, this);
    }

    @Override
    protected void update() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("UPDATE arbitre SET arbitreprenom = ?, arbitrenom = ? WHERE arbitreid = ?;");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, id);
            statement.executeUpdate();
            databaseConnection.commit();

        } catch (SQLException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }

        // Update cache
        cache.put(id, this);
    }

    @Override
    public void delete() throws FailedToDeleteEntityException, Exception
    {
        // Do nothing
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (firstName == null ? 0 : firstName.hashCode());
        result = prime * result + (lastName == null ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Official other = (Official) obj;
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        return true;
    }
}
