package my.projects.todolist.models;

public enum Priority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String name ;

    Priority(String name) {
        this.name = name ;
    }

    public String getName() {
        return name;
    }
}
