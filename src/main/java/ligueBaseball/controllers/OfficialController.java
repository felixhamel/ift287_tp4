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
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.models.OfficialModel;

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
        // TODO
        return null;
    }

    @POST
    public void createOfficial(OfficialModel OfficialModel)
    {
        Official Official = new Official();
        Official.setFirstName(OfficialModel.getFirstName());
        Official.setLastName(OfficialModel.getLastName());

        try {
            Official.save();
        } catch (FailedToSaveEntityException e) {
            throw new CannotCreateOfficialException("Cannot create Official.", e);
        }
    }

    // To Move inside the match controller under /match/{id}/officials @PUT
    /*
     * @POST public void addOfficial(OfficialModel OfficialModel) { Official Official = new Official();
     * Official.setFirstName(OfficialModel.getFirstName()); Official.setLastName(OfficialModel.getLastName()); Match Match = new Match();
     * Match.addOfficial(Official);
     * 
     * try { Match.save(); } catch (FailedToSaveEntityException e) { throw new CannotAddOfficialException("Cannot add Official.", e); } }
     */
}
