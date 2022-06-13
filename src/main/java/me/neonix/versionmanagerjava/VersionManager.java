package me.neonix.versionmanagerjava;

import com.google.gson.Gson;

import java.util.HashSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class VersionManager {
    private class Version implements Comparable<Version> {
        private final String[] versionList;
        private final String version;

        public Version(String version, String separator) {
            this.version = version;
            this.versionList = this.version.split(separator);
        }

        public Version(String version) {
            this(version, "\\.");
        }

        @Override
        public int compareTo(Version other) {
            int i = 0;
            while (i < this.versionList.length && i < other.versionList.length) {
                int thisVer = Integer.parseInt(this.versionList[i]);
                int otherVer = Integer.parseInt(other.versionList[i]);
                if (thisVer < otherVer) {
                    return -1;
                } else if (thisVer > otherVer) {
                    return 1;
                }
                i++;
            }
            return 0;
        }
    }

    private class Project {
        private final String author;
        private final String name;
        private Version version;

        public Project(String author, String name, Version version) {
            this.author = author;
            this.name = name;
            this.version = version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public Version getVersion() {
            return this.version;
        }
    }

    private class Remote extends Project {
        private final String sURL;
        private final String separator;
        private Release data;

        public Remote(String author, String projectName, String separator) {
            super(author, projectName, null);
            this.sURL = "https://api.github.com/repos/" + author + "/" + projectName + "/releases/latest";
            this.separator = separator;

            try {
                this.data = this.poll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Version version = new Version(this.data.tag_name, separator);
            this.setVersion(version);
        }

        private String readUrl(String urlString) throws Exception {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder buffer = new StringBuilder();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);

                return buffer.toString();
            } finally {
                if (reader != null)
                    reader.close();
            }
        }

        public void refresh() {
            try {
                Release release = this.poll();
                this.setVersion(new Version(release.tag_name, this.separator));
                this.data = release;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Release poll() throws Exception {
            String json = this.readUrl(this.sURL);
            Gson gson = new Gson();
            return gson.fromJson(json, Release.class);
        }

        public Release getData() {
            return this.data;
        }
    }

    private class Local extends Project{
        public Local(String author, String name, String version, String separator) {
            super(author, name, new Version(version, separator));
        }
    }

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
