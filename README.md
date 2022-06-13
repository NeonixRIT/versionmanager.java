# versionmanager.java
A GitHub Project Version Manager that polls latest version data from GitHub repo release tag.

# Installation
### Reference JAR
[Download the latest JAR](https://www.github.com/NeonixRIT/versionmanager.java/releases/latest) and add it to your project's references or dependencies directly.

# Usage
versionmanager checks the version passed to its constructor against the tag attached to the latest release on a GitHub repo. Comparing the given version string against the `tag_name` value at [https://api.github.com/repos/{author}/{projectName}/releases/latest](). This means release tag names need to be formatted specifically for this. Versionmanager doesnt support letters in version categories* and assumes a separator of a period unless told otherwise.

*A version category is a set of numbers separated by a uniform character (e.g. 2.0.3 has categories 2, 0, and 3). Using Semantic Versioning there are usually 3 version categories (major, minor, and patch) but versionmanager supports more categories as well.
```
import me.neonix.versionmanagerjava.VersionManager;
import me.neonix.versionmanagerjava.Status;

class Main {
    public static void main(String[] args) {
        VersionManager vm = new VersionManager("Aquatic-Labs", "Umbra-Mod-Menu", "2.0.4");

        vm.registerObserver((status, data) -> {
            if (status == Status.OUTDATED) {
                System.out.println("Outdated.");
            }
        });

        vm.registerObserver((status, data) -> {
            if (status == Status.CURRENT) {
                System.out.println("Current.");
            }
        });

        vm.registerObserver((status, data) -> {
            if (status == Status.DEV) {
                System.out.println("Dev.");
            }
        });

        vm.checkStatus();
    }
}

```