# Aya
Aya is a command-line screenshotter tool for Unix-based systems that run on X11 environments such as Linux systems, FreeBSD, OpenBSD and others.

Aya implements FFmpeg or ImageMagick as its screen capture and encoding backend. It supports both equally, but differences in picture compression between implementations exist.

**Aya is under very early development**

# Requirements and running

### Requirements:

- Unix-like system that can run a graphical X11 desktop (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/) **or** [ImageMagick](https://imagemagick.org/) 

Download Aya's [latest release](https://github.com/spacebanana420/aya/releases) and run with `java -jar aya.jar`.

# Build from source

Download [Yuuka](https://github.com/spacebanana420/yuuka), open a terminal at the root of the project and run:
```
yuuka package
```
Or if you don't have Yuuka installed:
```
java -jar yuuka.jar package
```
