# Setting up Aya dependencies

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
* Wayland support relies on `grim`, and so it only works on wl-roots compositors (Sway, Wayfire, Labwc, Niri, etc) and Hyprland.
* GUI mode only supports x11, so using it in Wayland might result in a blurry or pixelated menu and requires xwayland.

## Installing dependencies
The instructions below are for Linux-based systems. The commands seen below must be run as root.
You do not need to run all commands, only the ones appropriate for your setup.
For instance, if you are on Wayland, you need to run the Wayland command but not the X11 command.

Optional dependencies are included in these commands, it's up to you to decide if you want/need them.

### Arch Linux family (Arch, Artix, EndeavourOS, etc)

**Core dependencies:**
```sh
pacman -S jre-openjdk ffmpeg # Java for running programs only
```
or
```sh
pacman -S jdk-openjdk ffmpeg # Java for running and building programs
```

**X11 dependencies:**
```sh
pacman -S xclip xorg-xwininfo
```

**Wayland dependencies:**
```sh
pacman -S grim slurp wl-clipboard
```

### Debian family (Debian, Devuan, Ubuntu, Linux Mint, etc)

**Core dependencies:**
```sh
apt install default-jre ffmpeg # Java for running programs only
```
or
```sh
apt install default-jdk ffmpeg # Java for running and building programs
```

**X11 dependencies:**
```sh
apt install xclip x11-utils
```

**Wayland dependencies:**
```sh
apt install grim slurp wl-clipboard
```
