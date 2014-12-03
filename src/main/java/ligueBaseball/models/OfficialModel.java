package ligueBaseball.models;

import java.util.Date;




import java.util.List;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Official;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
@JsonSerialize(include = Inclusion.NON_NULL)
public class OfficialModel extends AbstractModel{
	
	private int id;
    private String firstName;
    private String lastName;
    
	public OfficialModel(Official official)  {
		// TODO
    }

	@Override
	public void createFromEntity(DatabaseEntity entity) throws Exception {
		// TODO Auto-generated method stub
		
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
