package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.models.TeamModel;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
public class TeamController
{
    @GET
    public List<TeamModel> getAllTeams() throws FailedToRetrievePlayersOfTeamException
    {
        List<Team> teams = Team.getAllTeams();
        List<TeamModel> models = new ArrayList<>();

        // Generate models from entities
        for (Team team : teams) {
            models.add(new TeamModel(team, true));
        }
        return models;
    }

    @GET
    @Path("{id}")
    public TeamModel getTeamWithId(@PathParam("id") final int teamId) throws FailedToRetrievePlayersOfTeamException
    {
        Team team = Team.getTeamWithId(teamId);
        if (team == null) {
            // throw
        }

        return new TeamModel(team, true);
    }
}
