# Installing Aya

## Downloading and running Aya

After [installing the necessary and desired dependencies](doc/install.md), you can download Aya's [latest release](https://github.com/spacebanana420/aya/releases) here.

Once downloaded, you run Aya with the command `java -jar aya.jar`.

To see a full list of options, run `java -jar aya.jar -h`.

Aya also makes use of a configuration file, located in `~/.config/aya/aya.conf`. This config overrides Aya's default behavior for certain options, overriding defaults, but any CLI argument you pass to it will also override the respective config's settings.

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
