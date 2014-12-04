package ligueBaseball.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Player;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.NotInstanceOfClassException;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "equipe")
@JsonRootName("team")
@JsonSerialize(include = Inclusion.NON_NULL)
public class TeamModel extends AbstractModel
{
    private int id;
    private String name;
    private FieldModel field;
    private List<PlayerModel> players;

    public TeamModel() {

    }

    public TeamModel(Team team) throws FailedToRetrievePlayersOfTeamException {
        this(team, false);
    }

    public TeamModel(Team team, boolean generatePlayerList) throws FailedToRetrievePlayersOfTeamException {
        createFromEntity(team);
        if (generatePlayerList) {
            players = new ArrayList<>();
            for (Player player : team.getPlayers()) {
                PlayerModel model = new PlayerModel(player);
                model.removeTeam();
                this.getPlayers().add(model);
            }
        }
    }

    @XmlAttribute(name = "id", required = false)
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "nom")
    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlElement(name = "terrain")
    @JsonProperty("field")
    public FieldModel getField()
    {
        return field;
    }

    public void setField(FieldModel field)
    {
        this.field = field;
    }

    @XmlElement(name = "joueurs")
    @JsonProperty("players")
    public List<PlayerModel> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<PlayerModel> players)
    {
        this.players = players;
    }

    @Override
    public void createFromEntity(DatabaseEntity entity) throws FailedToRetrievePlayersOfTeamException
    {
        if (!(entity instanceof Team)) {
            throw new NotInstanceOfClassException(this.getClass());
        }

        // Extract informations from team
        Team team = (Team) entity;
        this.setId(team.getId());
        this.setName(team.getName());
        this.setField(new FieldModel(team.getField()));
    }
}
