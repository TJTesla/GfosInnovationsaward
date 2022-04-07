package gfos.database;

import gfos.pojos.User;

public interface UserDatabaseInterface {
    public User getById(int id);
}
