package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class CannotFindMatchWithIdException extends ApplicationException
{
    private static final long serialVersionUID = -7103968339978306665L;

    public CannotFindMatchWithIdException(int matchId) {
        super(Status.PRECONDITION_FAILED, String.format("Impossible de trouver le match avec l'ID %s.", matchId));
    }

    public CannotFindMatchWithIdException(int matchId, Throwable cause) {
        super(Status.PRECONDITION_FAILED, String.format("Impossible de trouver le match avec l'ID %s.", matchId), cause);

    }

}
