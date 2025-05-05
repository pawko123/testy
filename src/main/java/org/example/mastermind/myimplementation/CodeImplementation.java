package org.example.mastermind.myimplementation;

import org.example.mastermind.CodeService;
import org.example.mastermind.Guess;

public class CodeImplementation implements CodeService {
    char[] availableColors = {'R', 'G', 'B', 'Y', 'O', 'P'};

    @Override
    public String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * availableColors.length);
            code.append(availableColors[randomIndex]);
        }
        return code.toString();
    }

    @Override
    public Guess checkGuess(String code, String guess) {
       StringBuilder guessCode = new StringBuilder();

       for (int i = 0; i < code.length(); i++) {
              char codeChar = code.charAt(i);
              char guessChar = guess.charAt(i);

              if (codeChar == guessChar) {
                guessCode.append("+");
              } else if (code.indexOf(guessChar) != -1) {
                guessCode.append("-");
              } else {
                guessCode.append("_");
              }
       }
       String guessResult = guessCode.toString();
       boolean isCorrect = guessResult.matches("\\+{" + code.length() + "}");

       return new Guess(guessResult, isCorrect);
    }
}
