# query4PubPsych

## Installation

```git clone https://github.com/clubs-project/PubPsych.git```

```mvn clean dependency:copy-dependencies package```


## Run

```java -jar target/PubPsych-0.0.1-SNAPSHOT-jar-with-dependencies.jar -l <LAN> -i <FILE>```


## Complete pipeline

 1. Lemmatise the corpus with PubPsych-0.0.1-SNAPSHOT-jar-with-dependencies.jar 

 2. Use ```scripts/training.sh``` to train a moses system and obtain the translation table 

 3. 
