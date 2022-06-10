package me.neonix.versionmanager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Remote extends Project {
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

    private static String readUrl(String urlString) throws Exception {
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
        String json = Remote.readUrl(this.sURL);
        Gson gson = new Gson();
        return gson.fromJson(json, Release.class);
    }

    public Release getData() {
        return this.data;
    }
}