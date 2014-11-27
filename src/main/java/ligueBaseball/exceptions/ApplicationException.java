package ligueBaseball.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public abstract class ApplicationException extends WebApplicationException
{
    private static final long serialVersionUID = 2340314836893794616L;

    public ApplicationException(javax.ws.rs.core.Response.Status status, String message) {
        super(Response.status(status).entity(new RestException(message)).build());
    }

    public ApplicationException(javax.ws.rs.core.Response.Status status, String message, Throwable cause) {
        super(Response.status(status).entity(new RestException(message, cause)).build());
    }
}
