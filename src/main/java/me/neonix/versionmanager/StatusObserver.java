package me.neonix.versionmanager;

import java.util.EventListener;

public interface StatusObserver extends EventListener {
    void invoke(Status status, Release data);
}
