package ligueBaseball.models;

import java.sql.Date;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PartOfTeamModel
{
    private int playerId;
    private int number;
    private String dateBegin = Date.valueOf(LocalDate.now()).toString();
    private TeamModel team;

    public PartOfTeamModel() {

    }

    public int getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(int playerId)
    {
        this.playerId = playerId;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getDateBegin()
    {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin)
    {
        this.dateBegin = dateBegin;
    }

    public TeamModel getTeam()
    {
        return team;
    }

    public void setTeam(TeamModel team)
    {
        this.team = team;
    }
}
