# Uninstall JDK on Linux (Ubuntu/Debian)
### 🔹 Step 1. Check Which JDK is Installed
* Run:
    ```bash
    dpkg --list | grep openjdk
    ```
* This will show all installed JDK/JRE packages, for example:
    ```bash
    ii  openjdk-11-jdk:amd64
    ii  openjdk-11-jre:amd64
    ```
### 🔹 Step 2. Remove the JDK + JRE
* To uninstall a specific version (e.g., Java 11):
    ```bash
    sudo apt remove --purge openjdk-11-jdk openjdk-11-jdk-headless openjdk-11-jre openjdk-11-jre-headless
    ```
### 🔹 Step 3. Remove Extra Dependencies

* Clean up unused packages:
    ```bash
    sudo apt autoremov
    ```

### 🔹 Step 4. (Optional) Remove All Java Versions
* If you want to remove every Java installation in one go:
    ```bash
    sudo apt purge openjdk-* icedtea-* icedtea6-*
    sudo apt autoremove
    ```

### 🔹 Step 5. Verify Uninstallation

* Check:
    ```bash
    java -version
    javac -version
    ```

* Expected result:
    ```bash
    Command 'java' not found
    ```

### 🔹 Step 6. (Optional) Remove Manual Oracle JDK

If you previously installed Oracle JDK via .tar.gz, it won’t show in dpkg.
To remove it:

1. Check where it was extracted (usually /usr/lib/jvm):
    ```bash
    ls /usr/lib/jvm
    ```

2. Delete it:
    ```bash
    sudo rm -rf /usr/lib/jvm/jdk-<version>
    ```

3. Remove any JAVA_HOME or PATH changes from ~/.bashrc, ~/.zshrc, or /etc/environment.

4. Reload:
    ```bash
    source ~/.bashrc
    ```

