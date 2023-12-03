package main.repository;

import main.model.Player;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PlayRepositoryImpl implements PlayRepository {

    private final Map<String, Player> players = new HashMap<>();

    @Override
    public Integer saveNewPlayer(String playerName) {
        Player player = new Player(playerName,0, 100);
        players.put(playerName, player);
        return 100;
    }

    @Override
    public Integer getBalanceByName(String playerName) {
        return players.get(playerName).getBalance();
    }

    @Override
    public void restartByName(String playerName) {
        Player player = players.get(playerName);
        player.setBalance(100);
        player.addLost();
        players.put(playerName, player);
    }

    @Override
    public void setBalance(String playerName, Integer balance) {
        Player player = players.get(playerName);
        player.setBalance(balance);
        players.put(playerName, player);
    }

    @Override
    public Boolean notExistsByName(String playerName) {
        return !players.containsKey(playerName);
    }

}
