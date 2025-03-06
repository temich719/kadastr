package com.example.kadastr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID uuid;
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime lastEditDate;
    private UUID insertedById;
    @Column(insertable = false, updatable = false)
    private UUID idNews;

    @ManyToOne
    @JoinColumn(name = "idNews", referencedColumnName = "id", nullable = false)
    private News news;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!Objects.equals(uuid, comment.uuid)) return false;
        if (!Objects.equals(text, comment.text)) return false;
        if (!Objects.equals(creationDate, comment.creationDate))
            return false;
        if (!Objects.equals(lastEditDate, comment.lastEditDate))
            return false;
        if (!Objects.equals(insertedById, comment.insertedById))
            return false;
        return Objects.equals(idNews, comment.idNews);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
        result = 31 * result + (insertedById != null ? insertedById.hashCode() : 0);
        result = 31 * result + (idNews != null ? idNews.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "uuid=" + uuid +
                ", text='" + text + '\'' +
                ", creationDate=" + creationDate +
                ", lastEditDate=" + lastEditDate +
                ", insertedById=" + insertedById +
                ", idNews=" + idNews +
                '}';
    }
}
