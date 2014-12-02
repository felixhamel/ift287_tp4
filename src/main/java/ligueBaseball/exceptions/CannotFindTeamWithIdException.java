package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class CannotFindTeamWithIdException extends ApplicationException
{
    private static final long serialVersionUID = 12542909443940409L;

    public CannotFindTeamWithIdException(int teamId) {
        super(Status.NOT_FOUND, String.format("Impossible de trouver une équipe avec l'ID #%s", teamId));
    }

    public CannotFindTeamWithIdException(int teamId, Throwable cause) {
        super(Status.NOT_FOUND, String.format("Impossible de trouver une équipe avec l'ID #%s", teamId), cause);
    }
}
