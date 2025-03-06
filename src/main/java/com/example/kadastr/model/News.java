package com.example.kadastr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID uuid;
    private String title;
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime lastEditDate;
    private UUID insertedById;
    private UUID updatedById;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (!Objects.equals(uuid, news.uuid)) return false;
        if (!Objects.equals(title, news.title)) return false;
        if (!Objects.equals(text, news.text)) return false;
        if (!Objects.equals(creationDate, news.creationDate)) return false;
        if (!Objects.equals(lastEditDate, news.lastEditDate)) return false;
        if (!Objects.equals(insertedById, news.insertedById)) return false;
        return Objects.equals(updatedById, news.updatedById);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
        result = 31 * result + (insertedById != null ? insertedById.hashCode() : 0);
        result = 31 * result + (updatedById != null ? updatedById.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "News{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", creationDate=" + creationDate +
                ", lastEditDate=" + lastEditDate +
                ", insertedById=" + insertedById +
                ", updatedById=" + updatedById +
                '}';
    }
}
