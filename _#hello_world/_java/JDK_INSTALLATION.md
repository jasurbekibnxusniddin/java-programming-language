
# Linux 64-bit installation instructions for Java or JDK

### Follow these steps to download and install Java for Linux.
1. [Download](#download)
2. [Install](#install)
3. [Verify](#verify)

---

### Download [<-](#)
There are two main options:

#### Option 1: OpenJDK (recommended, free & open-source)
- OpenJDK can be installed directly from your package manager.
- Example for **Ubuntu/Debian**:

    ```bash
    sudo apt update
    sudo apt install openjdk-17-jdk -y
    ```

#### Option 2: Oracle JDK (official distribution)

1. [Go to Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/).
2. Download the Linux x64 .tar.gz package for your preferred version (e.g., Java 21 LTS).
3. Extract it:
    ```bash
    tar -xvzf jdk-21_linux-x64_bin.tar.gz
    ```

4. Move it to /usr/lib/jvm (create directory if it doesn’t exist):
    ```bash
    sudo mkdir -p /usr/lib/jvm
    sudo mv jdk-21 /usr/lib/jvm/
    ```

### install [<-](#)
1. Set environment variables (if manually installed Oracle JDK):
Edit ~/.bashrc or ~/.zshrc and add:
    ```bash
    export JAVA_HOME=/usr/lib/jvm/jdk-21
    export PATH=$JAVA_HOME/bin:$PATH
    ```
2. Reload shell:
    ```sh
    source ~/.bashrc
    ```
3. If multiple versions are installed, configure the default:
    ```bash
    sudo update-alternatives --config java
    sudo update-alternatives --config javac
    ```

### Verify [<-](#)
* Check your installation:
    ```bash
        java -version
        javac -version
    ```

*  Expected output (example for Java 21):
    ````txt
    openjdk version "21" 2023-09-19
    OpenJDK Runtime Environment (build 21+35)
    OpenJDK 64-Bit Server VM (build 21+35, mixed mode, sharing)
    ````

✅ Java is now installed and ready!

