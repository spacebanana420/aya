# Aya
Aya is a command-line screenshotter tool for UNIX-like systems that run on an raphical environment such as Linux systems, FreeBSD, OpenBSD and more.

# Requirements and download

### Core requirements:

- UNIX-like system that can run X11 or Wayland (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/)

### Additional requirements for X11 users:
- Xwininfo (optional, for window capture)

### Additional requirements for Wayland users:
- Grim
- Slurp (optional, for capturing a selection of the screen)

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Note: Wayland support is currently experimental but seems to work well on wl-roots-based compositors.

# How to use

Once downloaded, you can run Aya with the command `java -jar aya.jar`. Simply running Aya will make it take an immediate screenshot (unless your system is unsupported).

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

### Supported image formats
* PNG
* JPG
* AVIF
* BMP

# Build from source

Download [Yuuka](https://github.com/spacebanana420/yuuka), open a terminal at the root of the project and run:
```
java -jar yuuka.jar package
```

Or if you already have Yuuka installed on your system:
```
yuuka package
```
