package ligueBaseball.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestException
{
    private String exception;
    private String cause;

    public RestException(String exception) {
        this.exception = exception;
    }

    public RestException(String exception, Throwable cause) {
        this.exception = exception;
        this.cause = cause.getMessage();
    }

    public String getException()
    {
        return exception;
    }

    public String getCause()
    {
        return cause;
    }
}
