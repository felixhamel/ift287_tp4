package ligueBaseball.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ligueBaseball.Logger;
import ligueBaseball.Logger.LOG_TYPE;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrieveNextKeyFromSequenceException;
import ligueBaseball.exceptions.FailedToSaveEntityException;

import com.google.common.base.MoreObjects;

public class Player extends DatabaseEntity
{
    String lastName;
    String firstName;
    int number = -1;
    int teamId = -1;
    Date beginDate;
    Date endDate;

    /**
     * Get the player with the given ID.
     *
     * @param databaseConnection - Connection with database
     * @param id - ID of the player.
     * @return Player - If found, otherwise return null.
     */
    public static List<Player> getAllPlayers()
    {
        PreparedStatement statement = null;
        List<Player> players = new ArrayList<Player>();

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM joueur LEFT OUTER JOIN faitpartie ON faitpartie.joueurid = joueur.joueurid;");

            ResultSet fieldResult = statement.executeQuery();
            while (fieldResult.next()) {
                players.add(createFieldFromResultSet(fieldResult));
            }
            return players;

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the player with the given ID.
     *
     * @param databaseConnection - Connection with database
     * @param id - ID of the player.
     * @return Player - If found, otherwise return null.
     */
    public static Player getPlayerWithId(int id)
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM joueur LEFT OUTER JOIN faitpartie ON faitpartie.joueurid = joueur.joueurid AND faitpartie.datefin IS NULL WHERE joueur.joueurid = ?;");
            statement.setInt(1, id);

            ResultSet fieldResult = statement.executeQuery();
            if (fieldResult.next()) {
                return createFieldFromResultSet(fieldResult);
            }

            Logger.error(LOG_TYPE.USER, "Impossible de trouver un joueur avec l'ID %s.", id);
            return null;

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            e.printStackTrace();
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get Player with firstname and lastname.
     *
     * @param databaseConnection - Connection with database
     * @param firstName - First name of the player.
     * @param lastName - Last name of the player.
     * @return Player - If found, otherwise return null.
     */
    public static List<Player> getPlayerWithName(String firstName, String lastName)
    {
        List<Player> players = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM joueur LEFT OUTER JOIN faitpartie ON faitpartie.joueurid = joueur.joueurid AND faitpartie.datefin IS NULL WHERE joueur.joueurprenom = ? AND joueur.joueurnom = ?;");
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet fieldResult = statement.executeQuery();
            while (fieldResult.next()) {
                players.add(createFieldFromResultSet(fieldResult));
            }

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());

        } finally {
            closeStatement(statement);
        }

        return players;
    }

    private static Player createFieldFromResultSet(ResultSet resultSet) throws SQLException
    {
        Player player = new Player();

        player.id = resultSet.getInt("joueurid");
        player.firstName = resultSet.getString("joueurprenom");
        player.lastName = resultSet.getString("joueurnom");
        player.number = resultSet.getInt("numero");
        player.teamId = resultSet.getInt("equipeid");
        player.beginDate = resultSet.getDate("datedebut");

        return player;
    }

    @Override
    protected void create() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            id = getNextIdForTable("joueur", "joueurid");
            statement = databaseConnection.prepareStatement("INSERT INTO joueur (joueurid, joueurnom, joueurprenom) VALUES(?, ?, ?);");
            statement.setInt(1, id);
            statement.setString(2, lastName);
            statement.setString(3, firstName);
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
    }

    @Override
    protected void update() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("UPDATE joueur SET joueurnom = ?, joueurprenom = ? WHERE joueurid = ?;");
            statement.setString(1, lastName);
            statement.setString(2, firstName);
            statement.setInt(3, id);
            statement.executeUpdate();

            databaseConnection.commit();

        } catch (SQLException e) {
            try {
                Logger.info(LOG_TYPE.DEBUG, "ERROR");
                e.printStackTrace();
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public void delete() throws FailedToDeleteEntityException, Exception
    {
        Team team = getTeam();
        if (team != null) {
            team.removePlayer(this);
        }

        // Delete from All faitpartie
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("DELETE FROM faitpartie WHERE joueurid = ?;");
            statement.setInt(1, id);
            statement.executeUpdate();

            databaseConnection.commit();
            System.out.println(statement);

        } catch (SQLException e) {
            try {
                Logger.info(LOG_TYPE.DEBUG, "ERROR");
                e.printStackTrace();
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }

        // Remove player
        try {
            statement = databaseConnection.prepareStatement("DELETE FROM joueur WHERE joueurid = ?;");
            statement.setInt(1, id);
            statement.executeUpdate();

            databaseConnection.commit();
            System.out.println(statement);

        } catch (SQLException e) {
            try {
                Logger.info(LOG_TYPE.DEBUG, "ERROR");
                e.printStackTrace();
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the team this player plays for.
     *
     * @param databaseConnection - Connection with database
     * @return Team - Get the current team for the player.
     */
    public Team getTeam()
    {
        return Team.getTeamWithId(teamId);
    }

    /**
     * Set the team this player will play for.
     *
     * @param databaseConnection - Connection with database
     * @param team - New team for the player.
     * @throws FailedToSaveEntityException Failed to save entity.
     */
    public void setTeam(Team team) throws FailedToSaveEntityException
    {
        team.addPlayer(this);
    }

    /**
     * Get the last name of the player.
     *
     * @return String - Last name.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Set the last name of the player.
     *
     * @param lastName - Last name.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Get the first name of the player.
     *
     * @return String - First name.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Set the first name of the player.
     *
     * @param firstName - First name.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Get the shirt number.
     *
     * @return number - Shirt number.
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * Set the shirt number.
     *
     * @param number - Shirt number.
     */
    public void setNumber(int number)
    {
        this.number = number;
    }

    /**
     * Get the beginning date.
     *
     * @return - Beginning date.
     */
    public Date getBeginningDate()
    {
        return beginDate;
    }

    /**
     * Set the beginning date.
     *
     * @param date - Beginning date.
     */
    public void setBeginningDate(Date date)
    {
        this.beginDate = date;
    }

    /**
     * Get the beginning date.
     *
     * @return - Beginning date.
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     * Set the beginning date.
     *
     * @param date - Beginning date.
     */
    public void setEndDate(Date date)
    {
        this.endDate = date;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("firstName", firstName).add("lastName", lastName).add("number", number).toString();
    }
}
