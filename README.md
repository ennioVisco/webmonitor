# hiddenname
For optimal reading of this guide and easier reproduction, we recommend opening this folder in IntelliJ Idea 2022.

## Prerequisites
Please remember that to execute this tool, the following condition must be met:
- JVM 11+ installed (IntelliJ should have one out of the box)
- The selected browser engines (for exact replication, Google Chrome 99+ and Mozilla Firefox 99+)
- A desktop environment must be available, with a resolution greater or equal than the ones addressed by the experiments (i.e. a screen bigger than 800x600, ideally bigger than 1920x1200 )

## Executing the experiments
Run the following commands.

### Reflow property
For mobile evaluation of "Reflow"
```sh
./gradlew run --args="ase-mobile reflow" 
```

For desktop evaluation of "Reflow"
```sh
./gradlew run --args="ase-desktop reflow" 
```

### Reflow-slide property
For mobile evaluation of "Reflow-slide" 
```sh
./gradlew run --args="ase-mobile reflow-slide" 
```

For desktop evaluation of "Reflow-slide"
```sh
./gradlew run --args="ase-desktop reflow-slide" 
```

### Hover property
For mobile evaluation of "Hover"
```sh
./gradlew run --args="ase-mobile hover" 
```

For desktop evaluation of "Hover"
```sh
./gradlew run --args="ase-desktop hover" 
```