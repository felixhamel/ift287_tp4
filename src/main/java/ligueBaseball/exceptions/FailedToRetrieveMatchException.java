package ligueBaseball.exceptions;

public class FailedToRetrieveMatchException extends Exception {

	private static final long serialVersionUID = -8023736835845663736L;
	
	public FailedToRetrieveMatchException() {
		super("Impossible d'aller récupérer les matchs");
	}
}
	

