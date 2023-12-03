package main.service;

import main.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayServiceImpl implements PlayService {

    @Autowired
    private PlayRepository playRepository;

    @Override
    public String getResponse(String request) {

        String[] args = request.split("\\s");
        String playerName = args[0];
        if (playerName.length() < 3) {
            return "Bad request - bad name";
        }
        int bet;
        try {
            bet = Integer.parseInt(args[args.length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
            return "Bad request - bad bet)))";
        }
        String response = "";
        if (playRepository.notExistsByName(playerName)) {
            playRepository.saveNewPlayer(playerName);
            response += " Hello. You get 100 coins.";
        }
        int balance = playRepository.getBalanceByName(playerName);
        if (balance == 0) {
            playRepository.restartByName(playerName);
            return "Your balance = 0! Start again!";
        }
        if (bet > balance) {
            bet = balance;
            response += " Your balance less then bet. Let's bet = " + balance;
        }
        boolean winner = (Math.random() < 0.5);
        if (winner) {
            int win = (int)(bet * 1.9);
            playRepository.setBalance(playerName, balance - bet + win);
            response += " You won " + (win - bet) + " coins";
        } else {
            playRepository.setBalance(playerName, balance - bet);
            response += " You lost " + bet + " coins";
        }

        return playerName + response;
    }
}
