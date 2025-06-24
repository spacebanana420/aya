# Aya
Aya is a command-line screenshotter tool for UNIX-like systems that run on an X11 graphical environment such as Linux systems, FreeBSD, OpenBSD and more.

# Requirements and download

### Core requirements:

- UNIX-like system that can run a graphical desktop (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/) **or** [ImageMagick](https://imagemagick.org/)

### Requirements for X11 users:
- Xwininfo (optional, for window capture)

### Requirements for Wayland users:
- Grim
- FFmpeg (ImageMagick not supported)
- Slurp (optional, for capturing a selection of the screen)

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Note 1: ImageMagick support is secondary and is planned to be removed in the future. It also only supports x11

Note 2: Wayland support is currently experimental but seems to work well on wl-roots-based compositors.

# How to use

Once downloaded, you can run Aya with the command `java -jar aya.jar`. Simply running Aya will make it take an immediate screenshot (unless your system is unsupported).

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

### Supported image formats
* PNG
* JPG
* AVIF (FFmpeg only)
* BMP (FFmpeg only)

# Build from source

Download [Yuuka](https://github.com/spacebanana420/yuuka), open a terminal at the root of the project and run:
```
java -jar yuuka.jar package
```

Or if you already have Yuuka installed on your system:
```
yuuka package
```
