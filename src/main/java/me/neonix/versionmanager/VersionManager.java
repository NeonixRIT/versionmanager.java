package me.neonix.versionmanager;

import java.util.HashSet;

public class VersionManager {
    private final String author;
    private final String projectName;
    private final String separator;

    private final Local local;
    private Remote remote;

    private Status status;
    private final HashSet<StatusObserver> observers;

    public VersionManager(String author, String projectName, String version, String separator) {
        this.author = author;
        this.projectName = projectName;
        this.separator = separator;
        
        this.local = new Local(author, projectName, version, separator);
        this.remote = null;
        
        this.status = null;
        this.observers = new HashSet<>();
    }

    public VersionManager(String author, String projectName, String version) {
        this(author, projectName, version, "\\.");
    }

    private void notifyObservers() {
        for (StatusObserver observer : this.observers) {
            observer.invoke(this.status, this.getRelease());
        }
    }

    public void registerObserver(StatusObserver observer) {
        this.observers.add(observer);
    }

    public Status checkStatus() {
        if (this.remote == null) {
            this.remote = new Remote(this.author, this.projectName, this.separator);
        } else {
            this.remote.refresh();
        }

        if (this.local.getVersion().compareTo(this.remote.getVersion()) == 0) {
            this.status = Status.CURRENT;
        } else if (this.local.getVersion().compareTo(this.remote.getVersion()) < 0) {
            this.status = Status.OUTDATED;
        } else {
            this.status = Status.DEV;
        }
        this.notifyObservers();
        return this.status;
    }

    public Release getRelease() {
        return this.remote.getData();
    }
}