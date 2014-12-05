package ligueBaseball.controllers;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ligueBaseball.entities.Field;
import ligueBaseball.entities.Player;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.CannotCreateTeamException;
import ligueBaseball.exceptions.CannotFindPlayerWithIdException;
import ligueBaseball.exceptions.CannotFindTeamWithIdException;
import ligueBaseball.exceptions.FailedToDeleteEntityException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.exceptions.TeamIsNotEmptyException;
import ligueBaseball.models.PartOfTeamModel;
import ligueBaseball.models.TeamModel;

import org.w3c.dom.Document;

import com.sun.jersey.multipart.FormDataParam;

@Path("/team")
public class TeamController
{
    private class TeamModelComparator implements Comparator<TeamModel>
    {
        @Override
        public int compare(TeamModel o1, TeamModel o2)
        {
            return o1.getName().compareTo(o2.getName());
        }
    };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TeamModel> getAllTeams() throws FailedToRetrievePlayersOfTeamException
    {
        List<Team> teams = Team.getAllTeams();
        List<TeamModel> models = new ArrayList<>();

        // Generate models from entities
        for (Team team : teams) {
            models.add(new TeamModel(team, true));
        }

        //  Sort teams by name
        models.sort(new TeamModelComparator());

        return models;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TeamModel getTeamWithId(@PathParam("id") final int teamId) throws FailedToRetrievePlayersOfTeamException, CannotFindTeamWithIdException
    {
        Team team = Team.getTeamWithId(teamId);
        if (team == null) {
            throw new CannotFindTeamWithIdException(teamId);
        }
        return new TeamModel(team, true);
    }

    @GET
    @Path("{id}/xml")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public TeamModel getTeamWithIdInXMLFormat(@PathParam("id") final int teamId) throws FailedToRetrievePlayersOfTeamException, CannotFindTeamWithIdException
    {
        Team team = Team.getTeamWithId(teamId);
        if (team == null) {
            throw new CannotFindTeamWithIdException(teamId);
        }
        return new TeamModel(team, true);
    }

    @POST
    public void createTeam(TeamModel teamModel)
    {
        Team team = new Team();
        team.setName(teamModel.getName());

        // Check if we need to create a new field
        Field field = Field.getFieldWithName(teamModel.getField().getName());
        if (field == null) {
            field = new Field();
            field.setName(teamModel.getField().getName());
            field.setAddress(teamModel.getField().getAddress());
            try {
                field.save();
            } catch (FailedToSaveEntityException e) {
                throw new CannotCreateTeamException("Cannot create field associated with team.", e);
            }
        }
        team.setField(field);

        try {
            team.save();
        } catch (FailedToSaveEntityException e) {
            throw new CannotCreateTeamException("Cannot create team.", e);
        }
    }

    @POST
    @Path("xml")
    @Consumes("multipart/form-data")
    public void createTeamFromXML(@FormDataParam("file") InputStream inputFile) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xml = builder.parse(inputFile);

        // Create team
        Team team = new Team();
        team.setName(xml.getUserData("name").toString());
    }

    @PUT
    @Path("{id}/player")
    public void addPlayerToTeam(@PathParam("id") final int teamId, PartOfTeamModel partOfTeam)
    {
        // Make sure that the team exists
        Team team = Team.getTeamWithId(teamId);
        if (team == null) {
            throw new CannotFindTeamWithIdException(teamId);
        }

        // Find player
        Player player = Player.getPlayerWithId(partOfTeam.getPlayerId());
        if (player == null) {
            throw new CannotFindPlayerWithIdException(partOfTeam.getPlayerId());
        }

        // Update player
        player.setNumber(partOfTeam.getNumber());
        if (partOfTeam.getDateBegin() != null) {
            player.setBeginningDate(Date.valueOf(partOfTeam.getDateBegin()));
        }

        // Add player to team
        team.addPlayer(player);
        team.save();

        System.out.println("ADDED PLAYER TO TEAM.");
    }

    @DELETE
    @Path("{id}")
    public void deleteTeam(@PathParam("id") final int teamId) throws TeamIsNotEmptyException, FailedToDeleteEntityException
    {
        // Check if team exists
        Team team = Team.getTeamWithId(teamId);
        if (team == null) {
            throw new CannotFindTeamWithIdException(teamId);
        }

        // Delete team
        try {
            team.delete();
        } catch (FailedToDeleteEntityException e) {
            throw e;
        } catch (TeamIsNotEmptyException e) {
            throw e;
        }
    }
}
