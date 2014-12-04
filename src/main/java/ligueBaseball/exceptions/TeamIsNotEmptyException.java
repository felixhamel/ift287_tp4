package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class TeamIsNotEmptyException extends ApplicationException
{
    private static final long serialVersionUID = 1875701240978298710L;

    public TeamIsNotEmptyException(String teamName) {
        super(Status.PRECONDITION_FAILED, String.format("L'équipe '%s' n'est pas vide. Retirer tout les joueurs avant de pouvoir détruire cette équipe.", teamName));
    }
}
