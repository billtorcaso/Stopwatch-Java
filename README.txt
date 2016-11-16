13-March-2015
Bill Torcaso
torcasobill@gmail.com

This Java code contains a thread-safe Stopwatch object that is useful 
for performance testing.

The idea is that the Stopwatch captures named moments for later review.  
Multiple threads can record moments on the same Stopwatch.  The Stopwatch 
never does I/O, so it has minimal impact on run-time.

You can nest beginning and ending "tasks", like this:

000000168000	TaskBegin:Genus
000000171000	| Pan (all chimpanzees)
000000176000	| TaskBegin:Species
000000183000	| | Troglodytes
000000187000	| | TaskBegin:Movie Stars
000000189000	| | | Cheetah - Tarzan's companion
000000195000	| | TaskEnd:Movie Stars
000000201000	| | TaskBegin:Subspecies
000000208000	| | | schweinfurthi
000000212000	| | | TaskBegin:Alpha Male
000000215000	| | | | Frodo - Jane Goodall's companion
000000220000	| | | TaskEnd:Alpha Male
000000223000	| | TaskEnd:Subspecies
000000227000	| TaskEnd:Species
000000230000	TaskEnd:Genus

The Stopwatch will give you a sequential List<Moment> anytime you ask for it.

I typically let a program run to completion and then use one of these methods
to eyeball the hot spots:

    prettyPrint(taskRelative)

	prettyPrint(previousMomentRelative)

You can create many independent Stopwatch objects.  For profiling legacy code,
there is singleton Stopwatch available through the factory.  This means 
that you can have a legacy library generate some timings, and easily print
them from main().

There are other display services:

    creationMomentRelative
	nanoTimeValues
	toStringAsIntervals
	toString

The Stopwatch records in nanosecond units.  The precision depends on 
the underlying host and its clock.

StopwatchFactory.java has a main() to run tests. See Test-output.txt.
Start it this way:

    java -cp . org.billtorcaso.kutils.StopwatchFactory

There is full javadoc in the apidocs/ directory.

Feedback welcome.
