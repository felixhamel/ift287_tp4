package ligueBaseball.models;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Match;
import ligueBaseball.entities.Official;
import ligueBaseball.exceptions.FailedToRetrieveMatchException;
import ligueBaseball.exceptions.FailedToRetrievePlayersOfTeamException;
import ligueBaseball.exceptions.NotInstanceOfClassException;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
@JsonSerialize(include = Inclusion.NON_NULL)
public class MatchModel
{
    private int id;
    private Date date;
    private Time time;
    private int localTeamScore = 0;
    private int visitorTeamScore = 0;

    private FieldModel field;
    private TeamModel localTeam;
    private TeamModel visitorTeam;

    private List<OfficialModel> officials = new ArrayList<>();

    public MatchModel() {

    }

    public MatchModel(Match match) throws FailedToRetrieveMatchException, FailedToRetrievePlayersOfTeamException {
        this.createFromEntity(match);
    }

    public int getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Time getTime()
    {
        return time;
    }

    public void setTime(Time t)
    {
        this.time = t;
    }

    public int getLocalTeamScore()
    {
        return localTeamScore;
    }

    public void setLocalTeamScore(int score)
    {
        this.localTeamScore = score;
    }

    public int getVisitorTeamScore()
    {
        return visitorTeamScore;
    }

    public void setVisitorTeamScore(int score)
    {
        this.visitorTeamScore = score;
    }

    public TeamModel getTeamLocal()
    {
        return localTeam;
    }

    public TeamModel getTeamVisitor()
    {
        return visitorTeam;
    }

    public FieldModel getField()
    {
        return field;
    }

    public List<OfficialModel> getOfficials()
    {
        return officials;
    }

    public void createFromEntity(DatabaseEntity entity) throws FailedToRetrieveMatchException, FailedToRetrievePlayersOfTeamException
    {
        if (!(entity instanceof Match)) {
            throw new NotInstanceOfClassException(Match.class);
        }

        Match match = (Match) entity;
        this.id = match.getId();
        this.setTime(match.getTime());
        this.setDate(match.getDate());
        this.setLocalTeamScore(match.getLocalTeamScore());
        this.setVisitorTeamScore(match.getVisitorTeamScore());
        this.localTeam = new TeamModel(match.getLocalTeam());
        this.visitorTeam = new TeamModel(match.getVisitorTeam());
        this.field = new FieldModel(match.getField());

        // Add officials
        for (Official official : match.getOfficials()) {
            this.officials.add(new OfficialModel(official));
        }
    }
}
