package ligueBaseball.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Player;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.NotInstanceOfClassException;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "joueur")
@JsonRootName("player")
@JsonSerialize(include = Inclusion.NON_NULL)
public class PlayerModel extends AbstractModel
{
    private int id;
    private String lastName;
    private String firstName;

    private int number;
    private Date dateBegin;
    private Date dateEnd;

    private TeamModel team;

    public PlayerModel() {

    }

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

    @XmlAttribute(name = "nom")
    @JsonProperty("lastName")
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @XmlAttribute(name = "prenom")
    @JsonProperty("firstName")
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @XmlAttribute(name = "numero")
    @JsonProperty("number")
    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    @XmlAttribute(name = "datedebut")
    @JsonProperty("dateBegin")
    public String getDateBegin()
    {
        if (dateBegin != null) {
            return new SimpleDateFormat("dd-MMM-yyyy").format(dateBegin);
        }
        return "";
    }

    public void setDateBegin(Date dateBegin)
    {
        this.dateBegin = dateBegin;
    }

    @XmlTransient
    @JsonProperty("dateEnd")
    public Date getDateEnd()
    {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd)
    {
        this.dateEnd = dateEnd;
    }

    @JsonProperty("team")
    public TeamModel getTeam()
    {
        return team;
    }

    public void removeTeam()
    {
        this.team = null;
    }

    @Override
    public void createFromEntity(DatabaseEntity entity) throws FailedToRetrievePlayersOfTeamException
    {
        if (!(entity instanceof Player)) {
            throw new NotInstanceOfClassException(this.getClass());
        }

        Player player = (Player) entity;
        this.setId(player.getId());
        this.setFirstName(player.getFirstName());
        this.setLastName(player.getLastName());
        this.setId(player.getId());
        this.setDateBegin(player.getBeginningDate());
        this.setDateEnd(player.getEndDate());
        this.setNumber(player.getNumber());

        Team team = player.getTeam();
        if (team != null) {
            this.team = new TeamModel(player.getTeam(), false);
        }
    }
}
