# Shrex Strikes - java edition

3d Multiplayer game where the end goal is to get as many kills as possible.

## Käima panemine
Intellij-s Get from VCS. Kopeeri gitlabist HTTP link. Clone.

Gradle pakub all paremal valikut ["Gradle build scripts found"](https://i.imgur.com/I0cSoiq.png)

Vajuta "Load"

Mingi .java faili peale minnes peaks tulema ka valik "Setup SDK". Programm on testitud java18-ga aga arvatavasti töötab ka teiste versioonidega. Seejärel saad käima panna `Client -> desktop -> src -> org.example -> DesktopLauncher `. Kui ühendus ei õnnestu, siis ilmselt on Taltechi server maas.

[Nii](https://i.imgur.com/0ANqn39.png) on ka võimalik mitu clientit korraga tööle panna, et [samal ajal](https://i.imgur.com/3XYjNmh.png) kahes või rohkemas clientis korraga mängida. (mis on ka multiplayeri mõte)


## Mängu sisu
In main menu choose your map, game mode, character, double damage, etc. 
When you are ready, then click the play button. Then you will be forwarded to lobby,
where you will need to wait for other players to join the game.

For moving around use AWSD. For looking around use your mouse. Mouse gets captured when you use the ESC key.



## How to manage the Server on Taltech server


push the Server.jar file to server `scp Server.jar ubuntu@ip_address:gameServer/`

log into server using `ssh ubuntu@ip_address`

run it in the background using nohup `nohup java -jar Server.jar &`

check the output using nano or cat `nano nohup.out` or `cat nohup.out`

kill the server by first getting the PID by `ps aux | grep Server.jar`

then kill it by `kill id_of_the_process_aka_PID` 

## Allpool on näite projekti asjandused, pole otseselt seotud Shrex Strikes mänguga



## Mis mängu kapoti all toimub?

### Lihtsõnaliselt
Client saadab seda, mida ta teha tahab. Server mõtleb välja tema uue asukoha ja siis saadab uuendused kõigile mängijatele.

### Natukene täpsemalt
- Serverit tööle pannes ta hakkab spetsiifilistel portidel (siin 3000 ja 3001) "kuulama".
- Seejärel clientit käima pannes on vaja öelda, mis aadressile (localhost) ja mis portidesse ühenduda.
- Kui client ühendub, siis server teeb talle uue "Player" objekti, mis hoiab selle spetsiifilise mängija asukohta ja on seotud selle mängija IP-ga läbi hashmap-i. (pythonis dictionary)
- Kui mingi mängija soovib kuskile liikuda, siis ta peab serverile saatma, et kuhu. Ta saadab 1 Characteri. (N E S W suuna) Selleks kasutatakse Client-i pool ka [scannerit](https://i.imgur.com/NRTyZ3E.png), et saab konsooli kirjutada liikumissuuna. Seejärel see saadetakse serverisse.
- Seejärel server liigutab seda mängijat. (player klassis `move()`)
- Iga kord, kui mängus midagi muutub ([keegi tuleb juurde](https://i.imgur.com/bmVnBJE.png) / [keegi liigub](https://i.imgur.com/WaWN5Ka.png) / [keegi lahkub](https://i.imgur.com/pvCaUrX.png)), siis server [saadab kõikidele mängijatele uue listi](https://i.imgur.com/RK69ay9.png), mille sees on iga mängija asukohad. Nii saab iga mängija omale info isegi siis, kui mängus liigub ka keegi teine.
- Kui clientile [saadetakse](https://i.imgur.com/Z1XA5IQ.png) uus "uuendus", siis ta saab mängijate asukohtade infoga uuesti omale [mänguvälja](https://i.imgur.com/0nqBszu.png) joonistada.
- Parim viis sellest kõigest aru saada on arvatavasti natukene selle koodiga eksperimenteerida.

### Gradle
build.gradle all on selline rida, mis impordib kryoneti 
`implementation group: 'com.esotericsoftware', name: 'kryonet', version: '2.22.0-RC1'` Peale selle lisamist peaks gradles ka projekti rebuild-ima, kui koodis ikka "kõik punane on".

## Miks sellised valikud ja mida veel teada
Kindlasti saab siin palju asju teha paremini, aga see on väga lihtne näide paljude kommentaaridega, et oleks arusaadavam alustada. Järgnev jutt on pigem lihtsõnaline, et vähem segadust tekitada.

### TCP/UDP
Siin näites saadame infot UDP-ga, mis on küll kiirem, aga ei ole garanteeritud, et alati jõuab kohale. TCP-ga peaks saatma tähtsamat informatsiooni (nt registreerimine, nime saatmine, mängu tulemused vms). TCP-ga peavad mõlemad - info vastuvõtja ja saatja nõustuma, et info on kohale jõudnud. 

### Mis infot saata serveri ja clienti vahel
Multiplayeris mängu ajal info saatmisel peab kindlasti vaatama, et saadetud klassid oleksid mälu mahult võimalikult väiksed. 
Selle asemel, et saadame serverist kogu kaardi mängijale, saadame siin näites ainult mängijate asukohad koordinaatidena. 
Siis kui maailmakaart oleks 10x10 asemel 10000x10000, siis ei ole mõtet saata kogu kaarti, sest suhtlus läheks väga aeglaseks. 
Samuti pole mõtet kindlasti saata pidevalt suuri libGDX objekte või mingeid graafikaid/tekstuure. (Kui on mingi ühekordne saatmine, siis on ok. Aga reaalajas mängud tihti vahetavad infot 10+korda/sekundis)

Küll hiljem, aga millalgi tuleb ka arvestada sellega, et localhost-is on infovahetus oluliselt kiirem (paar millisekundit) võrreldes välises arvutis jooksva serveriga (~40-200?ms)

Ehksiis on tähtis, et *läbi kryoneti saadetakse eelkõige primitiivseid andmetüüpe*.

Loomulikult, mida rohkem mänguarvutusi on võimalik teha ära clientis, seda parem. 
- Näide: Keegi laseb mingisuguse kuuli. On teada kuuli algalguskoht ja kiirus. Selle asemel, et server pidevalt uuendab kuuli asukohta koordinaatide saatmisega, võib ta tegelikult saata ainult 1 korra, et "nüüd lendab kuul siit sinna selle kiirusega". Siis saab iga client ise seda arvutada, et kus see mingi hetk olema peaks. (Aga lõpuks võiks ikka server öelda, kellele/kas see pihta läks.)


### Oma projekti multiplayeri tegemine/lisamine
Esimesena oleks mõtekas näiteks planeerida ära, et mis info on clientil ja mis info on serveril.

Kõige mõtekam oleks teha 2 eraldi projekti serveri ja clienti jaoks. (Või siis 2 erinevat kausta, nagu siin repos on. Tähtis on, et need on eraldiseisvad ja ühenduvad ainult läbi kryoneti). Arvatavasti clienti projekti on mõtekaim alustada [libgdx setupiga](https://libgdx.com/wiki/start/project-generation). Serveri projekt võib olla kasvõi puhas gradlega java projekt.

*Ärge tehke enne mängu clientis täiesti valmis ja siis hakkate mõtlema serveri peale.* Tihti tuleb ette, et serveri "lisamine" vajab päris palju ümberkirjutamist. 

Kui sobib, siis täitsa mõtekas on alguses kasvõi kopeerida selle näite kood ja proovida seda kuidagi oma projektiga seostada. Clienti osa peaks lisama libgdx-ile juurde ja serveri osa jaoks tegema siis näiteks uue projekti. Kui antud näide tundus liiga primitiivne, siis [siin](https://github.com/EsotericSoftware/kryonet/tree/master/examples/com/esotericsoftware/kryonet/examples) on rohkem näiteid sarnase struktuuriga.

Arvatavasti läheb teil vaja rohkem erinevate tüüpide andmete saatmist, kui ainult 1 playerite list ja 1 tähemärk(Character). (Sel juhul tuleb ka rohkem klasse registreerida Network.java-s) Et saada teada, mida `received` funktsioon vastu võttis, võib kasutada näiteks `if (object instanceof [sometype])` et saada teada, mis tüüpi andmeid(klass) saadeti.

### Mis vahe on clientil ja serveril
Iga mängija on 1 client. Kõikide clientite peale on kokku 1 server.

Client võiks teoorias olla pigem visuaalne. Client näitab(joonistab nt libGDX-iga) inimesele, et mis mängus toimub. Kõik visuaalsed ja häälelised efektid toimuvad clienti pool. Samuti loetakse seal ka nuppe/hiirt/?, et mida mängija teha tahab.
Client iseenesest tegelikult teabki ainult seda, mida üks mängija teha tahab. (Ja seda, mida server talle "ütleb")

Serveril peaks olema ülevaade kogu mängust ja kõikidest mängijatest. Põhimõtteliselt tekib selline skeem, et iga client saadab enda kohta infot ja siis server kogub kõik kokku ja mõtleb, missugune peaks mingis hetkes mäng olema. Siis ta jagab seda clientitega.

Serveril pole otseselt vaja midagi "joonistada", kuna see tulevikus jookseb kuskil konsoolis. Aga kui peate vajalikuks (näiteks debug-i jaoks) siis saab ka serveris mingit pilti enda jaoks teha.
