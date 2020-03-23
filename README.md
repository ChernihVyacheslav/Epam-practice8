# Epam-practice8
Tasks from the eighth practice class of EPAM java course at spring 2019.

Практическое задание №8
_______________________

База данных.

В качестве базы данных использовать любую реляционную БД.

БД содержит три таблицы:
users (id, login)
teams (id, name)
users_teams (user_id, team_id)

Изначально таблицы БД должны иметь некоторое наполнение (см. код класса Demo)

В корне создать каталог sql и сохранить в нем скрипт создания базы данных db-create.sql

_______________________


Во всех заданиях создать и реализовать соответствующие типы таким образом,
чтобы при запуске класса Demo отрабатывала соответствующая функциональность.

Класс Demo (скопировать в свой проект)
-------------------------------------------------------
package ua.nure.your_last_name.practice8;

import java.util.List;

import ua.nure.your_last_name.practice8.db.DBManager;
import ua.nure.your_last_name.practice8.db.entity.Team;
import ua.nure.your_last_name.practice8.db.entity.User;

public class Demo {

    private static void printList(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        // users  ==> [ivanov]
        // teams  ==> [teamA]

        DBManager dbManager = DBManager.getInstance();

        // Part 1
        dbManager.insertUser(User.createUser("petrov"));
        dbManager.insertUser(User.createUser("obama"));
        printList(dbManager.findAllUsers());
        // users  ==> [ivanov, petrov, obama]

    }
}
-------------------------------------------------------

Задание 1
_______________________

Метод DBManager#insertUser должен модифицировать поле id объекта User.
Метод DBManager#findAllUsers возвращает объект java.util.List<User>.
_______________________


Замечание. Класс User должен содержать:
1) метод 'getLogin()', который возвращает логин пользователя;
2) метод 'toString()', который возвращает логин пользователя;
3) реализацию метода 'equals(Object obj)', согласно которой два объекта User равны тогда и только тогда, когда они имеют один логин.
4) статический метод createUser(String login), который создает объект User по логину (идентификатор равен 0). 

------------------------
Добавить в класс Demo код для второго задания
-------------------------------------------------------
package ua.nure.your_last_name.practice8;

import java.util.List;

import ua.nure.your_last_name.practice8.db.DBManager;
import ua.nure.your_last_name.practice8.db.entity.Team;
import ua.nure.your_last_name.practice8.db.entity.User;

public class Demo {

    private static void printList(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        // users  ==> [ivanov]
        // teams  ==> [teamA]

        DBManager dbManager = DBManager.getInstance();

        // Part 1
        dbManager.insertUser(User.createUser("petrov"));
        dbManager.insertUser(User.createUser("obama"));
        printList(dbManager.findAllUsers());
        // users  ==> [ivanov, petrov, obama]

        System.out.println("===========================");

        // Part 2
        dbManager.insertTeam(Team.createTeam("teamB"));
        dbManager.insertTeam(Team.createTeam("teamC"));

        printList(dbManager.findAllTeams());
        // teams ==> [teamA, teamB, teamC]        

    }
}
-------------------------------------------------------

Задание 2
_______________________

Метод DBManager#insertTeam должен модифицировать поле id объекта Team.
Метод DBManager#findAllTeams возвращает объект java.util.List<Team>.
_______________________

Замечание. Класс Team должен содержать:
1) метод 'getName()', который возвращает название группы;
2) метод 'toString()', который возвращает название группы;
3) реализацию метода 'equals(Object obj)', согласно которой два объекта Team равны тогда и только тогда, когда они имеют одно название.
4) статический метод createTeam(String name), который создает объект Team по имени (идентификатор равен 0).
--------------------------------
Добавить в класс Demo код для третьего задания
-------------------------------------------------------
package ua.nure.your_last_name.practice8;

import java.util.List;

import ua.nure.your_last_name.practice8.db.DBManager;
import ua.nure.your_last_name.practice8.db.entity.Team;
import ua.nure.your_last_name.practice8.db.entity.User;

public class Demo {

    private static void printList(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        // users  ==> [ivanov]
        // teams  ==> [teamA]

        DBManager dbManager = DBManager.getInstance();

        // Part 1
        dbManager.insertUser(User.createUser("petrov"));
        dbManager.insertUser(User.createUser("obama"));
        printList(dbManager.findAllUsers());
        // users  ==> [ivanov, petrov, obama]

        System.out.println("===========================");

        // Part 2
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

    }
}
-------------------------------------------------------

Задание 3
_______________________

Метод DBManager#setTeamsForUser должен реализовывать транзакцию. Грамотно реализовать логику commit/rollback транзакции.
Метод DBManager#getUserTeams возвращает объект java.util.List<Team>.
_______________________

Замечание. Метод 'setTeamsForUser(User, Team...)' реализовать с помощью транзакции: 
вследствие вызова данного метода пользователю будут назначены либо все группы, указаннные в списке аргументов, либо ни одна из них.
Если метод будет вызван так: setTeamsForUser(user, teamA, teamB, teamC),
то в таблицу связей 'users_teams' записи должны быть вставлены последовательно в порядке появления групп в списке аргументов слева направа:

user_id, teamA_id
user_id, teamB_id 
user_id, teamC_id

Если последняя запись не может быть добавлена, то первые две также не попадают в базу данных (делать откат транзакции).

Замечание. Метод DBManager#getUserTeams должен возвращать объект List<Team>.
