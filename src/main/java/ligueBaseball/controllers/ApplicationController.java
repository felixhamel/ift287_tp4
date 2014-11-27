package ligueBaseball.controllers;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ligueBaseball.Application;

@Path("/settings")
public class ApplicationController
{
    @GET
    @Path("connection")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean getStatusOfConnection()
    {
        if (Application.connection == null) {
            return false;
        }

        try {
            return !Application.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @POST
    @Path("connection")
    public boolean connectToDatabase(@QueryParam("hostname") final String hostname, @QueryParam("database") final String database, @QueryParam("username") final String username, @QueryParam("password") final String password)
    {
        return true;
        // return Application.connectToDatabase(hostname, database, username, password);
    }

    @DELETE
    @Path("connection")
    public boolean disconnectFromDatabase()
    {
        return Application.disconnectFromDatabase();
    }
}
