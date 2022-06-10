package me.neonix.versionmanager;

public class Version implements Comparable<Version> {
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
    public String toString() {
        return this.version;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            return this.compareTo((Version) obj) == 0;
        }
        return false;
    }
}
