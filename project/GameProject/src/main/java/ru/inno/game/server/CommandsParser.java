package ru.inno.game.server;

public class CommandsParser {

    public static boolean isMessageForDamage(String messageFromClient) {
        return messageFromClient.equals("DAMAGE");
    }

    public static boolean isMessageForShot(String messageFromClient) {
        return messageFromClient.equals("shot");
    }

    public static boolean isMessageForExit(String messageFromClient) {
        return messageFromClient.equals("exit");
    }

    public static boolean isMessageFromNickname(String messageFromClient) {
        return messageFromClient.startsWith("name: ");
    }

    // произошло движение шарика у клиента
    public static boolean isMessageForMove(String messageFromClient) {
        return messageFromClient.equals("left") || messageFromClient.equals("right");
    }

}
