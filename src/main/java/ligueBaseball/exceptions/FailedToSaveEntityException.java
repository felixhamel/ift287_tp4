package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class FailedToSaveEntityException extends ApplicationException
{
    private static final long serialVersionUID = 890161362571158318L;

    public FailedToSaveEntityException(String message) {
        super(Status.PRECONDITION_FAILED, "Impossible de sauvegarder l'entitée parce que " + message);
    }

    public FailedToSaveEntityException(Throwable cause) {
        super(Status.PRECONDITION_FAILED, "Impossible de sauvegarder l'entité.", cause);
    }
}
