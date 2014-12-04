package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class FailedToDeleteEntityException extends ApplicationException
{
    private static final long serialVersionUID = 2048533964955399849L;

    public FailedToDeleteEntityException(String message) {
        super(Status.INTERNAL_SERVER_ERROR, message);
    }

    public FailedToDeleteEntityException(String message, Throwable cause) {
        super(Status.INTERNAL_SERVER_ERROR, message, cause);
    }

    public FailedToDeleteEntityException(Throwable cause) {
        super(Status.INTERNAL_SERVER_ERROR, "Impossible de détruire l'entitée.", cause);
    }
}
