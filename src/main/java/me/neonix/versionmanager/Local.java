package me.neonix.versionmanager;

public class Local extends Project{
    public Local(String author, String name, String version, String separator) {
        super(author, name, new Version(version, separator));
    }
}
