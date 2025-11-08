# Aya
Aya is a command-line screenshot tool for UNIX-like systems running on X11 or Wayland.

# Requirements and download

### Core requirements:

- UNIX-like system that can run X11 or Wayland (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/)

### Additional requirements for X11 users:
- xclip (optional, for copying the screenshot to clipboard)
- Xwininfo (optional, for window capture)

### Additional requirements for Wayland users:
- Grim
- Slurp (optional, for capturing a selection of the screen)

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Note: Wayland support is currently experimental but seems to work well on wl-roots-based compositors.

# How to use

Once downloaded, you can run Aya with the command `java -jar aya.jar`.

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

### Supported image formats
* PNG
* JPG
* AVIF
* BMP

# Build from source

Building Aya requires [Yuuka](https://github.com/spacebanana420/yuuka) as I use my build tool in this project. If you do not have Yuuka installed on your system, replace in the commands below `yuuka` with `java -jar yuuka.jar`.

The commands below must be ran at the root of this project.

### Building a JAR file

```
yuuka package
```

### Installing Aya system-wide (run as root)

```
yuuka install
```

