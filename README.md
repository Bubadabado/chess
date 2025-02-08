# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[WEB API sequence diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QqACYnE5Gt0y0MxWMYFXHlNa6l6020C3VgdiyDTtke5UACyD4e9KajdTAcnjKYAUSg3jTeltkLRGMmK+KHaThQ3GF7O4AzHuegeWce9znpe0DlMACC6hwYTyAuj7tmub7dh+lQAKy7iO-5HieVYXle5Swhwag6sQBCJDAEAAGYwJQV6YnB6AcKYXi+P4ATQOw5IwAAMhA0RJAEaQZFkSHMLK1AltUdRNK0BjqAkaAjqKoyzC8bwcE+4kgoU+Ylj0SkoCp+ivEsALnAWYlOvKMAILxPKwjxfGouisTYgmhgumGbrlCUSAUZYBq0uW46GQsSwmkS4YWhyMAAOIUjAsapDAFHeMMdpjqMMDnDA2DJIxoYRW6nZuWBQYOq60qRhy0YJWV2gwJAMCyao8lZUGGiOs6yZmSWDk8tm2CJAYmA6QhRSaVcAxBaMS7Tn0s7zs2k6tn0GnHK+XY5GAvYDkOGFTRl9x9F8C2Ns2Exwaunbvtt25OAAjL+mGqIBp59LhoEwD4wRBtASAAF4oMsMBrS+64ib2aF7fufSHi92HAXhMAEURUAka1lHUSBUB0SDZicEx3h+IEXgoOg3G8b4zACekmSYDdbKrr2MVntJLT2OO3SnQu8FsqNVzc+gplAi+RZyig5Q2fYVP2ZTvpORirnixqnksmSFKJQGQaLWg4VMkVVXeeBSBaJktV+jAOswDyRRyWRs4aBV5obSVVDG7asIoAgBgwDu91K2q7kEqrpJGF7FIoJrgu6wV+uVYUlowJCwwQDQDWpIYkALslqW2y19vtZ1ibdSLaYOVTA1DWIo3Fc+aZ-qDY03Ttu6lnjV0bc3d2PftcOvTh2PlN9s5-YDwON9dEOoehMN9wj72D8jhGqMR2CkUkmM0dAuMHAxhMsYEkK2lx0KxeOrI00J9MiYzdfM6zzTs5zP11mdaC8yXqYC9rb-C6mt+B0ltCGK597LAPHArFyRcg4yBDu6GA5IwBRx-guPWZoIwJ2im7ZAptDCJUtm-a2SQKB2ySA7WO6Da6AOwSbZGXsfZ+wDpZGBTt4HIFiCAw8WtX6oIoZFQ2CD4rsOYJjThag+EGwsuCayp9MYc0PNAzs-NuLgNGJXNAw0a4uzvm3Po8i1CngqP0fRABJaQp57p9i-FuJ4glMgGgrBdJ4OgECgAbA48cKwvj6IAHLjgunsGAjQJ6dynv2Gef49Hn0McY8cZiLFWJsVMOx+pppHS+C4txHiZpOKmL4-xx1AnBI-uDLan4HpPVhgBeeH00zD1+mjMel066TzKdPaGkS55AQXkjFGK80ZrwxlRLeON6IE08ETVi2AfBQGwNweAuozZiJSLTYSW0AElF7LUBoj99FcxQegEc+TRgHA-tpHq9cejRz2LMHoxyUB7D-uZMWgCYCej1GI2EcBFkoDEZArEiiPKFTVoIpBQZuFzjfmg-hmCjY4L1ObJKVsbYkPzmQwurCqHMNKDQj29DDCMMBcHYFod3mZE+fc6FBtYVZRtFlGAaAUDJAQeOIhjU-SGH0ayeQLixCYu0a80x0hAWf2BOUb5XpyUQLUINDR1cLlYt0nkuJ5jyiWOse3FpoS2nhI6cq0Y8S1WJM1ZpVpm4Kg7j1VUrC3TanlBvEnFA95YjNNNdq81ftKldLenar6L9UijyBq69apTzVQy9dU21i8+mr3XuRYZ2Md743ysxYmARLBexssygAUhAG2yyAiZJAA2a+6yLISSqJSNmez-U6xHHM4AGaoBwAgDZKAswhUhPOaXK4VyDloBuaWBtTaW1to7Sqx5I0FVSKsgAKzzWgT586eR-Kdc5AFbkVYkvgYg5BPD0BUvjuyOFJsEX4ORcQlkrVyH8pnRLGAuK6HewJQ9JhzpiVx1DhRHkHAKUqsPc7GlBg8AgCShRaALLxS3pedi5KvheSe2fTAKGb7EwfvQeUEArboAKDdtsXQ3BYTDsoKO6A46DXSAAxg49byM7Fredh2AuHfi6G2BkMIyQMjpCSPoiRlU73lGY-h3lT6GFOC-KhlhcDyjLsXeOIjriR2MfIygMxVGopD2wLgyDmcICOootwcAtGIAGCSFhttphN0bWUbmld0qbBVynaXRV9cTUhsQjq3aT0QmhuQhaiJz1+6I0+g6u867g1gw8x6ipvdI0+sXvUv0gbx4lKi3578EabXxaRuBSC0FgCwTc5FzaYaAvWvhlG3py9Y1DKxrRMZKbJkkygI2rsXpYDAGwHMwg8QyKXzpgzctaYKjSDPFxVmZ5H7GDOWcHtbcnmi3GrBkA3A8CwhW+1-5kmt2fvgRtvABpVCwnUwI-bsAbjcoI2IKz6HwyYdW1AMRR2Ts0rOzpy7vKiWwO3fd9riVnt8cA8et7zVr3tQaldyzytrMXPFQ99Rmjp1DauEVpuYSvPzdSyVvz4bdHeoHlV1G6MyKb0TQ1zAQA)

