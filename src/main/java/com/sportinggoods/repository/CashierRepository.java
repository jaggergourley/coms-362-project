package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.Cashier;
import src.main.java.com.sportinggoods.util.FileUtils;

import java.util.List;

public class CashierRepository {
    private final String filePath = "data/cashiers.csv";

    public boolean addCashier(Cashier cashier) {
        return FileUtils.appendToFile(filePath, cashier.toCSV());
    }

    public List<Cashier> getAllCashiers() {
        return FileUtils.readAllLines(filePath).stream()
                .map(Cashier::fromCSV)
                .toList();
    }
}
