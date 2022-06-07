package me.neonix.versionmanager;

public class Main {
    public static void main(String[] args) {
        VersionManager vm = new VersionManager("Aquatic-Labs", "Umbra-Mod-Menu", "2.0.4");

        vm.registerObserver(status -> {
            if (status == Status.OUTDATED) {
                System.out.println("Outdated.");
            }
        });

        vm.registerObserver(status -> {
            if (status == Status.CURRENT) {
                System.out.println("Current.");
            }
        });

        vm.registerObserver(status -> {
            if (status == Status.DEV) {
                System.out.println("Dev.");
            }
        });

        vm.checkStatus();
    }
}