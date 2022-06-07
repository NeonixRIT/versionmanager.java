package me.neonix.versionmanager;

public abstract class Project {
    private final String author;
    private final String name;
    private Version version;

    public Project(String author, String name, Version version) {
        this.author = author;
        this.name = name;
        this.version = version;
    }

    @Override
    public String toString() {
        return "Project[" + this.author + ", " + this.name + ", " + this.version + "]";
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return this.version;
    }
}
