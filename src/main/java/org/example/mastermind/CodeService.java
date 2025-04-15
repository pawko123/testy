package org.example.mastermind;

public interface CodeService {
    Guess checkGuess(String code,String guess) throws CodeCheckException;
    String generateCode(int length) throws CodeCheckException;
}
