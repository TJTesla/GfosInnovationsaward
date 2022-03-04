package gfos.database;

import gfos.beans.User;

public interface UserDatabaseInterface {
    public User getById(int id);
}
