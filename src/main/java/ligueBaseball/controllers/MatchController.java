package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Field;
import ligueBaseball.entities.Match;
import ligueBaseball.entities.Official;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.CannotAddOfficialException;
import ligueBaseball.exceptions.FailedToRetrieveMatchException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.models.MatchModel;
import ligueBaseball.models.OfficialModel;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
public class MatchController
{
    @GET
    public List<MatchModel> getAllMatch() throws FailedToRetrievePlayersOfTeamException, FailedToRetrieveMatchException
    {
        List<Match> matchs = Match.getAllMatch();
        List<MatchModel> models = new ArrayList<>();

        for (Match match : matchs) {
            models.add(new MatchModel(match));
        }
        return models;
    }

    @POST
    public void createNewMatch(MatchModel m) throws FailedToSaveEntityException, FailedToRetrievePlayersOfTeamException
    {
        Match match = new Match();
        /*
         * player.setFirstName(receivedPlayer.getFirstName()); player.setLastName(receivedPlayer.getLastName());
         */
        match.setDate(m.getDate());
        match.setTime(m.getTime());
        Field field = Field.getFieldWithName(m.getField().getName());
        if (field == null) {
            // TODO
        }
        match.setField(field);

        Team localteam = Team.getTeamWithName(m.getTeamLocal().getName());
        if (localteam == null) {
            // TODO
        }
        match.setLocalTeam(localteam);

        Team visitorTeam = Team.getTeamWithName(m.getTeamVisitor().getName());
        if (visitorTeam == null) {

        }
        match.setVisitorTeam(visitorTeam);
        match.setLocalTeamScore(m.getLocalTeamScore());
        match.setVisitorTeamScore(m.getVisitorTeamScore());

        try {
            match.save();
        } catch (FailedToSaveEntityException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GET
    @Path("/match/{id}/officials")
    public List<OfficialModel> getOfficialsForMatch(@PathParam("id") final int matchId)
    {
        // Make sure that the match exist
        Match match = Match.getMatchWithId(matchId);
        if (match == null) {

        }

        // Retrieve officials
        List<OfficialModel> models = new ArrayList<>();
        for (Official official : match.getOfficials()) {
            models.add(new OfficialModel(official));
        }
        return models;
    }

    @PUT
    @Path("/match/{id}/officials")
    public void addOfficial(@PathParam("id") final int matchId, @QueryParam("id") final int officialId)
    {
        // Make sure that the official exists
        Official official = Official.getOfficialWithId(officialId);
        if (official == null) {
            // throw
        }

        // Make sure that the match exists
        Match match = Match.getMatchWithId(officialId);
        if (match == null) {
            // throw
        }

        try {
            match.addOfficial(official);
            match.save();
        } catch (FailedToSaveEntityException e) {
            throw new CannotAddOfficialException("Impossible d'ajouter l'arbitre au match.", e);
        }
    }
}