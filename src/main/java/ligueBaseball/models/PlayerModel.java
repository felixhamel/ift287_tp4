package ligueBaseball.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Player;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
@JsonSerialize(include = Inclusion.NON_NULL)
public class PlayerModel extends AbstractModel
{
    private int id;
    private String lastName;
    private String firstName;

    private int numero;
    private Date dateBegin;
    private Date dateEnd;

    public PlayerModel(Player player) throws FailedToRetrievePlayersOfTeamException {
        this.createFromEntity(player);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public Date getDateBegin()
    {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin)
    {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd()
    {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd)
    {
        this.dateEnd = dateEnd;
    }

    @Override
    public void createFromEntity(DatabaseEntity entity) throws FailedToRetrievePlayersOfTeamException
    {
        if (!(entity instanceof Player)) {
            // throw
        }
        Player player = (Player) entity;
        this.setFirstName(player.getFirstName());
        this.setLastName(player.getLastName());
        this.setId(player.getId());
        this.setDateBegin(player.getBeginningDate());
        this.setDateEnd(player.getEndDate());
        this.setNumero(player.getNumber());
    }
}
