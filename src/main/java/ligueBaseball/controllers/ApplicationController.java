package ligueBaseball.controllers;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ligueBaseball.Application;
import ligueBaseball.models.ConnectionModel;

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
    public boolean connectToDatabase(ConnectionModel connection)
    {
        return Application.connectToDatabase(connection.getHostname(), connection.getDatabase(), connection.getUsername(), connection.getPassword());
    }

    @DELETE
    @Path("connection")
    public boolean disconnectFromDatabase()
    {
        return Application.disconnectFromDatabase();
    }
}
