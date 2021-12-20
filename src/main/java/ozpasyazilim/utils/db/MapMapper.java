package ozpasyazilim.utils.db;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> map(ResultSet r, StatementContext ctx) throws SQLException {

        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= r.getMetaData().getColumnCount(); i++) {
            map.put(r.getMetaData().getColumnLabel(i), r.getObject(i));
        }
        return map;
    }

}