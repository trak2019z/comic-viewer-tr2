package pawkordek.comicviewer.dao;

import lombok.NonNull;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import pawkordek.comicviewer.model.Comic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Repository
public class ComicDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ResultSetExtractor<List<Comic>> comicsExtractor =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .newResultSetExtractor(Comic.class);

    private final static Comic EMPTY_COMIC = Comic.builder()
            .id(0)
            .title("")
            .path("")
            .authors(Collections.emptyList())
            .tags(Collections.emptyList())
            .build();

    private final String SELECT_ALL_COMIC_DATA_QUERY =
            "       SELECT " +
                    "   c.id , c.title, c.path, " +
                    "   a.id AS authors_id, a.first_name AS authors_first_name, " +
                    "   a.middle_name AS authors_middle_name, a.last_name AS authors_last_name, " +
                    "   r.id AS authors_roles_id, r.name AS authors_roles_name, " +
                    "   t.id AS tags_id, t.name AS tags_name " +
                    "FROM " +
                    "   comics AS c " +
                    "   INNER JOIN comic_author AS ca ON c.id = ca.comic_id " +
                    "   INNER JOIN authors AS a ON ca.author_id = a.id " +
                    "   INNER JOIN author_author_role AS ar ON a.id = ar.author_id " +
                    "   INNER JOIN author_roles AS r ON ar.author_role_id = r.id " +
                    "   INNER JOIN comic_tag AS ct ON c.id = ct.comic_id " +
                    "   INNER JOIN tags AS t ON t.id = ct.tag_id ";

    public List<Comic> getAll() {
        return jdbcTemplate.query(
                SELECT_ALL_COMIC_DATA_QUERY,
                comicsExtractor);
    }


    public Comic getComic(int id) {
        List<Comic> comics = jdbcTemplate.query(
                SELECT_ALL_COMIC_DATA_QUERY +
                        "WHERE " +
                        "   c.id = " + id,
                comicsExtractor);
        if (comics != null) {
            return comics.get(0);
        } else {
            return EMPTY_COMIC;
        }
    }

    public List<Comic> getAllWithAttributes(Map<String, Object> attributes) {
        StringBuilder queryBuilder = new StringBuilder(
                SELECT_ALL_COMIC_DATA_QUERY +
                        "WHERE ");
        attributes.keySet().forEach((key) -> {
            String attributeSQL = getSQLForAttribute(key);
            queryBuilder.append(attributeSQL);
        });
        return jdbcTemplate.query(
                queryBuilder.toString(),
                new ComicPreparedStatementSetter(attributes),
                comicsExtractor);
    }

    private String getSQLForAttribute(String attributeName) {
        switch (attributeName) {
            case "title":
                return "    LOWER(c.title) like LOWER(?) ";
        }
        return "";
    }

    private class ComicPreparedStatementSetter implements PreparedStatementSetter {
        @NonNull
        Map<String, Object> attributes;

        @NonNull
        private PreparedStatement preparedStatement;

        private int currentAttributeNr = 1;

        ComicPreparedStatementSetter(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public void setValues(PreparedStatement ps) {
            preparedStatement = ps;
            attributes.forEach((attributeName, attributeValue) -> {
                try {
                    setAttributeValueInPreparedStatement(attributeName, attributeValue);
                    currentAttributeNr++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        private void setAttributeValueInPreparedStatement(String attributeName, Object attributeValue) throws SQLException {
            switch (attributeName) {
                case "title":
                    preparedStatement.setString(currentAttributeNr, "%"+ attributeValue.toString() + "%");
            }
        }
    }


}
