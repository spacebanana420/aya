# Building Aya from source

Building Aya requires my own build tool [Yuuka](https://github.com/spacebanana420/yuuka).

The command `yuuka` as seen below applies if you have it installed system-wide. If you do not, then replace `yuuka` with `java -jar yuuka.jar`.

### Getting the source code

```
git clone https://github.com/spacebanana420/aya.git
cd aya
```

### Building a JAR file

```
yuuka package
```

### Installing Aya system-wide (run as root)
This installs Aya in /usr/local/bin so it can be accessed from anywhere by running `aya`.

```
yuuka install
```

### Build separate .class files

If for some reason you don't want Aya built as a JAR and you want to have the raw bytecode:

```
yuuka build
```
