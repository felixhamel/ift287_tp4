package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Player;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.CannotFindPlayerWithIdException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.models.PlayerModel;
import ligueBaseball.models.TeamModel;

import com.google.common.base.Strings;

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerController
{
    /**
     * Get all the players in the database.
     *
     * @return List<Player> - All the players.
     * @throws FailedToRetrievePlayersOfTeamException
     */
    @GET
    public List<PlayerModel> getAllPlayers() throws FailedToRetrievePlayersOfTeamException
    {
        List<PlayerModel> models = new ArrayList<>();
        for (Player player : Player.getAllPlayers()) {
            models.add(new PlayerModel(player));
        }
        return models;
    }

    /**
     * Get the player with the given ID.
     *
     * @param playerId - ID of the player we want.
     * @return Player - Wanted player with given ID.
     * @throws CannotFindPlayerWithIdException
     * @throws FailedToRetrievePlayersOfTeamException
     */
    @GET
    @Path("{id}")
    public PlayerModel getPlayerWithId(@PathParam("id") final int playerId) throws CannotFindPlayerWithIdException, FailedToRetrievePlayersOfTeamException
    {
        Player player = Player.getPlayerWithId(playerId);
        if (player == null) {
            throw new CannotFindPlayerWithIdException(playerId);
        }
        return new PlayerModel(player);
    }

    /**
     * Create a new player with the given informations.
     *
     * @param firstName - First name of the new player.
     * @param lastName - Last name of the new player.
     * @return Player - The created player.
     * @throws FailedToSaveEntityException
     * @throws FailedToRetrievePlayersOfTeamException
     */
    @POST
    public PlayerModel createNewPlayer(PlayerModel receivedPlayer) throws FailedToSaveEntityException, FailedToRetrievePlayersOfTeamException
    {
        Player player = new Player();
        player.setFirstName(receivedPlayer.getFirstName());
        player.setLastName(receivedPlayer.getLastName());

        try {
            player.save();
        } catch (FailedToSaveEntityException e) {
            e.printStackTrace();
            throw e;
        }

        return new PlayerModel(player);
    }

    /**
     * Update a player.
     *
     * @param playerId
     * @param firstName
     * @param lastName
     * @return
     * @throws FailedToRetrievePlayersOfTeamException
     */
    @PUT
    @Path("{id}")
    public PlayerModel updatePlayer(@PathParam("id") final int playerId, Player receivedPlayer) throws FailedToRetrievePlayersOfTeamException
    {
        Player player = Player.getPlayerWithId(playerId);
        if (player == null) {
            throw new CannotFindPlayerWithIdException(playerId);
        }

        // Validation
        if (Strings.isNullOrEmpty(receivedPlayer.getFirstName())) {
            // throw
            System.out.println("Problem!");
            return null;
        }
        if (Strings.isNullOrEmpty(receivedPlayer.getLastName())) {
            // throw
            System.out.println("Problem!");
            return null;
        }

        // Update
        player.setFirstName(receivedPlayer.getFirstName());
        player.setLastName(receivedPlayer.getLastName());

        // Save it !
        try {
            player.save();
        } catch (FailedToSaveEntityException e) {
            e.printStackTrace();
            throw e;
        }

        // Create model
        return new PlayerModel(player);
    }

    /**
     * Delete the player with the given ID if found.
     *
     * @param playerId - ID of the player we want to delete.
     * @return Boolean - If the deletion was successful or not.
     * @throws CannotFindPlayerWithIdException
     */
    @DELETE
    @Path("{id}")
    public Boolean deletePlayerWithId(@PathParam("id") final int playerId) throws CannotFindPlayerWithIdException
    {
        // Find player with ID.
        Player player = Player.getPlayerWithId(playerId);
        if (player == null) {
            throw new CannotFindPlayerWithIdException(playerId);
        }
        try {
            player.delete();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @GET
    @Path("{id}/team")
    public TeamModel getTeamForPlayerIfAny(@PathParam("id") final int playerId) throws FailedToRetrievePlayersOfTeamException
    {
        Player player = Player.getPlayerWithId(playerId);
        if (player == null) {
            throw new CannotFindPlayerWithIdException(playerId);
        }

        Team team = player.getTeam();
        return new TeamModel(team);
    }
}
