package pl.ias.pas.hotelroom.pasrest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Student {

    private String id;
    private String name;

    public Student(){}

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(id, student.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
