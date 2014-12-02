package ligueBaseball.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.Logger;
import ligueBaseball.Logger.LOG_TYPE;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrieveNextKeyFromSequenceException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.exceptions.TeamIsNotEmptyException;

@XmlRootElement
public class Team extends DatabaseEntity
{
    String name;
    int fieldId = -1;

    // We use the cache feature because we often use those data and very rarely update them.
    private static Map<Integer, Team> cache = new HashMap<>();

    /**
     * Get all the teams.
     *
     * @param databaseConnection - Connection with database
     * @return List - All the teams.
     */
    public static List<Team> getAllTeams()
    {
        List<Team> teamList = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT equipeid FROM equipe;");
            ResultSet teams = statement.executeQuery();
            while (teams.next()) {
                teamList.add(getTeamWithId(teams.getInt("equipeid")));
                // teamList.add(getEntityFromResultSet(teams));
            }
        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
        } finally {
            closeStatement(statement);
        }
        return teamList;
    }

    /**
     * Get the team with the given ID.
     *
     * @param databaseConnection - Connection with database
     * @param id - ID of the team we want to retrieve.
     * @return Team - If found, return entity, otherwise return null.
     */
    public static Team getTeamWithId(int id)
    {
        // In case we cached it, use it
        if (cache.get(id) != null) {
            return cache.get(id);
        }

        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM equipe WHERE equipeid = ?;");
            statement.setInt(1, id);

            ResultSet teamResult = statement.executeQuery();
            if (!teamResult.next()) {
                return null;
            }
            return getEntityFromResultSet(teamResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the team with the given name.
     *
     * @param databaseConnection - Connection with database
     * @param name - Name of the team we want to retrieve.
     * @return Team - If found, return entity, otherwise return null.
     */
    public static Team getTeamWithName(String name)
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM equipe WHERE equipenom = ?;");
            statement.setString(1, name);

            ResultSet teamResult = statement.executeQuery();
            if (!teamResult.next()) {
                return null;
            }
            return getEntityFromResultSet(teamResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    protected static Team getEntityFromResultSet(ResultSet teamResultSet) throws SQLException
    {
        Team entity = new Team();
        entity.id = teamResultSet.getInt("equipeid");
        entity.name = teamResultSet.getString("equipenom");
        entity.fieldId = teamResultSet.getInt("terrainid");

        // Cache it
        cache.put(entity.id, entity);
        return entity;
    }

    @Override
    protected void create() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            id = getNextIdForTable("equipe", "equipeid");
            statement = databaseConnection.prepareStatement("INSERT INTO equipe (equipeid, equipenom, terrainid) VALUES(?, ?, ?);");
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, fieldId);
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
            // Add to cache
            cache.put(id, this);
            closeStatement(statement);
        }
    }

    @Override
    protected void update() throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("UPDATE equipe SET equipenom = ?, terrainid = ? WHERE equipeid = ?;");
            statement.setString(1, name);
            statement.setInt(2, fieldId);
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
            // Update cache
            cache.put(this.id, this);
            closeStatement(statement);
        }
    }

    @Override
    public void delete() throws FailedToDeleteEntityException, TeamIsNotEmptyException
    {
        try {
            if (!getPlayers().isEmpty()) {
                throw new TeamIsNotEmptyException(name);
            }
        } catch (FailedToRetrievePlayersOfTeamException e) {
            throw new FailedToDeleteEntityException("Impossible d'aller chercher la liste des joueurs.", e);
        }

        if (id >= 0) {
            PreparedStatement statement = null;
            try {
                // Delete equipe
                statement = databaseConnection.prepareStatement("DELETE FROM equipe WHERE equipeid = ?;");
                statement.setInt(1, id);
                statement.executeUpdate();
                databaseConnection.commit();

            } catch (SQLException e) {
                try {
                    databaseConnection.rollback();
                } catch (SQLException e1) {
                    Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
                }
                throw new FailedToDeleteEntityException(e);
            } finally {
                // Remove from cache
                cache.remove(id);
                closeStatement(statement);
            }
        }
        id = -1;
        name = null;
        fieldId = -1;
    }

    /**
     * Get the name of the team.
     *
     * @return name - Team name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the team.
     *
     * @param name - Team name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get all players for current team.
     *
     * @param databaseConnection - Connection with database
     * @return List - All the players.
     * @throws FailedToRetrievePlayersOfTeamException Failed to retrieve players of team.
     */
    public List<Player> getPlayers() throws FailedToRetrievePlayersOfTeamException
    {
        List<Player> players = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("SELECT joueurid FROM faitpartie WHERE equipeid = ? AND datefin IS NULL;");
            statement.setInt(1, id);

            ResultSet playersResultSet = statement.executeQuery();
            if (playersResultSet.isBeforeFirst()) {
                // Get all the players
                while (playersResultSet.next()) {
                    players.add(Player.getPlayerWithId(playersResultSet.getInt("joueurid")));
                }
            }

        } catch (SQLException e) {
            throw new FailedToRetrievePlayersOfTeamException(name, e);

        } finally {
            closeStatement(statement);
        }

        return players;
    }

    /**
     * Add a new player in this team.
     *
     * @param databaseConnection - Connection with database
     * @param player - Player to add to team.
     * @throws FailedToSaveEntityException Failed to save entity.
     */
    public void addPlayer(Player player) throws FailedToSaveEntityException
    {
        if (player.id < 0) {
            player.save();
        }
        // Insert
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("INSERT INTO faitpartie (joueurid, equipeid, numero, datedebut) VALUES(?, ?, ?, ?);");
            statement.setInt(1, player.getId());
            statement.setInt(2, id);
            statement.setInt(3, player.getNumber());

            if (player.getBeginningDate() != null) {
                statement.setDate(4, player.getBeginningDate());
            } else {
                statement.setDate(4, new Date(Calendar.getInstance().getTime().getTime()));
            }

            statement.execute();
            databaseConnection.commit();

        } catch (SQLException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            e.printStackTrace();
            // Nothing because it should be because it was already there.

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Remove a player from this team.
     *
     * @param databaseConnection - Connection with database
     * @param player - Player to remove from team.
     */
    public void removePlayer(Player player)
    {
        if (player.id >= 0) {
            PreparedStatement statement = null;
            try {
                statement = databaseConnection.prepareStatement("UPDATE faitpartie SET datefin = ? WHERE joueurid = ? AND equipeid = ?;");
                statement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
                statement.setInt(2, player.getId());
                statement.setInt(3, id);

                statement.executeUpdate();
                databaseConnection.commit();

            } catch (SQLException e) {
                try {
                    databaseConnection.rollback();
                } catch (SQLException e1) {
                    Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
                }
                // Nothing
            } finally {
                closeStatement(statement);
            }
        }
    }

    /**
     * Get the field related to this team.
     *
     * @param databaseConnection - Connection with database
     * @return Field - Entity if found, null otherwise.
     */
    public Field getField()
    {
        if (fieldId < 0) {
            return null;
        }
        return Field.getFieldWithId(fieldId);
    }

    /**
     * Set the field related to this team.
     *
     * @param databaseConnection - Connection with database
     * @param field - Field to associate with this team.
     * @throws FailedToSaveEntityException Failed to save entity.
     */
    public void setField(Field field) throws FailedToSaveEntityException
    {
        if (field != null) {
            if (field.id < 0) { // Field haven't been created yet
                field.save();
            }
            fieldId = field.id;
        }
    }
}
