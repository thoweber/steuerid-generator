# Generator für deutsche steuerliche Identifikationsnummer
Auch als **IdNr. oder Steuer-IdNr** bekannt.

![build status](https://github.com/thoweber/steuerid-generator/actions/workflows/maven.yml/badge.svg) 
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=thoweber_steuerid-generator&metric=alert_status)](https://sonarcloud.io/dashboard?id=thoweber_steuerid-generator)

> [EN] Generator for formally valid German Tax Identification Numbers

## Testdaten-Generator für deutsche Steueridentifikationsnummern
💖 **unterstützt sowohl das Format von 2007 als auch die Erweiterung von 2016**  
🤏 **keine Laufzeit-Abhängigkeiten (Runtime Dependencies)**  
⏱️ **auf hohen Durchsatz und kleinen Speicherverbrauch optimiert**  
🦺 **[Tests](https://sonarcloud.io/summary/new_code?id=thoweber_steuerid-generator) und [Benchmark](https://github.com/thoweber/steuerid-generator/blob/main/src/test/java/guru/thomasweber/steuerid/benchmark/BenchmarkRunner.java) enthalten**  


### Durchsatz
_Benchmark durchgeführt unter Windows 11 mit einem Intel® Core™ i7-9750H_

|Benchmark|Mode|Cnt|Score|Error|Units|
|---|---|---|---|---|---|
|`generateThroughputClassic`| thrpt| 5| 1640620,997| ±18312,716| ops/s|
|`generateThroughputV2016`  | thrpt| 5| 1539612,225| ±14934,601| ops/s|

## Verwendung
_Generieren von 10.000 eindeutigen Identifikationsnummern (beide Formate):_
```java
var generator = new SteuerIdGenerator();
var steuerIds = new HashSet<String>();
while (steuerIds.size() < 10000) {
    steuerIds.add(generator.generate());
}
```

_Generieren in einem bestimmten Format:_
```java
var generator = new SteuerIdGenerator();
// Format von 2007
var id2007 = generator.generate(SteuerIdMode.CLASSIC);
// Format von 2016
var id2016 = generator.generate(SteuerIdMode.V2016);
```
