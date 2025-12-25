# Install via Command Line Interface

## use JBang to install the Quarkus CLI

```sh
curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/
curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --force quarkus@quarkusio
```

## Create the Getting Started Application
Run this script in your CLI:
```sh
quarkus create
```

and 
```sh
cd code-with-quarkus
```

## Run the Getting Started Application
```sh
quarkus dev
```
Your Quarkus app is now running at localhost:8080
