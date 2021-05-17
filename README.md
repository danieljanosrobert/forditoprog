# Szkript nyelv megvalósítása AST generálással.

A nyelv utasításai (amelyek `;`-vel záródnak) a következök legyenek:

- Egy kifejezés a következö operátorokkal/müveletekkel, megfelelö precedenciákkal: `+`, `-`, `*`, `/`, `(`, `)`, `ABS()`, `?:`, `=`, `==`, `<`, `>`. Az utolsó 3 logikai kifejezés értéke `0` ha nem igaz, `1` ha igaz.
- Legyen benne `print(...)` utasítás, ami a paraméterül kapott kifejezést, vagy kifejezéseket kiírja.
- A  kifejezés levelében lehet szám (egész és tizedes tört is), lehet változónév (ha eddig nem volt neki érték adva, akkor az értéke legyen 0), és lehet `TIME` is, ami az aktuális idö (a UNIX idö, azaz az 1970 óta eltelt másodpercek száma).
- Lehessen benne változókat deklarálni `int` vagy `double` típusokkal (csak ez a kettö).
- Legyen benne `for` és `while` ciklus C szerü szintaktikával, `break` és `continue`.
- Legyen benne `if` és `switch`-`case`-`default` utasítás is.

## Példa szkript

```
int starttime;
int sum;
int x;
starttime = TIME;
sum = 0;
for (x=0;x<1000;x=x+1) {
  sum = sum + x;
}
print(sum);
print(TIME-starttime);
```

*Az elemzö csak felépítse az AST-t, a végrehajtás pedig ezen az AST-n egy külön fázisban történjen.

# Futtatás `antlr.jar`-rel

*Windows környezeten ajánlott a futtatást WSL2 Ubuntu környezeten végrehajtani.*

## Elökészületek

Mappa létrehozása, ahol a futtatás történni fog, belépés a mappába

```
mkdir ../project-run
```

## Fordítás

```
rm -r ../project-run/*.class ; rm -r ../project-run/ast/*.class ; rm -r ../project-run/ast/*$*.java ; cp -a . ../project-run/ ; java -jar ../project-run/antlr.jar ../project-run/ScriptLexer.g4 ../project-run/ScriptParser.g4 ; javac -cp .:../project-run/antlr.jar ../project-run/*.java;
```

## Futtatás

```
clear; java -cp "../project-run/antlr.jar:../project-run/" ScriptParser input.txt
```

## Script újragenerálása

```
clear; java -cp "../project-run/antlr.jar:../project-run/" ScriptParser input.txt --generate
```