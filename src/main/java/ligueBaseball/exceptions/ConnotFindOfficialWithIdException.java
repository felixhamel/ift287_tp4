package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class ConnotFindOfficialWithIdException extends ApplicationException {
	private static final long serialVersionUID = 1450582977318931294L;

	public ConnotFindOfficialWithIdException(int officialId) {
        super(Status.NOT_FOUND, "Impossible de trouver l'arbitre avec l'identifiant " + officialId + ".");
    }
}
