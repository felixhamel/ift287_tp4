package ligueBaseball.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.Logger;
import ligueBaseball.Logger.LOG_TYPE;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrieveNextKeyFromSequenceException;
import ligueBaseball.exceptions.FailedToSaveEntityException;

@XmlRootElement
public class Field extends DatabaseEntity
{
    String name;
    String address;

    // We use the cache feature because we often use those data and very rarely update them.
    private static Map<Integer, Field> cache = new HashMap<>();

    /**
     * Get the field with the given ID.
     *
     * @param databaseConnection - Connection with database
     * @param id - ID of the field.
     * @return Field - If found, otherwise return null.
     */
    public static Field getFieldWithId(int id)
    {
        // Check if we have it in cache
        if (!cache.isEmpty() && cache.containsKey(id)) {
            return cache.get(id);
        }

        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM terrain WHERE terrainid = ?;");
            statement.setInt(1, id);

            ResultSet fieldResult = statement.executeQuery();
            if (!fieldResult.next()) {
                return null;
            }
            cache.put(id, createFieldFromResultSet(fieldResult));
            return cache.get(id);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the field with the given name.
     *
     * @param databaseConnection - Connection with database
     * @param name - Name of the field.
     * @return Field - If found, otherwise return null.
     */
    public static Field getFieldWithName(String name)
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM terrain WHERE terrainnom = ?;");
            statement.setString(1, name);

            ResultSet fieldResult = statement.executeQuery();
            if (!fieldResult.next()) {
                return null;
            }
            return createFieldFromResultSet(fieldResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    private static Field createFieldFromResultSet(ResultSet resultSet) throws SQLException
    {
        Field field = new Field();
        field.id = resultSet.getInt("terrainid");
        field.name = resultSet.getString("terrainnom");
        field.address = resultSet.getString("terrainadresse");

        return field;
    }

    @Override
    protected void create() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            id = getNextIdForTable("terrain", "terrainid");
            statement = databaseConnection.prepareStatement("INSERT INTO terrain (terrainid, terrainnom, terrainadresse) VALUES(?, ?, ?);");
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.execute();
            databaseConnection.commit();

        } catch (SQLException | FailedToRetrieveNextKeyFromSequenceException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }

        // Add it to cache
        cache.put(id, this);
    }

    @Override
    protected void update() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("UPDATE terrain SET terrainnom = ?, terrainadresse = ? WHERE terrainid = ?;");
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setInt(3, id);
            statement.executeUpdate();
            databaseConnection.commit();

        } catch (SQLException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }

        // Update cache
        cache.put(id, this);
    }

    @Override
    public void delete() throws FailedToDeleteEntityException
    {
        if (id >= 0) {
            PreparedStatement statement = null;
            try {
                statement = databaseConnection.prepareStatement("DELETE FROM terrain WHERE terrainid = ?;");
                statement.setInt(1, id);
                statement.executeQuery();
                databaseConnection.commit();

            } catch (SQLException e) {
                try {
                    databaseConnection.rollback();
                } catch (SQLException e1) {
                    Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
                }
                throw new FailedToDeleteEntityException(e);
            } finally {
                closeStatement(statement);
            }
        }
        // Else the current field has never been created in the database before.
    }

    /**
     * Get the name of the field.
     *
     * @return String - Name of the field.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the field.
     *
     * @param name - Name of the field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the address of the field.
     *
     * @return String - Address of the field.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Set the address of the field.
     *
     * @param address - Address of the field.
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
}
