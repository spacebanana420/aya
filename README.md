# Aya
Aya is a command-line screenshot tool for UNIX-like systems that can take screenshots on X11, Wayland and TTY (on Linux).

```sh
space@Wakasagi ~/S/aya (main)> aya -file -p /path/to/image
Screenshot saved successfully

space@Wakasagi ~/S/aya (main)> aya -file -p image.png
Screenshot saved successfully

space@Wakasagi ~/S/aya (main)> aya -clip
Screenshot copied to clipboard
```

## Supported image formats
Regardless if you use it from X11, Wayland or a TTY, Aya always uses FFmpeg for feature-parity.
It currently supports:

* PNG
* JPG
* AVIF
* BMP

## How to use

Install the [necessary and desired dependencies](doc/install.md) and download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Once downloaded, you can run Aya with the command `java -jar aya.jar`.

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior, but any CLI argument you pass to it will also override the respective config's settings.

## Documentation
* [Installing Aya](doc/install.md)
* [Building Aya from source](doc/build.md)
