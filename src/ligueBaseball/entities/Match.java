package ligueBaseball.entities;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ligueBaseball.Logger;
import ligueBaseball.Logger.LOG_TYPE;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrieveNextKeyFromSequenceException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.exceptions.TeamDoesntExistException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Match extends DatabaseEntity
{
    private int localTeamId;
    private int visitorTeamId;
    private int fieldId;
    private Date date;
    private Time time;
    private int localTeamScore = 0;
    private int visitorTeamScore = 0;

    /**
     * Get the match that match with the given ID.
     *
     * @param databaseConnection - Connection with database - Connection with database
     * @param id - ID of the match to find.
     * @return Match - If found, otherwise return null.
     */
    public static Match getMatchWithId(Connection databaseConnection, int id)
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM match WHERE matchid = ?;");
            statement.setInt(1, id);

            ResultSet matchResult = statement.executeQuery();
            if (!matchResult.next()) {
                return null;
            }
            return getEntityFromResultSet(matchResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get a match that occured at a specific time between two given teams.
     *
     * @param databaseConnection - Connection with database - Connection with database
     * @param date - Date of the match
     * @param time - Time of the match
     * @param equipelocal - Local team
     * @param equipevisiteur - Visitor team
     * @return Match - If found, otherwise return null.
     * @throws TeamDoesntExistException Team doesn't exists.
     */
    public static Match getMatchWithDateTimeEquipe(Connection databaseConnection, String date, String time, String equipelocal, String equipevisiteur) throws TeamDoesntExistException
    {
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM match WHERE equipelocal = ? AND equipevisiteur = ? AND matchdate = ? AND matchheure = ?");

            Team local = Team.getTeamWithName(databaseConnection, equipelocal);
            if (local == null) {
                throw new TeamDoesntExistException(equipelocal);
            }
            statement.setInt(1, local.getId());

            Team visitor = Team.getTeamWithName(databaseConnection, equipevisiteur);
            if (visitor == null) {
                throw new TeamDoesntExistException(equipevisiteur);
            }
            statement.setInt(2, visitor.getId());

            try {
                statement.setDate(3, Date.valueOf(date));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("La date est invalide.");
            }
            try {
                if (time.lastIndexOf(':') <= 2) {
                    time += ":00";
                }
                statement.setTime(4, Time.valueOf(time));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("L'heure est invalide.");
            }

            ResultSet matchResult = statement.executeQuery();
            if (!matchResult.next()) {
                return null;
            }
            return getEntityFromResultSet(matchResult);

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Extract entity from given ResultSet.
     *
     * @param resultSet - Result set
     * @return match - Created object from the result set.
     * @throws SQLException Exception during communication with the database.
     */
    private static Match getEntityFromResultSet(ResultSet resultSet) throws SQLException
    {
        Match match = new Match();
        match.id = resultSet.getInt("matchid");
        match.localTeamId = resultSet.getInt("equipelocal");
        match.visitorTeamId = resultSet.getInt("equipevisiteur");
        match.fieldId = resultSet.getInt("terrainid");
        match.date = resultSet.getDate("matchdate");
        match.time = resultSet.getTime("matchheure");
        match.localTeamScore = resultSet.getInt("pointslocal");
        match.visitorTeamScore = resultSet.getInt("pointsvisiteur");

        return match;
    }

    @Override
    protected void create(Connection databaseConnection) throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            id = getNextIdForTable(databaseConnection, "match", "matchid");
            statement = databaseConnection.prepareStatement("INSERT INTO match (matchid, equipelocal, equipevisiteur, terrainid, matchdate, matchheure, pointslocal, pointsvisiteur) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
            statement.setInt(1, id);
            statement.setInt(2, localTeamId);
            statement.setInt(3, visitorTeamId);
            statement.setInt(4, fieldId);
            statement.setDate(5, date);
            statement.setTime(6, time);
            statement.setInt(7, localTeamScore);
            statement.setInt(8, visitorTeamScore);
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
    protected void update(Connection databaseConnection) throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("UPDATE match SET equipelocal = ?, equipevisiteur = ?,terrainid = ?,matchdate = ?,matchheure = ?,pointslocal = ?,pointsvisiteur = ? WHERE matchid = ?;");
            statement.setInt(1, localTeamId);
            statement.setInt(2, visitorTeamId);
            statement.setInt(3, fieldId);
            statement.setDate(4, date);
            statement.setTime(5, time);
            statement.setInt(6, localTeamScore);
            statement.setInt(7, visitorTeamScore);
            statement.setInt(8, this.id);
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
    }

    /**
     * Get all match for a team
     *
     * @param databaseConnection - Connection with database - Connection with database
     * @param teamName - Name of the team
     * @return List of match
     * @throws TeamDoesntExistException Team doesn't exists.
     */
    public static List<Match> getMatchForTeam(Connection databaseConnection, String teamName) throws TeamDoesntExistException
    {
        List<Match> MatchTeam = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            Team team = Team.getTeamWithName(databaseConnection, teamName);
            if (team == null) {
                throw new TeamDoesntExistException(teamName);
            }
            int id = team.getId();

            statement = databaseConnection.prepareStatement("SELECT * FROM match WHERE equipelocal = ? " + "or equipevisiteur = ? " + "AND pointslocal NOTNULL " + "AND pointsvisiteur NOTNULL;");
            statement.setInt(1, id);
            statement.setInt(2, id);

            ResultSet MatchResultSet = statement.executeQuery();
            while (MatchResultSet.next()) {
                MatchTeam.add(getEntityFromResultSet(MatchResultSet));
            }

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
        return MatchTeam;
    }

    @Override
    public void delete(Connection databaseConnection) throws FailedToDeleteEntityException, Exception

    {
        throw new NotImplementedException();
    }

    /**
     * Get every match
     *
     * @param databaseConnection - Connection with database - Connection
     * @return list of all match
     */
    public static List<Match> getAllMatch(Connection databaseConnection)
    {
        List<Match> Match = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM match");
            ResultSet MatchResultSet = statement.executeQuery();
            while (MatchResultSet.next()) {
                Match.add(getEntityFromResultSet(MatchResultSet));
            }

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } finally {
            closeStatement(statement);
        }
        return Match;

    }

    /**
     * Get list of match from a date
     *
     * @param databaseConnection - Connection with database
     * @param date - Date of the match
     * @return list of match after the date
     */
    public static List<Match> getMatchWithDate(Connection databaseConnection, String date)
    {
        List<Match> MatchDate = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = databaseConnection.prepareStatement("SELECT * FROM match WHERE matchdate >= ?" + "AND pointslocal NOTNULL " + "AND pointsvisiteur NOTNULL;");
            statement.setDate(1, Date.valueOf(date));

            ResultSet MatchResultSet = statement.executeQuery();
            while (MatchResultSet.next()) {
                MatchDate.add(getEntityFromResultSet(MatchResultSet));
            }

        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
            return null;

        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("La date est invalide.");

        } finally {
            closeStatement(statement);
        }
        return MatchDate;
    }

    /**
     * Get the officials for this match, if any.
     *
     * @param databaseConnection - Connection with database
     * @return List of the officials that where there for the match.
     */

    public List<Official> getOfficials(Connection databaseConnection)
    {
        List<Official> officials = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("SELECT arbitreid FROM arbitrer WHERE matchid = ?;");
            statement.setInt(1, id);
            ResultSet officialResultSet = statement.executeQuery();
            while (officialResultSet.next()) {
                officials.add(Official.getOfficialWithId(databaseConnection, officialResultSet.getInt("arbitreid")));
            }
        } catch (SQLException e) {
            Logger.error(LOG_TYPE.EXCEPTION, e.getMessage());
        } finally {
            closeStatement(statement);
        }
        return officials;
    }

    /**
     * Add an official that was present at this match. Match MUST have been saved in the database before doing this.
     *
     * @param databaseConnection - Connection with database
     * @param official - Official that was present at this match.
     * @throws FailedToSaveEntityException Failed to save entity.
     */
    public void addOfficial(Connection databaseConnection, Official official) throws FailedToSaveEntityException
    {
        PreparedStatement statement = null;
        try {
            statement = databaseConnection.prepareStatement("INSERT INTO arbitrer (arbitreid, matchid) VALUES(?, ?);");
            statement.setInt(1, official.getId());
            statement.setInt(2, id);
            statement.execute();
            databaseConnection.commit();

        } catch (SQLException e) {
            try {
                databaseConnection.rollback();
            } catch (SQLException e1) {
                Logger.error(LOG_TYPE.EXCEPTION, e1.getMessage());
            }
            e.printStackTrace();
            throw new FailedToSaveEntityException(e);
        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Get the local team for this match.
     *
     * @param databaseConnection - Connection with database
     * @return Team - If found, otherwise return null.
     */
    public Team getLocalTeam(Connection databaseConnection)
    {
        return Team.getTeamWithId(databaseConnection, localTeamId);
    }

    /**
     * Set the local team for this match.
     *
     * @param localTeam - Local team.
     */
    public void setLocalTeam(Team localTeam)
    {
        this.localTeamId = localTeam.getId();
    }

    /**
     * Get the visitor team for this match.
     *
     * @param databaseConnection - Connection with database
     * @return Team - If found, otherwise return null.
     */
    public Team getVisitorTeam(Connection databaseConnection)
    {
        return Team.getTeamWithId(databaseConnection, visitorTeamId);
    }

    /**
     * Set the visitor team for this match.
     *
     * @param visitorTeam - Visitor team.
     */
    public void setVisitorTeam(Team visitorTeam)
    {
        this.visitorTeamId = visitorTeam.getId();
    }

    /**
     * Get the fiels the match was/will be played on.
     *
     * @param databaseConnection - Connection with database
     * @return Field - If found, otherwise return null.
     */
    public Field getField(Connection databaseConnection)
    {
        return Field.getFieldWithId(databaseConnection, fieldId);
    }

    /**
     * Set the field for this match.
     *
     * @param field - Match was played on this field.
     */
    public void setField(Field field)
    {
        this.fieldId = field.getId();
    }

    /**
     * Get the date of the match.
     *
     * @return Date - Date of the match.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Set the date of the match.
     *
     * @param date - Date of the match.
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Get the time of the match.
     *
     * @return Time - Time of the match.
     */
    public Time getTime()
    {
        return time;
    }

    /**
     * Set the time of the match.
     *
     * @param time - Time of the match.
     */
    public void setTime(Time time)
    {
        this.time = time;
    }

    /**
     * Get the local team score.
     *
     * @return int - Local team score.
     */
    public final int getLocalTeamScore()
    {
        return localTeamScore;
    }

    /**
     * Set the local team score.
     *
     * @param localTeamScore - Local team score.
     */
    public void setLocalTeamScore(int localTeamScore)
    {
        this.localTeamScore = localTeamScore;
    }

    /**
     * Get the visitor team score.
     *
     * @return int - Visitor team score.
     */
    public final int getVisitorTeamScore()
    {
        return visitorTeamScore;
    }

    /**
     * Set the visitor team score.
     *
     * @param visitorTeamScore - Visitor team score.
     */
    public void setVisitorTeamScore(int visitorTeamScore)
    {
        this.visitorTeamScore = visitorTeamScore;
    }

}
