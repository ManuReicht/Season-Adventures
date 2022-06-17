# Season Adventures

## Installation und Ausführen
1. Zip herunterladen oder Klonen
2. in IntelliJ öffnen
3. Gradle Sync (automatisch)
4. Main-Klasse: DesktopLauncher
   * Pfad: Season-Adventures/desktop/src/at/htlkaindorf/desktop/DesktopLauncher.java
5. Run Configuration
   * Application
   * Main: at.htlkaindorf.desktop.DesktopLauncher
![image](https://user-images.githubusercontent.com/86600051/174284197-2c808116-1406-4224-aeee-0f45ec5e18dc.png)

## Trello
https://trello.com/b/0HgkEtQq/season-adventures

## Zeitaufzeichnung
https://docs.google.com/spreadsheets/d/1_x5mME89AK8lcQwUQ1XozdxPVGa0oh3o4wjBLSRYDFk/

## Libgdx - Game Engine
https://libgdx.com/

## Richtlinen für Levelerstellung (Ebenen)

1. Kachelebenen
    * ID 1: Background (0)
    * ID 2: Graphic Layer (1)
    * ID 3: Foreground (2)
    * ID 14: Dummy Objects (Orientierung) (visible="0") (13)
2. Objektebenen
    * ID 4: Terrain (3)
    * ID 5: Standard Collectibles (4)
    * ID 6: Special Collectibles (5)
    * ID 7: Walker (Gegner 1) (6)
    * ID 8: Gegner 2 (7)
    * ID 9: Gegner 3 (8)
    * ID 10: Movable Platform y (9)
    * ID 11: Movable Platform x (10)
    * ID 12: Trap (11)
    * ID 13: Level End (12)

### Laden der IDs
* ID -1
* Es wird die Reihenfolge in der Liste genommen (beginned bei 0)
