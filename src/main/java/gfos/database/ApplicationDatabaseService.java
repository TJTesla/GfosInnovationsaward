package gfos.database;

import java.sql.SQLException;

public class ApplicationDatabaseService extends DatabaseService {
    public ApplicationDatabaseService() throws SQLException {
        super();
    }

    public int createResume(String path, String name) {
        return -1;
    }

    private boolean resumeExists() {
        return false;
    }
}
