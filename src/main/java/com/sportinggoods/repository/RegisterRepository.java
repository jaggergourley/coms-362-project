package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.Register;
import src.main.java.com.sportinggoods.util.FileUtils;

public class RegisterRepository {
    private final String filePath = "data/register.csv";

    public boolean logRegisterActivity(String activity) {
        return FileUtils.appendToFile(filePath, activity);
    }
}
