# Aya
Aya is a command-line screenshotter tool for UNIX-like systems that run on an X11 graphical environment such as Linux systems, FreeBSD, OpenBSD and more.

# Requirements and download

### Requirements:

- UNIX-like system that can run a graphical X11 desktop (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/) **or** [ImageMagick](https://imagemagick.org/)
- Xwininfo (optional, for window capture) 

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Note 1: ImageMagick support is secondary and is planned to be removed in the future.

Note 2: Wayland is not yet supported but is planned to be in a future release through the use of the grim and slurp programs (while preserving FFmpeg feature-parity).

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
