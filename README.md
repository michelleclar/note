## Introduction

> A note-taking application
> Backend: Java (Quarkus JDK 21)
> Frontend: React (Next.js, Tailwind CSS)

> To use, switch to the `pro` branch. Deployment and development steps are provided.

## Future Plans

- [ ] Integrate AI for article continuation
- [ ] Integrate AI for article recommendations

## server

> language:java,go,python;

> frameworks: Quarkus

---

## Development

> Need JDK 21
> Modify db config

```sh
openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem

openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
## Currently, the services have not been split. There are plans to separate the authorization service in the future.
./gradlew quarkusDev
```

### Environmental references

#### System info

```sh
┌──────────────────────────────────────────┐
  OS : Arch Linux x86_64
  Kernel : 6.9.4-zen1-1-zen
  Resolution : 2560x1440
  DE : Hyprland
  Terminal : kitty
┌──────────────────────────────────────────┐
  CPU : 13th Gen Intel i7-13700KF (24) @ 5.300GHz
  GPU : NVIDIA GeForce RTX 4070 Ti
  GPU Driver : NVIDIA 550.90.07
 ﬙ Memory : 7488MiB / 31902MiB
└──────────────────────────────────────────┘
```

#### Development tools

| ---                      | ---                                                     |
| ------------------------ | ------------------------------------------------------- |
| Code editor              | neovim（v0.10.0）                                       |
| ------------------------ | ------------------------------                          |
| Browser                  | Chrome（126.0.6478.126）                                |
| ------------------------ | ------------------------------                          |
| Database connection tool | DataGrip(Not recommended, the experience is very poor.) |
| ------------------------ | ------------------------------                          |

##### Other tools

| ---                      | ---                            |
| ------------------------ | ------------------------------ |
| sdkman                   | manger jdk version             |
| ------------------------ | ------------------------------ |

---

### Options

```sh
## Options 1
sdk install quarkus
## Options 2
yay -S jbang
jbang app install --fresh --force quarkus@quarkusio

## creat project
quarkus create app org.carl:code-quarkus\
    --no-code \
    --gradle
```

### Refer to the website

- [quarkus](https://quarkus.io/)

* [quarkus-cli](https://cn.quarkus.io/guides/cli-tooling)

- [quarkus Github](https://github.com/quarkusio/quarkus)