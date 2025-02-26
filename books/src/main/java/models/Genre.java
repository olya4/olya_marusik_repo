package models;

import java.util.Objects;

public class Genre {

    private Integer genre_id;
    private String title;

    public Genre(Integer genre_id, String title) {
        this.genre_id = genre_id;
        this.title = title;
    }

    public Genre(String title) {
        this.title = title;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(genre_id, genre.genre_id) && Objects.equals(title, genre.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre_id, title);
    }

    @Override
    public String toString() {
        return "Жанр {" +
                +genre_id +
                ", категория '" + title + '\'' +
                '}';
    }
}
