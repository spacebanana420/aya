# Aya
Aya is a command-line screenshotter tool for Unix-based systems that run on X11 environments such as Linux systems, FreeBSD, OpenBSD and others.

# Requirements and download

### Requirements:

- Unix-like system that can run a graphical X11 desktop (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/) **or** [ImageMagick](https://imagemagick.org/) 

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Note: ImageMagick support is secondary and lacks some features/support compared to FFmpeg.

# How to use

Once downloaded, you can run Aya with the command `java -jar aya.jar`. Simply running Aya will make it take an immediate screenshot (unless your system is unsupported).

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

### Supported image formats
* PNG
* JPG
* AVIF (FFmpeg only)

# Build from source

Download [Yuuka](https://github.com/spacebanana420/yuuka), open a terminal at the root of the project and run:
```
yuuka package
```
Or if you don't have Yuuka installed:
```
java -jar yuuka.jar package
```
