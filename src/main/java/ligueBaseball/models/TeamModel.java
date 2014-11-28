package ligueBaseball.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TeamModel
{
    private String name;
    private FieldModel field;
    private List<PlayerModel> players = new ArrayList<>();

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
}
