package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class FailedToConnectToDatabaseException extends ApplicationException
{
    private static final long serialVersionUID = 5354390958805004929L;

    public FailedToConnectToDatabaseException(String databaseName, Throwable cause) {
        super(Status.INTERNAL_SERVER_ERROR, String.format("Failed to connect to database '%s'.", databaseName), cause);
    }
}
