package main.repository;

public interface PlayRepository {

    Integer saveNewPlayer(String playerName);

    Integer getBalanceByName(String playerName);

    void restartByName(String playerName);

    void setBalance(String playerName, Integer balance);

    Boolean notExistsByName(String playerName);

}
