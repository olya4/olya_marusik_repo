import models.Book;
import services.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyWindow extends JFrame {

    private Service service;

    public MyWindow(Service service) {

        this.service = service;

        // название окна
        setTitle("Книги");
        // границы окна
        setBounds(600, 300, 1300, 700);
        // когда окно закрыли, то программа завершилась
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // задать компановщика
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // создать вкладку
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        // список id авторов
        Integer[] arrayAuthorsId = new Integer[service.findAllAuthors().size()];
        for (int i = 0; i < service.findAllAuthors().size(); i++) {
            arrayAuthorsId[i] = service.findAllAuthors().get(i).getAuthor_id();
        }

        // создать выпадающий список и передать ему список id авторов
        JComboBox updateAuthorsIdCombo = new JComboBox(arrayAuthorsId);
        JComboBox removeAuthorsIdCombo = new JComboBox(arrayAuthorsId);
        JComboBox saveBookAuthorsIdCombo = new JComboBox(arrayAuthorsId);
        JComboBox updateBookAuthorsIdCombo = new JComboBox(arrayAuthorsId);
        JComboBox findBooksByAuthorIdCombo = new JComboBox(arrayAuthorsId);


        // список id жанров
        Integer[] arrayGenresId = new Integer[service.findAllGenres().size()];
        for (int i = 0; i < service.findAllGenres().size(); i++) {
            arrayGenresId[i] = service.findAllGenres().get(i).getGenre_id();
        }

        // создать выпадающий список и передать ему список id жанров
        JComboBox updateGenresIdCombo = new JComboBox(arrayGenresId);
        JComboBox removeGenresIdCombo = new JComboBox(arrayGenresId);
        JComboBox saveBookGenresIdCombo = new JComboBox(arrayGenresId);
        JComboBox updateBookGenresIdCombo = new JComboBox(arrayGenresId);
        JComboBox findBooksByGenreIdCombo = new JComboBox(arrayGenresId);


        // список id книг
        Integer[] arrayBooksId = new Integer[service.findAllBooks().size()];
        for (int i = 0; i < service.findAllBooks().size(); i++) {
            arrayBooksId[i] = service.findAllBooks().get(i).getBook_id();
        }

        // создать выпадающий список и передать ему список id книг
        JComboBox updateBooksIdCombo = new JComboBox(arrayBooksId);
        JComboBox listBooksIdCombo = new JComboBox(arrayBooksId);


        // Создать панель авторов
        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField surname1 = new JTextField(15);
        JTextField name1 = new JTextField(15);
        JTextField message1 = new JTextField(30);

        // Создать панель
        JPanel saveAuthorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        saveAuthorPanel.add(generateTitleField("ДОБАВИТЬ АВТОРА"));
        saveAuthorPanel.add(new JLabel("фамилия:"));
        saveAuthorPanel.add(formatField(surname1));
        saveAuthorPanel.add(new JLabel("имя:"));
        saveAuthorPanel.add(formatField(name1));

        // добавить панель на окошко
        add(saveAuthorPanel);
        // создать кнопку
        JButton saveAuthorButton = new JButton("SAVE");
        // действие, при нажатии на кнопку
        saveAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка на пустые поля
                if (surname1.getText().isEmpty() || name1.getText().isEmpty()) {
                    message1.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message1.getText());
                    return;
                }
                // получить значения полей
                String surname = surname1.getText();
                String name = name1.getText();
                // передать значения полей
                service.saveAuthor(surname, name);
                message1.setText("Добавлен новый автор " + surname + " " + name);
                JOptionPane.showMessageDialog(MyWindow.this,
                        message1.getText());
                // добавить новый id в выпадающий список
                updateAuthorsIdCombo.addItem(service.getLastIdAuthor());
                removeAuthorsIdCombo.addItem(service.getLastIdAuthor());
                saveBookAuthorsIdCombo.addItem(service.getLastIdAuthor());
                updateBookAuthorsIdCombo.addItem(service.getLastIdAuthor());
                findBooksByAuthorIdCombo.addItem(service.getLastIdAuthor());
            }
        });
        // добавить кнопку на панель
        saveAuthorPanel.add(saveAuthorButton);
        // добавить панель на панель авторов
        authorPanel.add(saveAuthorPanel);

        JTextField id2 = new JTextField(10);
        JTextField result2 = new JTextField(50);
        JTextField message2 = new JTextField(30);

        // нельзя изменить текст в этом поле
        result2.setEditable(false);
        // Создать панель
        JPanel findByIdAuthorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findByIdAuthorPanel.add(generateTitleField("НАЙТИ АВТОРА"));
        findByIdAuthorPanel.add(new JLabel("id:"));
        findByIdAuthorPanel.add(formatField(id2));
        findByIdAuthorPanel.add(new JLabel("результат:"));
        findByIdAuthorPanel.add(formatField(result2));
        // добавить панель на окошко
        add(findByIdAuthorPanel);
        // создать кнопку
        JButton findByIdAuthorButton = new JButton("FIND");
        // действие, при нажатии на кнопку
        findByIdAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка, что полученное значение число
                if (id2.getText().isEmpty()) {
                    message2.setText("Следует указать id автора");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message2.getText());
                    return;
                }

                try {
                    // преобразовать полученное значение в число
                    int id = Integer.parseInt(id2.getText());

                    // получить результат
                    String result = service.findAuthorById(id);

                    if (result == null) {
                        message2.setText("Автор не найден");
                        JOptionPane.showMessageDialog(MyWindow.this,
                                message2.getText());
                        return;
                    }
                    // передать результат полю окошка
                    result2.setText(result);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    message2.setText("Следует указать число");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message2.getText());
                }
            }
        });
        // добавить кнопку на панель
        findByIdAuthorPanel.add(findByIdAuthorButton);
        // добавить панель на панель авторов
        authorPanel.add(findByIdAuthorPanel);

        JTextField surname3 = new JTextField(15);
        JTextField name3 = new JTextField(15);
        JTextField message3 = new JTextField(30);
        // Создать панель
        JPanel updateAuthorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        updateAuthorPanel.add(generateTitleField("ИЗМЕНИТЬ АВТОРА"));
        updateAuthorPanel.add(new JLabel("id:"));
        // добавить выпадающий список
        updateAuthorPanel.add(updateAuthorsIdCombo);
        // добавить поля
        updateAuthorPanel.add(new JLabel("фамилия:"));
        updateAuthorPanel.add(formatField(surname3));
        updateAuthorPanel.add(new JLabel("имя:"));
        updateAuthorPanel.add(formatField(name3));

        // добавить панель на окошко
        add(updateAuthorPanel);
        // создать кнопку
        JButton updateAuthorButton = new JButton("UPDATE");
        // действие, при нажатии на кнопку
        updateAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) updateAuthorsIdCombo.getSelectedItem();
                // проверка на пустые поля
                if (surname3.getText().isEmpty() || name3.getText().isEmpty() || updateAuthorsIdCombo.getSelectedItem() == null) {
                    message3.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message3.getText());
                    return;
                }
                // получить значения полей
                String surname = surname3.getText();
                String name = name3.getText();
                // передать значения полей
                service.updateAuthor(id, surname, name);

                message3.setText("Автор успешно изменен");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message3.getText());
            }
        });
        // добавить кнопку на панель
        updateAuthorPanel.add(updateAuthorButton);
        // добавить панель на панель авторов
        authorPanel.add(updateAuthorPanel);

        JTextField result4 = new JTextField(50);
        JTextField message4 = new JTextField(30);
        // нельзя изменить текст в этом поле
        result4.setEditable(false);
        // Создать панель
        JPanel removeAuthorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        removeAuthorPanel.add(generateTitleField("УДАЛИТЬ АВТОРА"));
        removeAuthorPanel.add(new JLabel("id:"));
        // добавить выпадающий список
        removeAuthorPanel.add(removeAuthorsIdCombo);

        removeAuthorPanel.add(formatField(result4));
        // добавить панель на окошко
        add(removeAuthorPanel);
        // создать кнопку
        JButton removeAuthorButton = new JButton("DELETE");
        // действие, при нажатии на кнопку
        removeAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) removeAuthorsIdCombo.getSelectedItem();
                if (removeAuthorsIdCombo.getSelectedItem() == null) {
                    return;
                }
                // передать результат полю окошка
                result4.setText(service.findAuthorById(id));

                // найти все id книг удаляемого автора
                List<Integer> books = new ArrayList<>();
                for (int i = 0; i < service.findBooksByAuthorId(id).size(); i++) {
                    books.add(service.findBooksByAuthorId(id).get(i).getBook_id());
                }

                // удалить id книг из выпадающего списока
                for (int i = 0; i < books.size(); i++) {
                    updateBooksIdCombo.removeItem(books.get(i));
                    listBooksIdCombo.removeItem(books.get(i));
                }

                // передать значения полей
                service.removeAuthor(id);
                message4.setText("Автор успешно удален");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message4.getText());
                // удалить id из выпадающего списока
                updateAuthorsIdCombo.removeItem(id);
                removeAuthorsIdCombo.removeItem(id);
                saveBookAuthorsIdCombo.removeItem(id);
                updateBookAuthorsIdCombo.removeItem(id);
                findBooksByAuthorIdCombo.removeItem(id);
            }
        });
        // добавить кнопку на панель
        removeAuthorPanel.add(removeAuthorButton);
        // добавить панель на панель авторов
        authorPanel.add(removeAuthorPanel);

        JTextArea textArea5 = new JTextArea(6, 90);
        textArea5.setFont(new Font("Dialog", Font.PLAIN, 14));
        // Параметры переноса слов
        textArea5.setLineWrap(true);
        textArea5.setWrapStyleWord(true);
        // нельзя изменить текст в этом поле
        textArea5.setEditable(false);
        // Создать панель, которую можно прокручивать
        JScrollPane findAllAuthorsScrollPane = new JScrollPane(textArea5);
        // Создать панель
        JPanel findAllAuthorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findAllAuthorsPanel.add(generateTitleField("ВСЕ АВТОРЫ"));
        findAllAuthorsPanel.add(new JLabel("result:"));
        findAllAuthorsPanel.add(findAllAuthorsScrollPane);
        // добавить панель на окошко
        add(findAllAuthorsPanel);
        // создать кнопку
        JButton findAllAuthorsButton = new JButton("FIND ALL");
        // действие, при нажатии на кнопку
        findAllAuthorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // передать результат полю окошка
                textArea5.setText(service.findAllAuthors().toString());
            }
        });
        // добавить кнопку на панель
        findAllAuthorsPanel.add(findAllAuthorsButton);
        // добавить панель на панель авторов
        authorPanel.add(findAllAuthorsPanel);

        // добавить панель авторов на вкладку
        tabbedPane.add("Автор", authorPanel);

        getContentPane().add(tabbedPane);


        // Создать панель жанров
        JPanel genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField title6 = new JTextField(15);
        JTextField message6 = new JTextField(30);

        // Создать панель
        JPanel saveGenrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        saveGenrePanel.add(generateTitleField("ДОБАВИТЬ ЖАНР"));
        saveGenrePanel.add(new JLabel("название:"));
        saveGenrePanel.add(formatField(title6));

        // добавить панель на окошко
        add(saveGenrePanel);
        // создать кнопку
        JButton saveGenreButton = new JButton("SAVE");
        // действие, при нажатии на кнопку
        saveGenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка на пустые поля
                if (title6.getText().isEmpty()) {
                    message6.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message6.getText());
                    return;
                }

                // проверка на уникальность названия жанра
                for (int i = 0; i < service.getAllTitlesGenres().size(); i++) {
                    if (title6.getText().equals((service.getAllTitlesGenres().get(i)))) {
                        message6.setText("Такая категория жанра уже существует");
                        JOptionPane.showMessageDialog(MyWindow.this,
                                message6.getText());
                        return;
                    }
                }

                // получить значения полей
                String title = title6.getText();
                // передать значения полей
                service.saveGenre(title);
                message6.setText("Добавлен новый жанр " + title);
                JOptionPane.showMessageDialog(MyWindow.this,
                        message6.getText());
                // добавить новый id в выпадающий список
                updateGenresIdCombo.addItem(service.getLastIdGenre());
                removeGenresIdCombo.addItem(service.getLastIdGenre());
                saveBookGenresIdCombo.addItem(service.getLastIdGenre());
                updateBookGenresIdCombo.addItem(service.getLastIdGenre());
                findBooksByGenreIdCombo.addItem(service.getLastIdGenre());
            }
        });
        // добавить кнопку на панель
        saveGenrePanel.add(saveGenreButton);
        // добавить панель на панель жанров
        genrePanel.add(saveGenrePanel);

        JTextField id7 = new JTextField(10);
        JTextField result7 = new JTextField(50);
        JTextField message7 = new JTextField(30);

        // нельзя изменить текст в этом поле
        result7.setEditable(false);
        // Создать панель
        JPanel findByIdGenrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findByIdGenrePanel.add(generateTitleField("НАЙТИ ЖАНР"));
        findByIdGenrePanel.add(new JLabel("id:"));
        findByIdGenrePanel.add(formatField(id7));
        findByIdGenrePanel.add(new JLabel("результат:"));
        findByIdGenrePanel.add(formatField(result7));
        // добавить панель на окошко
        add(findByIdGenrePanel);
        // создать кнопку
        JButton findByIdGenreButton = new JButton("FIND");
        // действие, при нажатии на кнопку
        findByIdGenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка, что полученное значение число
                if (id7.getText().isEmpty()) {
                    message7.setText("Следует указать id жанра");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message7.getText());
                    return;
                }

                try {
                    // преобразовать полученное значение в число
                    int id = Integer.parseInt(id7.getText());

                    // получить результат
                    String result = service.findGenreById(id);

                    if (result == null) {
                        message7.setText("Жанр не найден");
                        JOptionPane.showMessageDialog(MyWindow.this,
                                message7.getText());
                        return;
                    }
                    // передать результат полю окошка
                    result7.setText(result);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    message7.setText("Следует указать число");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message7.getText());
                }
            }
        });
        // добавить кнопку на панель
        findByIdGenrePanel.add(findByIdGenreButton);
        // добавить панель на панель авторов
        genrePanel.add(findByIdGenrePanel);

        JTextField title8 = new JTextField(15);
        JTextField message8 = new JTextField(30);

        // Создать панель
        JPanel updateGenrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        updateGenrePanel.add(generateTitleField("ИЗМЕНИТЬ ЖАНР"));
        updateGenrePanel.add(new JLabel("id:"));
        // добавить выпадающий список
        updateGenrePanel.add(updateGenresIdCombo);
        // добавить поля
        updateGenrePanel.add(new JLabel("жанр:"));
        updateGenrePanel.add(formatField(title8));

        // добавить панель на окошко
        add(updateGenrePanel);
        // создать кнопку
        JButton updateGenreButton = new JButton("UPDATE");
        // действие, при нажатии на кнопку
        updateGenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) updateGenresIdCombo.getSelectedItem();
                // проверка на пустые поля
                if (title8.getText().isEmpty() || updateGenresIdCombo.getSelectedItem() == null) {
                    message8.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message8.getText());
                    return;
                }

                // проверка на уникальность названия жанра
                for (int i = 0; i < service.getAllTitlesGenres().size(); i++) {
                    if (title8.getText().equals((service.getAllTitlesGenres().get(i)))) {
                        message8.setText("Такая категория жанра уже существует");
                        JOptionPane.showMessageDialog(MyWindow.this,
                                message8.getText());
                        return;
                    }
                }

                // получить значения полей
                String title = title8.getText();
                // передать значения полей
                service.updateGenre(id, title);

                message8.setText("Жанр успешно изменен");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message8.getText());
            }
        });
        // добавить кнопку на панель
        updateGenrePanel.add(updateGenreButton);
        // добавить панель на панель жанров
        genrePanel.add(updateGenrePanel);

        JTextField result9 = new JTextField(50);
        JTextField message9 = new JTextField(30);
        // нельзя изменить текст в этом поле
        result9.setEditable(false);

        // Создать панель
        JPanel removeGenrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        removeGenrePanel.add(generateTitleField("УДАЛИТЬ ЖАНР"));
        removeGenrePanel.add(new JLabel("id:"));
        // добавить выпадающий список
        removeGenrePanel.add(removeGenresIdCombo);

        removeGenrePanel.add(formatField(result9));
        // добавить панель на окошко
        add(removeGenrePanel);
        // создать кнопку
        JButton removeGenreButton = new JButton("DELETE");
        // действие, при нажатии на кнопку
        removeGenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) removeGenresIdCombo.getSelectedItem();
                if (removeGenresIdCombo.getSelectedItem() == null) {
                    return;
                }
                // передать результат полю окошка
                result9.setText(service.findGenreById(id));
                // передать значения полей
                service.removeGenre(id);
                message9.setText("Жанр успешно удален");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message9.getText());
                // удалить id из выпадающего списока
                updateGenresIdCombo.removeItem(id);
                removeGenresIdCombo.removeItem(id);
                saveBookGenresIdCombo.removeItem(id);
                updateBookGenresIdCombo.removeItem(id);
                findBooksByGenreIdCombo.removeItem(id);
            }
        });
        // добавить кнопку на панель
        removeGenrePanel.add(removeGenreButton);
        // добавить панель на панель жанров
        genrePanel.add(removeGenrePanel);

        JTextArea textArea10 = new JTextArea(6, 90);
        textArea10.setFont(new Font("Dialog", Font.PLAIN, 14));
        // Параметры переноса слов
        textArea10.setLineWrap(true);
        textArea10.setWrapStyleWord(true);
        // нельзя изменить текст в этом поле
        textArea10.setEditable(false);
        // Создать панель, которую можно прокручивать
        JScrollPane findAllGenresScrollPane = new JScrollPane(textArea10);
        // Создать панель
        JPanel findAllGenresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findAllGenresPanel.add(generateTitleField("ВСЕ ЖАНРЫ"));
        findAllGenresPanel.add(new JLabel("result:"));
        findAllGenresPanel.add(findAllGenresScrollPane);
        // добавить панель на окошко
        add(findAllGenresPanel);
        // создать кнопку
        JButton findAllGenresButton = new JButton("FIND ALL");
        // действие, при нажатии на кнопку
        findAllGenresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // передать результат полю окошка
                textArea10.setText(service.findAllGenres().toString());
            }
        });
        // добавить кнопку на панель
        findAllGenresPanel.add(findAllGenresButton);
        // добавить панель на панель жанров
        genrePanel.add(findAllGenresPanel);

        // добавить панель жанров на вкладку
        tabbedPane.add("Жанр", genrePanel);

        getContentPane().add(tabbedPane);


        // Создать панель книг
        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField title11 = new JTextField(30);
        JTextField message11 = new JTextField(30);

        // Создать панель
        JPanel saveBookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        saveBookPanel.add(generateTitleField("ДОБАВИТЬ КНИГУ"));
        saveBookPanel.add(new JLabel("название:"));
        saveBookPanel.add(formatField(title11));
        saveBookPanel.add(new JLabel("автор:"));
        // добавить выпадающий список авторов
        saveBookPanel.add(saveBookAuthorsIdCombo);
        saveBookPanel.add(new JLabel("жанр:"));
        // добавить выпадающий список жанров
        saveBookPanel.add(saveBookGenresIdCombo);

        // добавить панель на окошко
        add(saveBookPanel);
        // создать кнопку
        JButton saveBookButton = new JButton("SAVE");
        // действие, при нажатии на кнопку
        saveBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка на пустые поля
                if (title11.getText().isEmpty() || saveBookAuthorsIdCombo.getSelectedItem() == null || saveBookGenresIdCombo.getSelectedItem() == null) {
                    message11.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message11.getText());
                    return;
                }
                // получить значения полей
                String title = title11.getText();
                int author_id = (int) saveBookAuthorsIdCombo.getSelectedItem();
                int gender_id = (int) saveBookGenresIdCombo.getSelectedItem();
                // передать значения полей
                service.saveBook(title, author_id, gender_id);
                message11.setText("Добавлена новая книга " + title + " автора " + author_id + " в жанре " + gender_id);
                JOptionPane.showMessageDialog(MyWindow.this,
                        message11.getText());
                // добавить новый id в выпадающий список
                updateBooksIdCombo.addItem(service.getLastIdBook());
                listBooksIdCombo.addItem(service.getLastIdBook());
            }
        });
        // добавить кнопку на панель
        saveBookPanel.add(saveBookButton);
        // добавить панель на панель книг
        bookPanel.add(saveBookPanel);

        JTextField id12 = new JTextField(10);
        JTextField result12 = new JTextField(50);
        JTextField message12 = new JTextField(30);

        // нельзя изменить текст в этом поле
        result12.setEditable(false);
        // Создать панель
        JPanel findByIdBookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findByIdBookPanel.add(generateTitleField("НАЙТИ КНИГУ"));
        findByIdBookPanel.add(new JLabel("id:"));
        findByIdBookPanel.add(formatField(id12));
        findByIdBookPanel.add(new JLabel("результат:"));
        findByIdBookPanel.add(formatField(result12));
        // добавить панель на окошко
        add(findByIdBookPanel);
        // создать кнопку
        JButton findByIdBookButton = new JButton("FIND");
        // действие, при нажатии на кнопку
        findByIdBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверка, что полученное значение число
                if (id12.getText().isEmpty()) {
                    message12.setText("Следует указать id книги");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message12.getText());
                    return;
                }

                try {
                    // преобразовать полученное значение в число
                    int id = Integer.parseInt(id12.getText());

                    // получить результат
                    String result = service.findBookById(id);

                    if (result == null) {
                        message12.setText("Книга не найдена");
                        JOptionPane.showMessageDialog(MyWindow.this,
                                message12.getText());
                        return;
                    }
                    // передать результат полю окошка
                    result12.setText(result);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    message12.setText("Следует указать число");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message12.getText());
                }
            }
        });
        // добавить кнопку на панель
        findByIdBookPanel.add(findByIdBookButton);
        // добавить панель на панель авторов
        bookPanel.add(findByIdBookPanel);

        JTextField title13 = new JTextField(30);
        JTextField message13 = new JTextField(30);
        // Создать панель
        JPanel updateBookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        updateBookPanel.add(generateTitleField("ИЗМЕНИТЬ КНИГУ"));
        updateBookPanel.add(new JLabel("id:"));
        // добавить выпадающий список
        updateBookPanel.add(updateBooksIdCombo);
        // добавить поля
        updateBookPanel.add(new JLabel("название:"));
        updateBookPanel.add(formatField(title13));
        updateBookPanel.add(new JLabel("автор:"));
        // добавить выпадающий список авторов
        updateBookPanel.add(updateBookAuthorsIdCombo);
        updateBookPanel.add(new JLabel("жанр:"));
        // добавить выпадающий список жанров
        updateBookPanel.add(updateBookGenresIdCombo);

        // добавить панель на окошко
        add(updateBookPanel);
        // создать кнопку
        JButton updateBookButton = new JButton("UPDATE");
        // действие, при нажатии на кнопку
        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) updateBooksIdCombo.getSelectedItem();

                // проверка на пустые поля
                if (title13.getText().isEmpty() || updateBookAuthorsIdCombo.getSelectedItem() == null
                        || updateBookGenresIdCombo.getSelectedItem() == null) {
                    message13.setText("Следует заполнить все поля");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message13.getText());
                    return;
                }
                // получить значения полей
                String title = title13.getText();
                int author_id = (int) updateBookAuthorsIdCombo.getSelectedItem();
                int gender_id = (int) updateBookGenresIdCombo.getSelectedItem();
                // передать значения полей
                service.updateBook(id, title, author_id, gender_id);
                message13.setText("Книга успешно изменена");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message13.getText());
            }
        });
        // добавить кнопку на панель
        updateBookPanel.add(updateBookButton);
        // добавить панель на панель авторов
        bookPanel.add(updateBookPanel);

        JTextField result14 = new JTextField(50);
        JTextField message14 = new JTextField(30);
        // нельзя изменить текст в этом поле
        result14.setEditable(false);
        // Создать панель
        JPanel removeBookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        removeBookPanel.add(generateTitleField("УДАЛИТЬ КНИГУ"));
        removeBookPanel.add(new JLabel("id:"));
        // добавить выпадающий список
        removeBookPanel.add(listBooksIdCombo);

        removeBookPanel.add(formatField(result14));
        // добавить панель на окошко
        add(removeBookPanel);
        // создать кнопку
        JButton removeBookButton = new JButton("DELETE");
        // действие, при нажатии на кнопку
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) listBooksIdCombo.getSelectedItem();
                if (listBooksIdCombo.getSelectedItem() == null) {
                    return;
                }
                // передать результат полю окошка
                result14.setText(service.findBookById(id));
                // передать значения полей
                service.removeBook(id);
                message14.setText("Книга успешно удалена");
                JOptionPane.showMessageDialog(MyWindow.this,
                        message14.getText());
                // удалить id из выпадающего списока
                updateBooksIdCombo.removeItem(id);
                listBooksIdCombo.removeItem(id);
            }
        });
        // добавить кнопку на панель
        removeBookPanel.add(removeBookButton);
        // добавить панель на панель авторов
        bookPanel.add(removeBookPanel);

        JTextArea textArea15 = new JTextArea(6, 90);
        textArea15.setFont(new Font("Dialog", Font.PLAIN, 14));
        // Параметры переноса слов
        textArea15.setLineWrap(true);
        textArea15.setWrapStyleWord(true);
        // нельзя изменить текст в этом поле
        textArea15.setEditable(false);
        // Создать панель, которую можно прокручивать
        JScrollPane findAllBooksScrollPane = new JScrollPane(textArea15);
        // Создать панель
        JPanel findAllBooksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findAllBooksPanel.add(generateTitleField("ВСЕ КНИГИ"));
        findAllBooksPanel.add(new JLabel("result:"));
        findAllBooksPanel.add(findAllBooksScrollPane);
        // добавить панель на окошко
        add(findAllBooksPanel);
        // создать кнопку
        JButton findAllBooksButton = new JButton("FIND ALL");
        // действие, при нажатии на кнопку
        findAllBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // передать результат полю окошка
                textArea15.setText(service.findAllBooks().toString());
            }
        });
        // добавить кнопку на панель
        findAllBooksPanel.add(findAllBooksButton);
        // добавить панель на панель авторов
        bookPanel.add(findAllBooksPanel);

        JTextField result16 = new JTextField(16);
        JTextField result_count_16 = new JTextField(15);
        // нельзя изменить текст в этом поле
        result16.setEditable(false);
        result_count_16.setEditable(false);
        JTextField message16 = new JTextField(30);
        JTextArea textArea16 = new JTextArea(6, 40);
        textArea16.setFont(new Font("Dialog", Font.PLAIN, 14));
        // Параметры переноса слов
        textArea16.setLineWrap(true);
        textArea16.setWrapStyleWord(true);
        // нельзя изменить текст в этом поле
        textArea16.setEditable(false);
        // Создать панель, которую можно прокручивать
        JScrollPane findBooksByAuthorIdPane = new JScrollPane(textArea16);
        // Создать панель
        JPanel findBooksByAuthorIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findBooksByAuthorIdPanel.add(generateTitleField("ПО АВТОРУ"));
        findBooksByAuthorIdPanel.add(new JLabel("id автора:"));
        // добавить выпадающий список
        findBooksByAuthorIdPanel.add(findBooksByAuthorIdCombo);
        findBooksByAuthorIdPanel.add(formatField(result16));
        findBooksByAuthorIdPanel.add(formatField(result_count_16));
        findBooksByAuthorIdPanel.add(new JLabel("result:"));
        findBooksByAuthorIdPanel.add(findBooksByAuthorIdPane);
        // добавить панель на окошко
        add(findBooksByAuthorIdPanel);
        // создать кнопку
        JButton findBooksByAuthorIdButton = new JButton("FIND BOOKS");
        // действие, при нажатии на кнопку
        findBooksByAuthorIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) findBooksByAuthorIdCombo.getSelectedItem();

                if (findBooksByAuthorIdCombo.getSelectedItem() == null) {
                    return;
                }

                if (service.findBooksByAuthorId(id).isEmpty()) {
                    message16.setText("У этого автора нет книг");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message16.getText());
                    return;
                }

                result16.setText(service.findAuthorById(id));
                result_count_16.setText("У автора " + service.getCountBooksByAuthorId(id) + " книг");
                // передать результат полю окошка
                textArea16.setText(service.findBooksByAuthorId(id).toString());
            }
        });
        // добавить кнопку на панель
        findBooksByAuthorIdPanel.add(findBooksByAuthorIdButton);
        // добавить панель на панель книг
        bookPanel.add(findBooksByAuthorIdPanel);

        JTextField result17 = new JTextField(16);
        JTextField result_count_17 = new JTextField(15);
        // нельзя изменить текст в этом поле
        result17.setEditable(false);
        result_count_17.setEditable(false);
        JTextField message17 = new JTextField(30);
        JTextArea textArea17 = new JTextArea(6, 40);
        textArea17.setFont(new Font("Dialog", Font.PLAIN, 14));
        // Параметры переноса слов
        textArea17.setLineWrap(true);
        textArea17.setWrapStyleWord(true);
        // нельзя изменить текст в этом поле
        textArea17.setEditable(false);
        // Создать панель, которую можно прокручивать
        JScrollPane findBooksByGenreIdPane = new JScrollPane(textArea17);
        // Создать панель
        JPanel findBooksByGenreIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // добавить поля на панель
        findBooksByGenreIdPanel.add(generateTitleField("ПО ЖАНРУ"));
        findBooksByGenreIdPanel.add(new JLabel(" id жанра:"));
        // добавить выпадающий список
        findBooksByGenreIdPanel.add(findBooksByGenreIdCombo);
        findBooksByGenreIdPanel.add(formatField(result17));
        findBooksByGenreIdPanel.add(formatField(result_count_17));
        findBooksByGenreIdPanel.add(new JLabel("result:"));
        findBooksByGenreIdPanel.add(findBooksByGenreIdPane);
        // добавить панель на окошко
        add(findBooksByGenreIdPanel);
        // создать кнопку
        JButton findBooksByGenreIdButton = new JButton("FIND BOOKS");
        // действие, при нажатии на кнопку
        findBooksByGenreIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить выбранное значение из списка
                int id = (int) findBooksByGenreIdCombo.getSelectedItem();

                if (findBooksByGenreIdCombo.getSelectedItem() == null) {
                    return;
                }

                if (service.findBooksByGenreId(id).isEmpty()) {
                    message17.setText("Нет книг этого жанра");
                    JOptionPane.showMessageDialog(MyWindow.this,
                            message17.getText());
                    return;
                }

                result17.setText(service.findGenreById(id));
                result_count_17.setText("В этом жанре " + service.getCountBooksByGenreId(id) + " книг");
                // передать результат полю окошка
                textArea17.setText(service.findBooksByGenreId(id).toString());
            }
        });
        // добавить кнопку на панель
        findBooksByGenreIdPanel.add(findBooksByGenreIdButton);
        // добавить панель на панель книг
        bookPanel.add(findBooksByGenreIdPanel);

        // добавить панель книги на вкладку
        tabbedPane.add("Книги", bookPanel);

        getContentPane().add(tabbedPane);


        // видимость окна
        setVisible(true);

    }

    private JTextField formatField(JTextField textField) {
        // шрифт: формат, стиль, размер
        Font font = new Font("Arial", Font.PLAIN, 18);
        textField.setFont(font);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        return textField;
    }

    private JTextField generateTitleField(String str) {
        // шрифт: формат, стиль, размер
        Font font = new Font("Arial", Font.ITALIC, 20);
        // создать поле
        JTextField textField = new JTextField();
        // этот текст будет выведен при запуске программы
        textField.setText(str);
        // нельзя изменить текст в этом поле
        textField.setEditable(false);
        // разместить текст по центру формы (горизонтальное выравнивание)
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        return textField;
    }
}


