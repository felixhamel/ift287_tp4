package ligueBaseball.models;

import ligueBaseball.entities.DatabaseEntity;

public abstract class AbstractModel
{
    /**
     * Default constructor.
     */
    public AbstractModel() {
    }

    /**
     * Create a new model from an entity.
     *
     * @param entity
     * @throws Exception
     */
    public AbstractModel(DatabaseEntity entity) throws Exception {
        this.createFromEntity(entity);
    }

    /**
     * Create the current model instance from an existing entity.
     *
     * @param entity
     */
    public abstract void createFromEntity(DatabaseEntity entity) throws Exception;
}
