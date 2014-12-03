package ligueBaseball.models;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Official;
import ligueBaseball.exceptions.NotInstanceOfClassException;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
@JsonSerialize(include = Inclusion.NON_NULL)
public class OfficialModel extends AbstractModel
{
    private int id;
    private String firstName;
    private String lastName;

    public OfficialModel(Official official) throws NotInstanceOfClassException {
        createFromEntity(official);
    }

    @Override
    public void createFromEntity(DatabaseEntity entity) throws NotInstanceOfClassException
    {
        // Make sure the received entity
        if (!(entity instanceof Official)) {
            throw new NotInstanceOfClassException(this.getClass());
        }

        Official official = (Official) entity;
        this.id = official.getId();
        this.firstName = official.getFirstName();
        this.lastName = official.getLastName();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String name)
    {
        this.firstName = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String name)
    {
        this.lastName = name;
    }
}
