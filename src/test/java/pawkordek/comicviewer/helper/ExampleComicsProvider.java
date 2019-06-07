package pawkordek.comicviewer.helper;

import pawkordek.comicviewer.model.Author;
import pawkordek.comicviewer.model.AuthorRole;
import pawkordek.comicviewer.model.Comic;
import pawkordek.comicviewer.model.Tag;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ExampleComicsProvider {
    public final static AuthorRole authorRole1 = AuthorRole.builder()
            .id(1)
            .name("Writer")
            .build();

    public final static AuthorRole authorRole2 = AuthorRole.builder()
            .id(2)
            .name("Artist")
            .build();

    public final static Author author1 = Author.builder()
            .id(1)
            .firstName("Janusz")
            .lastName("Christa")
            .roles(asList(authorRole1, authorRole2))
            .build();

    public final static Author author2 = Author.builder()
            .id(2)
            .firstName("Henryk")
            .middleName("Jerzy")
            .lastName("Chmielewski")
            .roles(asList(authorRole1, authorRole2))
            .build();

    public final static Author author3 = Author.builder()
            .id(3)
            .firstName("René")
            .lastName("Goscinny")
            .roles(singletonList(authorRole1))
            .build();

    public final static Author author4 = Author.builder()
            .id(4)
            .firstName("Albert")
            .lastName("Uderzo")
            .roles(singletonList(authorRole2))
            .build();

    public final static Tag tag1 = Tag.builder()
            .id(1)
            .name("Polish")
            .build();

    public final static Tag tag2 = Tag.builder()
            .id(2)
            .name("French")
            .build();

    public final static Comic comic1 = Comic.builder()
            .id(1)
            .title("Kajko i Kokosz")
            .path("kajko_kokosz")
            .authors(singletonList(author1))
            .tags(singletonList(tag1))
            .build();

    public final static Comic comic2 = Comic.builder()
            .id(2)
            .title("Tytus, Romek i Atomek")
            .path("tytus_romek_atomek")
            .authors(singletonList(author2))
            .tags(singletonList(tag1))
            .build();

    public final static Comic comic3 = Comic.builder()
            .id(3)
            .title("Asterix")
            .path("asterix")
            .authors(asList(author3, author4))
            .tags(singletonList(tag2))
            .build();
}