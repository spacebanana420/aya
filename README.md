# Aya
Aya is a command-line screenshot tool for UNIX-like systems that can take screenshots on X11, Wayland and TTY (on Linux).

```sh
space@Wakasagi ~/S/aya (main)> aya -file image.png
Screenshot saved successfully

space@Wakasagi ~/S/aya (main)> aya -clip image.png
Screenshot copied to clipboard
```

## How to use

Install the [necessary and desired dependencies](doc/install.md) and download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Once downloaded, you can run Aya with the command `java -jar aya.jar`.

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

### Supported image formats
* PNG
* JPG
* AVIF
* BMP

## Build from source

Building Aya requires my own build tool [Yuuka](https://github.com/spacebanana420/yuuka).

The command `yuuka` as seen below applies if you have it installed system-wide. If you do not, then replace `yuuka` with `java -jar yuuka.jar`.

### Getting the source code

```
git clone https://github.com/spacebanana420/aya.git
cd aya
```

### Building a JAR file

```
yuuka package
```

### Installing Aya system-wide (run as root)
This installs Aya in /usr/local/bin so it can be accessed from anywhere by running `aya`.

```
yuuka install
```

### Build separate .class files

If for some reason you don't want Aya built as a JAR and you want to have the raw bytecode:

```
yuuka build
```
