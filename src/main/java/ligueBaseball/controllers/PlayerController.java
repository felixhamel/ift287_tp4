package ligueBaseball.controllers;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Player;
import ligueBaseball.exceptions.CannotFindPlayerWithIdException;
import ligueBaseball.exceptions.FailedToSaveEntityException;

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerController
{
    /**
     * Get all the players in the database.
     *
     * @return List<Player> - All the players.
     */
    @GET
    public List<Player> getAllPlayers()
    {
        return Player.getAllPlayers();
    }

    /**
     * Get the player with the given ID.
     *
     * @param playerId - ID of the player we want.
     * @return Player - Wanted player with given ID.
     * @throws CannotFindPlayerWithIdException
     */
    @GET
    @Path("{id}")
    public Player getPlayerWithId(@PathParam("id") final int playerId) throws CannotFindPlayerWithIdException
    {
        Player player = Player.getPlayerWithId(playerId);
        if (player == null) {
            throw new CannotFindPlayerWithIdException(playerId);
        }
        return player;
    }

    /**
     * Create a new player with the given informations.
     *
     * @param firstName - First name of the new player.
     * @param lastName - Last name of the new player.
     * @return Player - The created player.
     * @throws FailedToSaveEntityException
     */
    @POST
    public Player createNewPlayer(@FormParam("firstname") final String firstName, @FormParam("lastname") final String lastName) throws FailedToSaveEntityException
    {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);

        try {
            player.save();
        } catch (FailedToSaveEntityException e) {
            throw e;
        }

        return player;
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
}
