This is the code I've worked on to create my game "Babbles The Frog". I'm basing it off an old joke my sister 
made so if you're wondering why I would make this, there you go. 

The files included in this repository are all the xml, java, gradle and otherwise important files for functionality 
and running a build. This repository does not however contain any of the background images, sprites or sound effects
and you will need to use your own to run the game as it does not function without them. 

In general the idea is to use the PlatformView class as an engine to run the game with all the other classes. Some 
classes will be used as objects to represent characters such as Babbles. Others are used to hold schematics for levels
or the data for objects to place in a level. The PlatformView class reads all these in to produce the game with the help 
of other classes such as the Animation class for animating sprites.
