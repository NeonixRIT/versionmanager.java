package me.neonix.versionmanager;

import java.util.EventListener;

public interface RefreshObserver extends EventListener {
    void invoke(Status status);
}
