# Installing Aya

Aya is a Java program and so it requires Java but it also has multiple external dependencies, most of which are optional depending on your setup.

## Core requirements:

- UNIX-like operating system (such as Linux-based systems, FreeBSD, OpenBSD, etc)
- Java 11 or newer
- [FFmpeg](https://ffmpeg.org/)

### Additional requirements for X11 users:
- xclip (optional, for copying the screenshot to clipboard)
- xwininfo (optional, for window capture)

### Additional requirements for Wayland users:
- grim
- slurp (optional, for capturing a selection of the screen)
- wl-clipboard (optional, for copying the screenshot to clipboard)

### Additional requirements for TTY screen capture:
- Linux is the only supported platform

Install the necessary and desired dependencies from your Linux system's package manager and download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

## Notes
* Wayland support relies on `grim`, and so it only works on wl-roots compositors and Hyprland.
* GUI mode only supports x11, so using it in Wayland might result in a blurry or pixelated menu, and requires xwayland to work.

## Installing Aya on your system

Since Aya is a CLI program, you want to eventually run it from anywhere just by typing the `aya` command. In order to do this, you have to install Aya on your system.

### Installing Aya automatically (using Yuuka)

To install Aya automatically, you need my [Yuuka](https://github.com/spacebanana420/yuuka) build tool. Download the latest version of Yuuka from its releases, place the JAR at the same place where Aya's JAR is, and run:

```sh
java -jar yuuka.jar install aya.jar
```

If you already have Yuuka installed on your system, you can run instead:

```sh
yuuka install aya.jar
```

Both commands require root.


### Installing Aya manually

If you do not want to download or install my build tool, you have to do manually what my program does under the hood to install Aya.

Create the following script:

```sh
touch aya
chmod +x aya
```

Open the file with a text editor, and paste the following text:

```sh
#!/bin/sh
java -jar /usr/local/bin/jars/aya.jar "$@"
```

Now run as root:

```sh
mkdir /usr/local/bin/jars/
mv aya.jar /usr/local/bin/jars/
mv aya /usr/local/bin
```
