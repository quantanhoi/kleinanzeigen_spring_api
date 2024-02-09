# HS Kleinanzeigen

## Gruppe: *Autowired*

### Mitglieder
- Christian Schmitt - 772402
- Atta-ul-Haleem Mirza - 772398
- Ngo Tien Duc - 766941
- Vincent Rescheleit - 769872
- Trung Thieu Quang - 771043

## Projektgenerierung

[Maven Archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) zur Erstellung einer simplen Spring Boot Anwendung für HS Kleinanzeigen.

### Voraussetzungen
* JDK 17
* git
* maven
* Docker


### Generierung
* Zielanwendung via cli generieren:

**${VERSION} mit der gewünschten Version ersetzen!**
**${GRUPPENNAME} mit dem gewünschten Gruppennamen ersetzen!**

```bash
$ mvn archetype:generate \
    -DarchetypeGroupId=io.github.leichtundkross \
    -DarchetypeArtifactId=hs-kleinanzeigen-archetype \
    -DarchetypeVersion=${VERSION} \
    -DgroupId=de.hs.da \
    -DartifactId=hs-kleinanzeigen-${GRUPPENNAME} \
    -Dversion=0.0.1-SNAPSHOT \
    -DinteractiveMode=false
```

oder

* Zielanwendung mit IntelliJ generieren:

    * **File->New->Project**

    * Check "Create from archetype" box

    * Entweder **io.github.leichtundkross:hs-kleinanzeigen-archetype** aus der Liste auswählen oder hinzufügen.
      ArchetypeGroupId=**io.github.leichtundkross**, DarchetypeArtifactId=**hs-kleinanzeigen-archetype**, version=**${VERSION}**.
    * GroupId=**de.hs.da**, artifactId=**hs-kleinanzeigen-${GRUPPENNAME}**, version=**0.0.1-SNAPSHOT**.

## Setup der lokalen Datenbank

Für die Entwicklung wird eine MySQL Datenbank innerhalb eines Docker Containers verwendet. 

Die Datenbank kann mit diesem Befehl gestartet werden:

```bash
docker run --name=root -p 4406:3306 -e MYSQL_ROOT_PASSWORD=start01 -e MYSQL_DATABASE=KLEINANZEIGEN -d mysql:8.0.22
```

