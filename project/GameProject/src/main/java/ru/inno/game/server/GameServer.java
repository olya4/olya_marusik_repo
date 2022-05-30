package ru.inno.game.server;

import ru.inno.game.services.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static ru.inno.game.server.CommandsParser.*;

// сервер - отвечает за подключение игроков по протоколу socket
public class GameServer {
    // могут подключиться два игрока (два потока)

    // отдельный поток (Thread - поток исполнения) для первого игрока, параллельный main
    private ClientThread firstPlayer;
    // отдельный поток (Thread - поток исполнения) для второго игрока, параллельный main
    private ClientThread secondPlayer;
    // объект для socket-сервера
    private ServerSocket serverSocket;
    // флаг, определяет, началась игра или нет
    private boolean isGameStarted = false;
    // время начала игры в миллисекундах
    private long startTimesMills;
    // игра в процессе
    private boolean isGameInProcess = true;
    // идентификатор игры
    private long gameId;
    // объект бизнес-логики игры
    private GameService gameService;
    // объект синхронизации - mutex
    private Lock lock = new ReentrantLock();

    public GameServer(GameService gameService) {
        this.gameService = gameService;
    }

    // метод запуска сервера на определенном порту
    public void start(int port) {
        try {
            // запустить SocketServer на определнном порту
            serverSocket = new ServerSocket(port);
            System.out.println("СЕРВЕР ЗАПУЩЕН...");
            System.out.println("ОЖИДАНИЕ ПОДКЛЮЧЕНИЯ ПЕВОГО КЛИЕНТА...");
            // если первый игрок не подключен - ждать, пока не подключится
            firstPlayer = connect();
            System.out.println("ОЖИДАНИЕ ПОДКЛЮЧЕНИЯ ВТОРОГО КЛИЕНТА...");
            // если первый игрок подключен - ждать, пока подключится второй
            secondPlayer = connect();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // служебный метод - ждет подключения клиента
    // возвращает объект ClientThread, который представляет собой отдельный поток,
    // где происходит общение с клиентом со стороны сервера
    private ClientThread connect() {
        // получить клиента
        Socket client;
        try {
            // уводит приложение в ожидание, пока не присоединиться какой-либо клиент
            // как только клиент подключен к серверу
            // объект-соединение возвращается, как результат выполнения метода
            client = serverSocket.accept();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        // создать socket-клиенту отдельный поток для клиента
        ClientThread clientThread = new ClientThread(client);
        // запустить этот отдельный бесконечный поток
        clientThread.start();
        System.out.println("КЛИЕНТ ПОДКЛЮЧЕН...");
        // отправить сообщение клиенту о подключении
        clientThread.sendMessage("Вы подключены к серверу");
        // возвращается поток
        return clientThread;
    }

    // отдельный поток на сервере для общения с клиентом
    private class ClientThread extends Thread {

        // поток(stream - поток char) символов, который можно направлять клиенту
        private final PrintWriter toClient;
        // поток (stream - поток char)символов, который приходит от клиента
        private final BufferedReader fromClient;

        // когда клиент подключился запросить nickname
        private String playerNickname;

        // когда клиент подключился запросить ip
        private String ip;

        public String getIp() {
            return ip;
        }

        public ClientThread(Socket client) {
            try {
                // получение потоков для чтения и записи

                // autoFlush - чтобы сразу отправлял данные в поток
                // а не ждал, пока принудительно вызовут flush

                // оборачиваются байтовые потоки символьными
                this.toClient = new PrintWriter(client.getOutputStream(), true);
                this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.ip = client.getInetAddress().getHostAddress();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // работает в отдельном потоке на протяжении всей программы
        // ожидает сообщение от клиента
        @Override
        public void run() {
            // бесконечный цикл (работает, пока игра запущена и клиент подключен)
            while (isGameInProcess) {
                // логика печати сообщений от клиента
                // сервер в отдельном потоке ждет сообщение от клиента
                String messageFromClient;
                try {
                    // получено сообщение от клиента
                    messageFromClient = fromClient.readLine();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
                if (messageFromClient != null) {
                    // если сообщение от клиента начинается с префиксом name
                    if (isMessageFromNickname(messageFromClient)) {
                        // метод для получения nickname
                        resolveNickname(messageFromClient);
                    } else if (isMessageForExit(messageFromClient) && isGameInProcess) {
                        // только один поток может завершить в конкретный момент времени игру
                        lock.lock();

                        // gameService.finishGame(gameId, (System.currentTimeMillis() - startTimesMills)/1000);

                        gameService.finishGame(gameId);
                        isGameInProcess = false;
                        lock.unlock();
                    } else if (isMessageForMove(messageFromClient)) {
                        // сообщить игроку куда походил противник
                        resolveMove(messageFromClient);
                    } else if (isMessageForShot(messageFromClient)) {
                        // сообщить игроку, что противник сделал выстрел
                        resolveShot(messageFromClient);
                    } else if (isMessageForDamage(messageFromClient)) {
                        // сообщить о нанесенном уроне
                        resolveDamage();
                    }
                    // чтобы игра началась только одним потоком
                    // только один поток может выполнить проверку
                    lock.lock();
                    // если игра еще не началась, и у игроков указаны nickname
                    if (isReadyForStartGame()) {
                        // когда игроки подключились - запустить игру
                        // вызывается метод бизнес-логики для начала игры
                        gameId = gameService.startGame(firstPlayer.getIp(), secondPlayer.getIp(), firstPlayer.playerNickname, secondPlayer.playerNickname);
                        // время начала игры
                        startTimesMills = System.currentTimeMillis();
                        // установить флаг - игра началась
                        isGameStarted = true;
                    }
                    lock.unlock();
                }
            }
        }

        private void resolveDamage() {
            if (meFirst()) {
                gameService.shot(gameId, firstPlayer.playerNickname, secondPlayer.playerNickname);
            } else {
                gameService.shot(gameId, secondPlayer.playerNickname, firstPlayer.playerNickname);
            }
        }

        private void resolveShot(String messageFromClient) {
            if (meFirst()) {
                secondPlayer.sendMessage(messageFromClient);
            } else {
                firstPlayer.sendMessage(messageFromClient);
            }
        }

        private void resolveMove(String messageFromClient) {
            if (meFirst()) {
                secondPlayer.sendMessage(messageFromClient);
            } else {
                firstPlayer.sendMessage(messageFromClient);
            }
        }

        private boolean isReadyForStartGame() {
            return firstPlayer.playerNickname != null && secondPlayer.playerNickname != null && !isGameStarted;
        }

        // метод для получения nickname
        private void resolveNickname(String messageFromClient) {
            // если это первый игрок
            if (meFirst()) {
                fixNickname(messageFromClient, firstPlayer, "ИМЯ ПЕРВОГО ИГРОКА: ", secondPlayer);
            } else {
                // то это nickname для второго игрока (вырезать имя после 6 символов)
                fixNickname(messageFromClient, secondPlayer, "ИМЯ ВТОРОГО ИГРОКА: ", firstPlayer);
            }
        }

        private void fixNickname(String nickname, ClientThread currentPlayer, String anotherMessagePrefix, ClientThread anotherPlayer) {
            // то это nickname для первого игрока (вырезать имя после 6 символов)
            currentPlayer.playerNickname = nickname.substring(6);
            System.out.println(anotherMessagePrefix + nickname);
            // отправить полученное сообщение второму игроку
            anotherPlayer.sendMessage(nickname);
        }

        public void sendMessage(String message) {
            toClient.println(message);
        }

        private boolean meFirst() {
            // проверка: объект потока является первым игроком
            return this == firstPlayer;
        }
    }
}
