package ligueBaseball.exceptions;

public class FailedToCreateTableException extends Exception
{
    private static final long serialVersionUID = -7977978099008660687L;

    public FailedToCreateTableException(String tableName) {
        super(String.format("Impossible de créer une nouvelle table ayant comme nom '%s'.", tableName));
    }

    public FailedToCreateTableException(String tableName, Throwable cause) {
        super(String.format("Impossible de créer une nouvelle table ayant comme nom '%s'.", tableName), cause);
    }
}
