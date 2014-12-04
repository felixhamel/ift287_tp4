package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Field;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.CannotCreateTeamException;
import ligueBaseball.exceptions.CannotFindTeamWithIdException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.models.TeamModel;

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
}
