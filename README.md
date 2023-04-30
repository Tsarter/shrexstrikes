# Shrex Strikes - java edition


## Warnings && known bugs
Doesn't work on mac m1 and m2. Fix, generate .jar file `gradlew desktop:dist`, open terminal as rosetta (intel architecture) and run it in terminal using  `java -jar -XstartOnFirstThread desktop-1.0.jar` 

## How to play?
1. Download the code from gitlab using `git clone https://gitlab.cs.ttu.ee/tatall/iti0301-2023.git`
2. Go to inellij and click on File -> Open
3. Navigate to the cloned folder and choose build.gradle in (IMPORTANT!) Client folder.
4. When promted, then choose `open as project`
5. There should only be Client files in your intellij project. If you see Server files then something went wrong.
6. Make sure you are on the main branch by using `git checkout master` in intellij terminal
7. Make sure you have the latest version by using `git pull` in intellij terminal
8. Go to file desktop -> src -> org.example -> DesktopLauncher
9. The run icon should turn green. Click on it.
    If it doesn't run and you are on a mac then you need to do some extra configurations.
    "On macOS your application needs to be started with the -XstartOnFirstThread JVM argument"
10. Start playing by clicking Start Game inside the game window.

Keys:
 - SpaceBar -> Jump
 - A, W, S ,D -> moving around
 - esc -> pausing the game (only in zombie mode)


## What is in the game?

Two game modes:
- Zombies (Endless waves of zombies who try to kill you)
- 1 versus 1 (Fight against each other until time runs out, player with most kills win)

Other stuff:
- In settings you can change your username, sound level, player.
- Shrex Green - option to subscibe to shrex green and receive green badge and no ads, extra health, damage, speed.



## How to manage the Server on Taltech server 

ssh to the server and delete the old file `ssh ubuntu@193.40.156.227`

push the Server.jar file to server `scp Server-1.0.jar ubuntu@193.40.156.227:gameServer/`

log into server using `ssh ubuntu@ip_address`

run it in the background using nohup `nohup java -jar Server.jar &`

check the output using nano or cat `nano nohup.out` or `cat nohup.out`

kill the server by first getting the PID by `ps aux | grep Server-1.0.jar`

then kill it by `kill id_of_the_process_aka_PID` 

exit ssh by using `exit`


