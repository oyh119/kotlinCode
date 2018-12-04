package cn.mealkey.mkservice.node.model;

import java.util.Objects;

public class NodeTaste {
    public long id;
    public String name;

    public NodeTaste(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NodeTaste() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeTaste nodeTaste = (NodeTaste) o;
        return id == nodeTaste.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NodeTaste{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
