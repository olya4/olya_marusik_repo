package models;

import java.util.Objects;

public class Author {

    private Integer author_id;
    private String surname;
    private String name;

    public Author(Integer author_id, String surname, String name) {
        this.author_id = author_id;
        this.surname = surname;
        this.name = name;
    }

    public Author(String surname, String name) {
        this.surname = surname;
        this.name = name;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(author_id, author.author_id) && Objects.equals(surname, author.surname) && Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author_id, surname, name);
    }

    @Override
    public String toString() {
        return "Автор {" +
                +author_id +
                ", фамилия = '" + surname + '\'' +
                ", имя = '" + name + '\'' +
                '}';
    }
}
