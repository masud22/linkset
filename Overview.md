This is an overview of a LinkSet library

# Introduction #

Java is a great language. A much cleaner than a C#. But it lags behind in the area of listeners. Every time I do some programming in C#, I thought "I wish Java had delegates".
I also liked the Qt signal/slot approach.

Some time ago I thought "Can I create something cleaner and easyer to use than those horrible anonymous classes?".

This way LinkSet was born.

LinkSet is a little "nobody knows, nobody cares" library that utilizes Java 5 features to provide a simle to use mechanism of listeners that doesn't require anonymous classes.


# Features of LinkSet #

  * Simple to use (no listener interfaces required, no special build steps needed).
  * Flexible (both instance and static methods can be event handlers).
  * Does not enforse a coding style (event handling methind can be private).
  * Easy to learn (only a few intuitive classes to use).
  * Small and reasonably fast.
  * Free (LGPL licence).