package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class CannotFindPlayerWithIdException extends ApplicationException
{
    private static final long serialVersionUID = 463979116079901002L;

    public CannotFindPlayerWithIdException(int playerId) {
        super(Status.NOT_FOUND, "Impossible de trouver le joueur avec l'identifiant " + playerId + ".");
    }
}
