package ua.nure.chernykh.practice8;

import ua.nure.chernykh.practice8.db.DBManager;
import ua.nure.chernykh.practice8.db.entity.Team;
import ua.nure.chernykh.practice8.db.entity.User;

import java.sql.SQLException;
import java.util.List;

public class Demo {

    private static void printList(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        // users  ==> [ivanov]
        // teams  ==> [teamA]

        DBManager dbManager = DBManager.getInstance();

        // Part 1
        try {
            dbManager.insertUser(User.createUser("petrov"));
            dbManager.insertUser(User.createUser("obama"));
            printList(dbManager.findAllUsers());
            // users  ==> [ivanov, petrov, obama]
            System.out.println("===========================");

//         Part 2
            dbManager.insertTeam(Team.createTeam("teamB"));
            dbManager.insertTeam(Team.createTeam("teamC"));

            printList(dbManager.findAllTeams());
            // teams ==> [teamA, teamB, teamC]

            System.out.println("===========================");

            // Part 3
            User userPetrov = dbManager.getUser("petrov");
            User userIvanov = dbManager.getUser("ivanov");
            User userObama = dbManager.getUser("obama");

            Team teamA = dbManager.getTeam("teamA");
            Team teamB = dbManager.getTeam("teamB");
            Team teamC = dbManager.getTeam("teamC");

            // method setTeamsForUser must implement transaction!
            dbManager.setTeamsForUser(userIvanov, teamA);
            dbManager.setTeamsForUser(userPetrov, teamA, teamB);
            dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

            for (User user : dbManager.findAllUsers()) {
                printList(dbManager.getUserTeams(user));
                System.out.println("~~~~~");
            }
            // teamA
            // teamA teamB
            // teamA teamB teamC
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
