package ligueBaseball.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import ligueBaseball.entities.DatabaseEntity;
import ligueBaseball.entities.Field;
import ligueBaseball.exceptions.NotInstanceOfClassException;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlRootElement(name = "terrain")
@JsonRootName("field")
public class FieldModel extends AbstractModel
{
    private String name;
    private String address;

    public FieldModel() {

    }

    public FieldModel(Field field) {
        createFromEntity(field);
    }

    @XmlAttribute(name = "nom", required = true)
    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "adresse", required = true)
    @JsonProperty("address")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public void createFromEntity(DatabaseEntity entity)
    {
        if (!(entity instanceof Field)) {
            throw new NotInstanceOfClassException(this.getClass());
        }

        Field field = (Field) entity;
        this.setAddress(field.getAddress());
        this.setName(field.getName());
    }
}
