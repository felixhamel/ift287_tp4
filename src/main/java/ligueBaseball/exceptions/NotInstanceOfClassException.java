package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class NotInstanceOfClassException extends ApplicationException
{
    private static final long serialVersionUID = -6562710653778221953L;

    public NotInstanceOfClassException(Class expectedType) {
        super(Status.PRECONDITION_FAILED, String.format("Mauvais type d'objet reçu dans la requête. S'attendait a recevoit un objet du type '%s'.", expectedType.getName()));
    }

    public NotInstanceOfClassException(Class expectedType, Throwable cause) {
        super(Status.PRECONDITION_FAILED, String.format("Mauvais type d'objet reçu dans la requête. S'attendait a recevoit un objet du type '%s'.", expectedType.getName()), cause);
    }
}
