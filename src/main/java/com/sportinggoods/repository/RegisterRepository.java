package com.sportinggoods.repository;

import com.sportinggoods.model.Register;
import com.sportinggoods.util.FileUtils;

public class RegisterRepository {
    private final String filePath = "data/register.csv";

    public boolean logRegisterActivity(String activity) {
        return FileUtils.appendToFile(filePath, activity);
    }
}
