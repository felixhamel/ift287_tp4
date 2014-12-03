package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Official;
import ligueBaseball.exceptions.CannotCreateOfficialException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.exceptions.ConnotFindOfficialWithIdException;
import ligueBaseball.models.OfficialModel;
import ligueBaseball.models.PlayerModel;

@Path("/official")
@Produces(MediaType.APPLICATION_JSON)
public class OfficialController
{
    @GET
    public List<OfficialModel> getAllOfficials()
    {
        List<Official> officials = Official.getAllOfficials();
        List<OfficialModel> models = new ArrayList<>();

        // Generate models from entities
        for (Official Official : officials) {
            models.add(new OfficialModel(Official));
        }
        return models;
    }

    @GET
    @Path("{id}")
    public OfficialModel getOfficialWithId(@PathParam("id") final int officialId)
    {
    	Official official = Official.getOfficialWithId(officialId);
        if (official == null) {
            throw new ConnotFindOfficialWithIdException(officialId);
        }
        return new OfficialModel(official);
    }

    /**
     * Create a new official with the given informations.
     *
     * @param firstName - First name of the new official.
     * @param lastName - Last name of the new official.
     * @return Official - The created official.
     * @throws CannotCreateOfficialException
     */
    @POST
    public void createOfficial(OfficialModel OfficialModel)
    {
        Official official = new Official();
        official.setFirstName(OfficialModel.getFirstName());
        official.setLastName(OfficialModel.getLastName());

        try {
        	official.save();
        } catch (FailedToSaveEntityException e) {
            throw new CannotCreateOfficialException("Cannot create Official.", e);
        }
    }
     
}
