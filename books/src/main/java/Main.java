import repositories.AuthorsRepository;
import repositories.BooksRepository;
import repositories.GenresRepository;
import services.Service;
import utils.CustomDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    // путь к файлу
    public static final String PATH_TO_PROPERTIES = "src/main/resources/jdbc.properties";

    public static void main(String[] args) {

        FileInputStream fileInputStream;
        Properties properties = new Properties();

        try {
            // открыть файл
            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
            properties.load(fileInputStream);
            // получить значения из файла
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            // установить соединение
            DataSource dataSource = new CustomDataSource(url, username, password);
            System.out.println("Соединение с БД установлено");

            System.out.println();

            AuthorsRepository authorsRepository = new AuthorsRepository(dataSource);
            GenresRepository genresRepository = new GenresRepository(dataSource);
            BooksRepository booksRepository = new BooksRepository(dataSource);
            Service service = new Service(authorsRepository, genresRepository, booksRepository);

            new MyWindow(service);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
