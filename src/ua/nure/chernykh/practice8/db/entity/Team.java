package ua.nure.chernykh.practice8.db.entity;

import java.util.Objects;

public class Team {

    private int id;
    private String name;

    public static Team createTeam(String name) {
        Team newTeam = new Team();
        newTeam.name = name;
        return newTeam;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
