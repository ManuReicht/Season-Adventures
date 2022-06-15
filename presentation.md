# Season Adventures

## Projektidee

+ Spiel erstellen -> Plattformer
  + Zeitlimit, Gegner, Collectables 
+ Nicht zu schwierig
+ Keine konkrete Idee -> Kostenlose Assets gesucht -> Season Assets gefunden
+ 4 Jahreszeiten -> Umgebung, Objekte und Collectables ändern sich mit den Jahreszeiten

## Technik

+ Framework: LIBGDX
  + Cross Platform Java game development framework (Windows, Linux, macOS, Mobile, Browser)
  + Übernimmt Aufgaben: Kamera, Physik, Level Laden, Animationen, ...
+ Level Bau: Tiled
  + Open Source Level Editor
  + Dateien generieren, welche von LIBGDX unterstützt werden -> einfach Level erstellen

### Design Pattern

+ Abstract Factory
  + Aussehen von Objekten ändert sich mit Jahreszeit
    + momentan nur Münzen und Gegner, aber erweiterbar
  + Eine Season Factory, von der alle anderen erben
    + Frühling, Sommer, Herbst und Winter Factory
    + Je nach Season werden Objekte mit anderen factories erzeugt

### World  Creator

+ Grafik des Levels -> Automatisch von LIBGDX geladen
  + Keine Hitboxen/ Collisionen
  + Keine Objekte
  + Keine Gegner
+ Positionen für Hitboxen/ Objekte/ Gegner in Tiled festgelegt
  + World creator erzeugt je nach ebene in tiled hitboxen/ Objekte

### Contact Listener

+ Funktion: Erkennt was mit wem kollidiert und legt fest, was deshalb passieren soll
+ Erklärung:
  + Wird bei jeder kollision aufgerufen (Bsp.: Spieler prallt auf Gegner, zwei Gegner prallen aufeinander)
  + Checkt, welche zwei Objekte involviert sind
  + Ruft je nach objekten eine Funktion auf (Bsp.: Zwei Gegner prallen aufeinander -> Beide laufen in die andere Richtung weiter)

## Zuständigkeiten

+ 