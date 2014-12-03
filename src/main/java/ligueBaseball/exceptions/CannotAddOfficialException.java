package ligueBaseball.exceptions;

import javax.ws.rs.core.Response.Status;

public class CannotAddOfficialException extends ApplicationException {
	private static final long serialVersionUID = 1450582977318931294L;

    public CannotAddOfficialException(String message) {
        super(Status.INTERNAL_SERVER_ERROR, message);
    }

    public CannotAddOfficialException(String message, Throwable cause) {
        super(Status.INTERNAL_SERVER_ERROR, message, cause);
    }
}
