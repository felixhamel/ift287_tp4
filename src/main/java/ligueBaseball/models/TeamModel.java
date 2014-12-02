package ligueBaseball.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Player;
import ligueBaseball.entities.Team;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
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
                this.getPlayers().add(new PlayerModel(player));
            }
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public FieldModel getField()
    {
        return field;
    }

    public void setField(FieldModel field)
    {
        this.field = field;
    }

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
            // throw
        }

        // Extract informations from team
        Team team = (Team) entity;
        this.setId(team.getId());
        this.setName(team.getName());
        this.setField(new FieldModel(team.getField()));
    }
}
