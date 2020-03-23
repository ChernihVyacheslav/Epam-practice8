package ua.nure.chernykh.practice8.db;

import ua.nure.chernykh.practice8.db.entity.Team;
import ua.nure.chernykh.practice8.db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String CONNECTION_URL =
            "jdbc:mysql://localhost/practice8db_1?"
                    + "user=practice8user&password=practice8pass";

//    private static final String CONNECTION_URL =
//            "jdbc:mysql://practice8user:practice8pass@localhost:3306/practice8db_1";

    private static final String SQL_FIND_ALL_USERS =
            "SELECT * FROM users ORDER BY ID";

    private static final String SQL_FIND_USER_BY_LOGIN =
            "SELECT * FROM users WHERE login=?";

    private static final String SQL_INSERT_USER =
            "INSERT INTO users (login) VALUES (?)";

    private static final String SQL_FIND_ALL_TEAMS =
            "SELECT * FROM teams";

    private static final String SQL_FIND_TEAM_BY_NAME =
            "SELECT * FROM teams WHERE name=?";

    private static final String SQL_INSERT_TEAM =
            "INSERT INTO teams (name) VALUES (?)";

    private static final String SQL_SET_TEAM_FOR_USER =
            "INSERT INTO users_teams (user_id, team_id) VALUES (?, ?)";

    private static final int VALUE_INDEX = 1;
//    private static final String SQL_FIND_USER_TEAMS = "SELECT teams.id, teams.name FROM teams, users_teams " +
//            "WHERE users_teams.user_id = ? " +
//            "AND users_teams.team_id = teams.id";
    private static final String SQL_FIND_USER_TEAMS = "SELECT teams.id, teams.name FROM teams " +
            "INNER JOIN users_teams " +
            "ON users_teams.user_id = ? AND users_teams.team_id = teams.id";

    //////////////////////////////////

    private static DBManager instance;

    private DBManager() {
        // ...
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CONNECTION_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // adjust your connection
        return con;
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public boolean insertUser(User user) throws SQLException {
        Connection con = getConnection();
        boolean result = insertUser(con, user);
        DBUtils.close(con);
        return result;
    }

    private boolean insertUser(Connection con, User user) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(VALUE_INDEX, user.getLogin());

            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(VALUE_INDEX);
                    user.setId(id);
                }
                return true;
            }
        } catch (SQLException ex) {
            // (1) log this exception
            System.err.println("Cannot insert a new user");
            // (2)
            throw new SQLException("Cannot insert a new user", ex);
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return false;
    }

    public boolean insertTeam(Team team) throws SQLException {
        Connection con = getConnection();
        boolean result = insertTeam(con, team);
        DBUtils.close(con);
        return result;
    }

    private boolean insertTeam(Connection con, Team team) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(SQL_INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(VALUE_INDEX, team.getName());

            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(VALUE_INDEX);
                    team.setId(id);
                }
                return true;
            }
        } catch (SQLException ex) {
            // (1) log this exception
            System.err.println("Cannot insert a new team");
            // (2)
            throw new SQLException("Cannot insert a new team", ex);
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return false;
    }

    public List<User> findAllUsers() throws SQLException {
        Connection con = getConnection();
        List<User> result = findAllUsers(con);
        DBUtils.close(con);
        return result;
    }

    private List<User> findAllUsers(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_USERS);
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException ex) {
            // (1) log this exception
            System.err.println("Cannot find all users");
            // (2)
            throw new SQLException("Cannot find all users", ex);
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (stmt != null) {
                DBUtils.close(stmt);
            }
        }
        return users;
    }

    public List<Team> findAllTeams() throws SQLException {
        Connection con = getConnection();
        List<Team> result = findAllTeams(con);
        DBUtils.close(con);
        return result;
    }

    private List<Team> findAllTeams(Connection con) throws SQLException {
        List<Team> teams = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_TEAMS);
            while (rs.next()) {
                teams.add(extractTeam(rs));
            }
        } catch (SQLException ex) {
            // (1) log this exception
            System.err.println("Cannot find all teams");
            // (2)
            throw new SQLException("Cannot find all teams", ex);
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (stmt != null) {
                DBUtils.close(stmt);
            }
        }
        return teams;
    }

    public User getUser(String login) throws SQLException {
        Connection con = getConnection();
        User result = getUser(con, login);
        DBUtils.close(con);
        return result;
    }

    private User getUser(Connection con, String login) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
            pstmt.setString(VALUE_INDEX, login);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return null;
    }

    public Team getTeam(String name) throws SQLException {
        Connection con = getConnection();
        Team result = getTeam(con, name);
        DBUtils.close(con);
        return result;
    }

    private Team getTeam(Connection con, String name) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(SQL_FIND_TEAM_BY_NAME);
            pstmt.setString(VALUE_INDEX, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractTeam(rs);
            }
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return null;
    }

    public boolean setTeamsForUser(User user, Team... teams) throws SQLException {
        Connection con = getConnection();
        boolean result = setTeamsForUser(con, user, teams);
        DBUtils.close(con);
        return result;
    }

    private boolean setTeamsForUser(Connection con, User user, Team... teams) throws SQLException {
        PreparedStatement pstmt = null;
        boolean result = true;
        try {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            for (Team team : teams) {
                pstmt = con.prepareStatement(SQL_SET_TEAM_FOR_USER, Statement.RETURN_GENERATED_KEYS);
                int k = 1;
                pstmt.setString(k++, Integer.toString(user.getId()));
                pstmt.setString(k++, Integer.toString(team.getId()));
                if (pstmt.executeUpdate() <= 0) {
                    result = false;
                }
            }

            con.commit();
        } catch (SQLException ex) {

            DBUtils.rollback(con);
            // (1) log this exception
            // LOG.error("Cannot insert two users" ,ex);

            // (2)
            throw new SQLException("Cannot set teams for user", ex);
        } finally {
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return result;
    }

    public List<Team> getUserTeams(User user) throws SQLException {
        Connection con = getConnection();
        List<Team> result = getUserTeams(con, user);
        DBUtils.close(con);
        return result;
    }

    private List<Team> getUserTeams(Connection con, User user) throws SQLException {
        List<Team> teams = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_TEAMS, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(VALUE_INDEX, Integer.toString(user.getId()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                teams.add(extractTeam(rs));
            }
        } catch (SQLException ex) {
            // (1) log this exception
            System.err.println("Cannot get all teams for the user");
            // (2)
            throw new SQLException("Cannot get all teams for the user", ex);
        } finally {
            if (rs != null) {
                DBUtils.close(rs);
            }
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
        }
        return teams;
    }

    private static User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        return user;
    }

    private static Team extractTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setName(rs.getString("name"));
        return team;
    }

}
