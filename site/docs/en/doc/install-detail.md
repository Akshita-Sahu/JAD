# Install JAD

## Quick installation

### Use `jad-boot`(Recommended)

Download`jad-boot.jar`，Start with `java` command:

```bash
curl -O https://github.com/Akshita-Sahu/JAD/jad-boot.jar
java -jar jad-boot.jar
```

Print usage:

```bash
java -jar jad-boot.jar -h
```

### Use `jad.sh`

You can install JAD with one single line command on Linux, Unix, and Mac. Pls. copy the following command and paste it into the command line, then press _Enter_ to run:

```bash
curl -L https://github.com/Akshita-Sahu/JAD/install.sh | sh
```

The command above will download the bootstrap script `jad.sh` to the current directory. You can move it to any other place you want, or put its location in `$PATH`.

You can enter its interactive interface by executing `jad.sh`, or execute `jad.sh -h` for more help information.

## Full installation

Latest Version, Click To Download: [![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/latest_version)

Download and unzip, find `jad-boot.jar` in the directory. Start with `java` command:

```bash
java -jar jad-boot.jar
```

Print usage:

```bash
java -jar jad-boot.jar -h
```

## Manual Installation

[Manual Installation](manual-install.md)

## Installation via Packages

JAD has packages for Debian and Fedora based systems.
you can get them from the github releases page https://github.com/akshita-sahu/jad/releases.

### Instruction for Debian based systems

```bash
sudo dpkg -i jad*.deb
```

### Instruction for Fedora based systems

```bash
sudo rpm -i jad*.rpm
```

### Usage

After the installation of packages, execute

```bash
jad.sh
```

## Offline Help Documentation

Latest Version Documentation, Click To Download:[![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/doc/latest_version)

## Uninstall

- On Linux/Unix/Mac, delete the files with the following command:

  ```bash
  rm -rf ~/.jad/
  rm -rf ~/logs/jad/
  ```

- On Windows, delete `.jad` and `logs/jad` directory under user home.

---

::: warning
If you need to diagnose applications running on JDK 6/7, please click [here to install jad 3](https://github.com/Akshita-Sahu/JAD/3.x/en/doc/install-detail.html).
:::
