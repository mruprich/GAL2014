GAL2014
=======

Repository for school project - implementation of Fleury's algorithm in C/C++

Eulerovsky graf - existuje v nem eulerovsky tah, tzn tah prochazejici vsemi hranami prave jenou
Theorem: "Neorientovany graf G ma eulerovsky tah, prave kdyz je G souvisly a pocet vrcholu s lichym stupnem je 0 nebo 2"
    -je to proto ze bud je tah uzavreny (0 vrcholu s lichym stupnem)
    -nebo se v jednom zacne, v druhem skocni
    
Corollary: "Orientovany graf G ma eulerovsky tah, prave kdyz je G souvisly a pocet vrcholu s lichym stupnem je 0 nebo 2"
    -Orientovana kostra grafu G=(V,E) je orientovany strom T=(V,E') s korenem u z V, kde E' je podmnozinou E a d+(u)=0 a        d+(v)=1 pro vsechna v z V-{u}
    -Vyvazeny graf G=(V,E) je orientovany graf s d+(u)=d-(u) pro vsechny u z V
    
    Je-li G souvisly a vyvazeny orientovany graf s orientovanou kostrou T s korenem u, pak eulerovsky cyklus v reverznim       smeru lze zjistit nasledovne:
        a) Zacneme libovolnou hranou incidentni k u
        b) Dalsi hrany vybirame jako incidentni k aktualnimu uzlu takove, ze
            i) hrana jeste nebyla vybrana
            ii) zadna hrana z T se nevybere, dokud lze vybrat jinou
        c) Hledani konci, kdyz aktualni uzel nema incidentni zadne nepouzite hrany

Stupen  - pocet incidentnich vrcholu - u neorientovaneho grafu
        - u orientovaneho se berou 2 stupne: -vystupni stupen a vstupni stupen





Postup pro GIT:

1. Vzdy pred zacatkem prace dat git pull
2. Po editaci nekterych souboru dat git commit -am "nejaka zprava o commitu"
3. nakonec po 2. dat git push

