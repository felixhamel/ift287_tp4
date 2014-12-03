package ligueBaseball.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.entities.Match;
import ligueBaseball.entities.Official;
import ligueBaseball.exceptions.CannotAddOfficialException;
import ligueBaseball.exceptions.CannotCreateOfficialException;
import ligueBaseball.exceptions.FailedToSaveEntityException;
import ligueBaseball.models.OfficialModel;

@Path("/arbitre")
@Produces(MediaType.APPLICATION_JSON)

public class OfficialController {
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
    @POST
    public void addOfficial(OfficialModel OfficialModel)
    {
        Official Official = new Official();
        Official.setFirstName(OfficialModel.getFirstName());
        Official.setLastName(OfficialModel.getLastName());
        Match Match = new Match();
        Match.addOfficial(Official);

        try {
            Match.save();
        } catch (FailedToSaveEntityException e) {
            throw new CannotAddOfficialException("Cannot add Official.", e);
        }
    }
}
