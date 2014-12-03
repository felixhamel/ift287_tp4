package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ligueBaseball.entities.Field;
import ligueBaseball.entities.Team;
import ligueBaseball.entities.Match;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToRetrieveMatchException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.exceptions.TeamDoesntExistException;
import ligueBaseball.models.MatchModel;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
public class MatchController {
	
    @GET
    public List<MatchModel> getAllMatch() throws FailedToRetrievePlayersOfTeamException,FailedToRetrieveMatchException
    {
    	List<Match> matchs = Match.getAllMatch();
    	List<MatchModel> models = new ArrayList<>();
    	
    	for (Match match : matchs)
    	{
    		models.add(new MatchModel(match));
    	}
    	return models;
    }
	
    @GET
    @Path("{teamName}")
    public List<MatchModel> getMatchForTeam(@PathParam("teamName") String team)throws FailedToRetrieveMatchException,TeamDoesntExistException,FailedToRetrievePlayersOfTeamException 
    {
    	List<Match> matchs = Match.getMatchForTeam(team);
    	List<MatchModel> models = new ArrayList<>();
    	
    	for (Match match : matchs)
    	{
    		models.add(new MatchModel(match));
    	}
    	return models;
    	
    	
    }
    
    @GET
    @Path("{date}")
    public List<MatchModel> getMatchDate(@PathParam("date") String date) throws FailedToRetrieveMatchException,TeamDoesntExistException,FailedToRetrievePlayersOfTeamException
    {
    	List<Match> matchs = Match.getMatchWithDate(date);
    	List<MatchModel> models = new ArrayList<>();
    	
    	for (Match match : matchs)
    	{
    		models.add(new MatchModel(match));
    	}
    	return models;
    }

	@POST
    public void createNewMatch(MatchModel m) throws FailedToSaveEntityException, FailedToRetrievePlayersOfTeamException
    {
        Match match = new Match();
        /*player.setFirstName(receivedPlayer.getFirstName());
        player.setLastName(receivedPlayer.getLastName());*/
        match.setDate(m.getDate());
        match.setTime(m.getTime());
        Field field = Field.getFieldWithName((m.getField().getName()));
        if (field == null) {
        	//TODO
        }
        match.setField(field);

        Team localteam = Team.getTeamWithName(m.getTeamLocal().getName());
        if(localteam == null){
        	//TODO
        }
        match.setLocalTeam(localteam);
        
        Team visitorTeam = Team.getTeamWithName(m.getTeamVisiteur().getName());
        if(visitorTeam == null){
        	
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

}
