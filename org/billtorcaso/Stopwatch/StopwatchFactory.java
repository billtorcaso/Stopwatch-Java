package org.billtorcaso.Stopwatch;

import java.lang.Long;
import java.lang.System;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
//import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

/**
 * This class is the factory for Stopwatch objects. 
 * <p>Use the class-static method 
 * <code>Stopwatch.stopwatchCreate()</code> to get a Stopwatch object.
 * <p>Use the class-static method 
 * <code>Stopwatch.stopwatchCreateNoOp</code> to get a no-op Stopwatch object. Use this when you
 * do not want to record anything but your code is written to make Stopwatch calls.
 *
 * @see <a href="Stopwatch.html">Stopwatch</a>
 * @see <a href="StopwatchInstance.html">StopwatchInstance</a>
 * @author Bill Torcaso
 */
@ThreadSafe
public final class StopwatchFactory {

	/**
	 * The constructor of <code>StopwatchFactory</code> is private,
	 * in order to make the class a singleton with services provided
	 * only through class-static methods.
	 */
	private StopwatchFactory() {
		super();
	}

	/**
	 * This factory method is the only public way to create a functioning Stopwatch object.
	 * The returned object is a Stopwatch object with its <code>title</code> set to "Creation:" + title
	 * @param title String title of this Stopwatch
     * @return a reference to a Stopwatch object
	 */
	public static Stopwatch stopwatchCreate(String title) {
		return new StopwatchImpl(title);
	}

	/**
	 * This factory method is the only public way to create a non-functioning, placeholder Stopwatch object.
	 * The returned object is a Stopwatch object, and the return values 
     * from its methods are always <code>0L</code>, <code>""</code>, or an empty <code>List&lt;Moment&gt;</code>.
     * @param title String title for this Stopwatch
     * @return a reference to a Stopwatch object
	 */
	public static Stopwatch stopwatchCreateNoOp(String title) {
		return new StopwatchNoOpImpl(title);
	}

	/**
	 * <code>main()</code> runs a small exercise of Stopwatch methods.
	 * It writes to System.out,
	 * and you are expected to compare the output with a known-good text file.
	 * <p>Run it as
	 * <pre>
	 *     $ java -jar Stopwatch.jar   org.billtorcaso.Stopwatch.StopwatchFactory;
	 *
	 *     or
	 *
	 *     $ java -cp .   org.billtorcaso.Stopwatch.StopwatchFactory;
	 * </pre>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void  main(String[] args) {
		//
		System.out.println("\nHello, world --- from StopwatchFactory.java");
		System.out.println("\nNanoseconds displayed as %012d should be read as follows:");
		System.out.println("\n\t012987654321");
		System.out.println("\n\t12 seconds\t987 milliseconds\t654 microseconds\t321 nanoseconds");
		System.out.println("\nExpect: a few 10-second delays during timing ops,\nand then output from various Stopwatch methods.");

		Stopwatch ticker = null;

		try {

			// prettyPrint
			System.out.println("\nNow testing the pretty-print services.");
			System.out.println("\nExpect: deeply indented Task Begin/End titles from prettyPrint()");

			ticker = StopwatchFactory.stopwatchCreate("Linnaean Classification");
			ticker.taskBegin("Kingdom: Animalia ");
			ticker.taskBegin("Phylum: Chordata");
			ticker.taskBegin("Subphylum: Vertebrata");
			ticker.taskBegin("Class: Mammalia");
			ticker.taskBegin("Subclass: Theria");
			ticker.taskBegin("Infraclass: Eutheria");
			ticker.taskBegin("Order: Primates");
			ticker.taskBegin("Suborder: Anthropoidea");
			ticker.taskBegin("Superfamily: Hominoidea");
			ticker.taskBegin("Family: Hominidae");
			ticker.taskBegin("Genus: Homo");
			ticker.taskBegin("Species: Sapiens");
			ticker.taskBegin("People At Knome");
			ticker.momentRecord("Me - Bill Torcaso");
			ticker.taskEnd("People At Knome");
			ticker.taskEnd("Species");
			ticker.taskEnd("Genus");
			ticker.momentRecord("Notice a different style of Begin-Moment-End below here.");
			ticker.taskBegin("Genus");
			ticker.momentRecord("Pan (all chimpanzees)");
			ticker.taskBegin("Species");
			ticker.momentRecord("Troglodytes");
			ticker.taskBegin("Movie Stars");
			ticker.momentRecord("Cheetah - Tarzan's companion");
			ticker.taskEnd("Movie Stars");
			ticker.taskBegin("Subspecies");
			ticker.momentRecord("schweinfurthi");
			ticker.taskBegin("Alpha Male");
			ticker.momentRecord("Frodo - Jane Goodall's companion");
			ticker.taskEnd("Alpha Male");
			ticker.taskEnd("Subspecies");
			ticker.taskEnd("Species");
			ticker.taskEnd("Genus");
			ticker.taskEnd("Family");
			ticker.taskEnd("Superfamily");
			ticker.taskEnd("Suborder");
			ticker.taskEnd("Order");
			ticker.taskEnd("Infraclass");
			ticker.taskEnd("Subclass");
			ticker.taskEnd("Class");
			ticker.taskEnd("Subphylum");
			ticker.taskEnd("Phylum");
			ticker.taskEnd("Kingdom");
			ticker.taskEnd("The-End");

			String prettyString = null;
			prettyString =
					ticker.prettyPrint(Stopwatch.IntervalReportType.taskRelative);
			System.out.println("\nHere is prettyPrint(taskRelative):\nExpect 'TaskBegin' to report 0 and 'TaskEnd'\nto report elapsed time since its matching task began.\nThe final time is elapsed-time for the whole stopwatch\n----------\n");
			System.out.println(prettyString);

			prettyString = 
					ticker.prettyPrint(Stopwatch.IntervalReportType.previousMomentRelative);
			System.out.println("\nHere is prettyPrint(previousMomentRelative).\nExpect each timestamp to be a small number of microseconds after the one before it.");
			System.out.println( String.format("\n%s", prettyString));

			prettyString = 
					ticker.prettyPrint(Stopwatch.IntervalReportType.creationMomentRelative);
			System.out.println( "\nHere is prettyPrint(creationMomentRelative):\nExpect each timestamp to increase from the beginning.\nThe difference of two timestamps is the elapsed time between them\n");
			System.out.println(prettyString);

			prettyString =
					ticker.prettyPrint(Stopwatch.IntervalReportType.nanoTimeValue);
			System.out.println("\nHere is prettyPrint(nanoTimeValue):\nExpect full nanosecond counts (ugly) that always increase.");
			System.out.println(prettyString);

			System.out.println(String.format("\nHere is the plain toString() of the Linnaean stopwatch"));
			System.out.println(ticker.toString());

			// Shared stopwatch tests (minimal)
			Stopwatch ticker2 = null;
			Stopwatch ticker3 = null;

			System.out.println("Now testing the shared Stopwatch services ....");

			System.out.println("\nExpect: two different objects to call printyPrint(), and have identical results");
			ticker2 = StopwatchInstance.get();
			ticker3 = StopwatchInstance.get();

			ticker = StopwatchInstance.get();
			ticker.taskBegin("SharedStopwatch:testing begins");
			ticker.taskBegin("SharedStopwatch:1:starting a task");
			ticker.momentRecord("SharedStopwatch:testing a moment");
			ticker.momentRecord("SharedStopwatch:testing another moment");
			ticker.taskEnd("SharedStopwatch:1:task ends");
			ticker.taskEnd("SharedStopwatch:testing ends");

			System.out.println("\nHere are two shared Stopwatch references output from prettyPrint():\nAre they identical?\n");
			System.out.println("ticker2:\n----------\n" + ticker2.prettyPrint());
			System.out.println("ticker3:\n----------\n" + ticker3.prettyPrint());

			System.out.println("\nTesting StopwatchInstance.release() using assertions.  No Exception means success.");
			StopwatchInstance.release();
			ticker = StopwatchInstance.get();
			assert(ticker != ticker2 && ticker2 == ticker3);
			System.out.println("No Exception - that means success.\nEnd of tests for shared stopwatch");

			// Test taskRelative on a small sequence
			System.out.println("\nSimple comparison of creationMomentRelative and previousMomentRelative");
			ticker = StopwatchFactory.stopwatchCreate("Greek letters");
			ticker.taskBegin("alpha");
			TimeUnit.SECONDS.sleep(1);
			ticker.taskBegin("beta");
			TimeUnit.SECONDS.sleep(2);
			ticker.momentRecord("gamma");
			TimeUnit.SECONDS.sleep(3);
			ticker.taskEnd("delta");
			TimeUnit.SECONDS.sleep(4);
			ticker.taskEnd("epsilon");
			ticker.taskEnd("Greek letters");

			System.out.println(
					String.format("\nHere is prettyPrint():\nNote that the seconds go 0,0,1,3,6,10,10\n----------\n%s", 
							ticker.prettyPrint()));


			System.out.println(
					String.format("\nHere is toString():\nNote that the seconds go 0,0,1,2,3,4,0\n----------\n%s", 
							ticker.toString()));

			System.out.println(
					String.format("\nHere is momentsRetrieve(taskRelative).toString():\n----------\n%s", 
							ticker.momentsRetrieve(Stopwatch.IntervalReportType.taskRelative).toString()));

			System.out.println(
					String.format("\nHere is momentsRetrieve(creationMomentRelative).toString():\nExpect seconds as 0,0,1,3,6,10,10\n----------\n%s", 
							ticker.momentsRetrieve(Stopwatch.IntervalReportType.creationMomentRelative).toString()));

			System.out.println(
					String.format("\nHere is momentsRetrieve(previousMomentRelative).toString():\nExpect seconds as 0,0,1,2,3,4,0\n----------\n%s", 
							ticker.momentsRetrieve(Stopwatch.IntervalReportType.previousMomentRelative).toString()));

			System.out.println(
					String.format("\nHere is momentsRetrieve(nanoTimeValue).toString():\nExpect increasing 12+ digit numbers\n----------\n%s", 
							ticker.momentsRetrieve(Stopwatch.IntervalReportType.nanoTimeValue).toString()));

			System.out.println(
					String.format("\nHere is plain toString():\nExpect seconds as 0,0,1,2,3,0\n----------\n%s", 
							ticker.toString()));

			System.out.println("\n--------------------\nThat's all, folks!");
		}
		catch (InterruptedException ie) {
			System.err.println(String.format("\nGot interrupted; what's up with that? |%s|", ie.toString()));
			System.exit(1);
		}

		System.exit(0);
	}
}

/**
 * This class represents a "moment", with a long integer value like that returned from System.nanoTime()
 * and a String title.
 *
 * A MomentImpl has accessors <code>whenGet()</code> and <code>titleGet()</code>.
 *
 * MomentImpl has its own toString() method, which return the two values in (something like)
 * this format: "%012d\t%s", where the first value is for <code>when</code> and the 
 * second value is for <code>title</code>.
 *
 * A Moment object becomes visible to a caller <u>only</u> as an element in the List returned
 * from <code>momentsRetrieve()</code>.
 */
@Immutable
final class MomentImpl implements Stopwatch.Moment {

	/**
	 * Records the value of System.nanoTime()
	 */
	private long when;

	/**
	 * Records the user-supplied title
	 */
	private String title;       

	/**
	 * Cached value for toString()
	 */
	@GuardedBy("asStringGuard")
	private String asString;    //  Cached value for toString()

	/**
	 * This object exists only to guard synchronization of "asString".
	 */
	private static final Object asStringGuard = new Object();

	public MomentImpl(long when, String title) {
		/**
		 * <p>titleGet.</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		this.when = when;
		this.title = title;
	}

	public long whenGet() {
		/**
		 * <p>toString.</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		return this.when;
	}

	public String titleGet() {
		return this.title;
	}

	@GuardedBy("asStringGuard")
	public String toString() {
		synchronized (asStringGuard) {
			if (asString == null) {
				asString = String.format("%012d\t%s", when, title);
			}
		}
		return asString;
	}
}

/**
 * This class implements the Stopwatch interface.
 * @see <a href="Stopwatch.html">Stopwatch</a>
 *
 */
@ThreadSafe
final class StopwatchImpl implements Stopwatch {

	/**
	 * Object "recordedMoments": the storage for moments recorded by this Stopwatch.
	 * As a ConcurrentNavigableMap, <code>recordedMoments</code> is 
	 * thread-safe.  It is also navigable by its keys,
	 * which are the monotonically increasing values
	 * from System.nanoTime(), represented as Long objects.
	 */
	private final ConcurrentNavigableMap<Long, String> recordedMoments;

	/**
	 * This object exists only to guard synchronization to "recordedMoments".
	 */
	private static final Object recordedMomentsGuard = new Object();

	/**
	 * For efficiency, cache the value of "the moment of creation",
	 * "the firstMoment", and "the previous moment".
     * <p>Only "the previous moment" is mutable
	 */

	/**
	 * The time that the stopwatch was created, in timeNanos() form.
	 */
	@GuardedBy("recordedMomentsGuard")
	private long creationMoment = -1;

	/**
	 * The time that the stopwatch first recorded a moment or task, in timeNanos() form.
	 */
	@GuardedBy("recordedMomentsGuard")
	private long firstMoment    = -1;

	/**
	 * The time of the most-recently-recorded moment, in timeNanos() form.
	 * This is cached for efficiency only, and is used in momentAdd() and momentsRetrieve().
	 */
	@GuardedBy("recordedMomentsGuard")
	private long previousMoment = -1;

	/**
	 * Construct a StopwatchImpl object.
	 * @param title String title for this Stopwatch
	 */
	public  StopwatchImpl(String title) {
		synchronized (recordedMomentsGuard) {
			recordedMoments = new ConcurrentSkipListMap<Long, String>();
            if (title == null) {
                title = "";
            }
			momentRecord("Creation:" + title); // discard the return value
		}
	}

	/**
	 * Add a moment to the chronological set of moments that this Stopwatch records.
	 * Maintains the cache of creationMoment, firstMoment, and previousMoment.
	 * @param title String title of this Stopwatch
	 * @return the value of previousMoment.
	 *
	 */
	@GuardedBy("recordedMomentsGuard")
	private final long momentAdd(String title) {

        long thisMoment = System.nanoTime();

        long thisMomentRelative;

		synchronized (recordedMomentsGuard) {

			if (creationMoment == -1) {

				// Initialize creationMoment and previousMoment to 
				// thisMoment; one-time only
				creationMoment = previousMoment = thisMoment;
				thisMomentRelative = 0;
			}
			else if (previousMoment == creationMoment) {

				// Cache the value of firstMoment,
				// which is sometimes when this Stopwatch
				// begins to do real work; one-time only
				firstMoment = thisMoment;
				previousMoment = thisMoment;
				thisMomentRelative = 0;
			}
			else {
				// This is the regular case
				thisMomentRelative = thisMoment - previousMoment;
				previousMoment = thisMoment;
			}

			recordedMoments.put(thisMoment, (title == null)? "Moment" : title);
		}

        return thisMomentRelative;
	}

	/**
	 *
	 * Record a named moment.
	 */
	public long momentRecord(String title) {
		return momentAdd(title);
	}

	/**
	 *
	 * Record the beginning of a named task.
	 */
	public long taskBegin(String title) {
		return momentAdd("TaskBegin:" + title);
	}

	/**
	 *
	 * Record the end of a named task.
	 */
	public long taskEnd(String title) {
		return momentAdd("TaskEnd:" + title);
	}

	/**
	 * Return a List of Moment objects that represents the state of the Stopwatch
	 * at the time of the call.  This method is thread-safe and returns a
	 * copy of the Stopwatch state.  Changes in the list are not reflected in the
	 * Stopwatch itself.
	 * Note that if multiple threads use the same Stopwatch object, the List
	 * may contain Moments that "the current thread" did not create.
	 *
	 * @return a List containing Moment objects, in the order in which they
	 * were recorded by the Stopwatch.
	 * @param intervalType a IntervalReportType object.
	 */
	@GuardedBy("recordedMomentsGuard")
	public List<Moment> momentsRetrieve(IntervalReportType intervalType) {

        List<Moment> result = new ArrayList<Moment>();
        assert result != null;

        String title;

        // initialize "previous moment" to the creation moment, 
        // and other values follow naturally
        long previousMoment = creationMoment;  
        long thatMoment;
        long tempMoment;

        // 'stackOfTasks' is used to balance nested TaskBegin/TaskEnd pairs
        // and is relevant only for taskRelative operations
        Deque<Long> stackOfTasks = null;

        if (intervalType == Stopwatch.IntervalReportType.taskRelative) {
            stackOfTasks = new ArrayDeque<Long>(); 
        }

        synchronized (recordedMomentsGuard) {

            for (Long key: recordedMoments.keySet()) {

                thatMoment = key.longValue();
                title = recordedMoments.get(key);

                switch(intervalType) {

                    case nanoTimeValue:
                        // "thatMoment" already has the right value
                        // and "previousMoment" is not relevant
                        break;

                    case creationMomentRelative:
                        thatMoment -= creationMoment;
                        break;

                    case previousMomentRelative:
                        tempMoment = thatMoment - previousMoment;
                        previousMoment = thatMoment;
                        thatMoment = tempMoment;
                        break;

                    case taskRelative:

                        // These are the two types of taskBegin() moments
                        if (title.startsWith("Creation:") ||
                            title.startsWith("TaskBegin:")) {
                                thatMoment = 0;
                                stackOfTasks.addFirst(key);
                        }
                        // This is the one type of taskEnd() moment
                        else if (title.startsWith("TaskEnd")) {
                            Long taskBeginMoment = stackOfTasks.pollFirst();
                            if (taskBeginMoment != null) {
                                thatMoment -= taskBeginMoment;
                            }
                            else {
                                thatMoment = -1;
                            }
                        }
                        // It is a regular Moment and does not change indentation
                        else {
                            thatMoment = 0;
                        }
                        break;

                    default: 
                        throw new IllegalStateException(
                                     "Unknown Stopwatch.IntervalReportType value:" + 
                                     intervalType.toString());
                }

                result.add(new MomentImpl(thatMoment, title));

            }
        }
        
        return result;
	}

	/**
	 * Get the title string of the Stopwatch object 
     * that was given when the object was created.
	 *
	 * @return the title String of this Stopwatch
	 */

    @GuardedBy("recordedMomentsGuard")
	public String titleGet() {
		synchronized (recordedMomentsGuard) {
			return recordedMoments.firstEntry().getValue();
		}
	}

	/**
	 * Return a String representation of the Moments in this Stopwatch.
	 * Overrides <code>Object.toString()</code>.
	 *
	 * @return a String representation of the Moments in this Stopwatch, in
	 * <code>previousMomentRelative</code> format.
	 * @see <a href="Stopwatch.html#toStringAsIntervals">toStringAsIntervals</a>
	 */
	public String toString() {
        return toStringAsIntervals(Stopwatch.IntervalReportType.previousMomentRelative);
    }

    /**
     *
     * Return a String representation of the Moments in this Stopwatch, in which
     * the representation of Moments is selected from one of the
     * <code>Stopwatch.IntervalReportType</code> values: <code>nanoTimeValue</code>,
     * <code>previousMomentRelative</code>, or <code>creationMomentRelative</code>.
     * These values result in displaying the raw value returned from
     * System.nanoTime(), or the interval between a given moment and its
     * previous moment, or the interval between a given moment and the original moment.
     */
    public String toStringAsIntervals(Stopwatch.IntervalReportType thisReportType) {

        List<Moment> momentsList = momentsRetrieve(thisReportType);

        StringBuilder output = new StringBuilder();

        for (Moment m: momentsList) {
            output.append(m.toString());
            output.append("\n");
        }

        return output.toString();
    }
    /**
     *
     * Produce a nicely-formated text representation of the Stopwatch
     * and its recorded moments.
     * <p>This differs from <b>toString()</b> in that the text lines are
     * indented to show the nesting of TaskBegin and TaskEnd moments.
     * <p>There is an unspecified limit to depth of indentation.
     * <p> If the caller has not provided balanced calls to taskBegin() and
     * taskEnd(), the output will be complete but will not be pretty.
     * @param thisReportType one of the enum values from Stopwatch.IntervalReportType
     * @return a formatted text representation of the Stopwatch's state
     */

    public String prettyPrint(Stopwatch.IntervalReportType thisReportType) {

        List<Moment> momentsList = momentsRetrieve(thisReportType);

        int indentCount = 0;

        String title;

        // The length of this String is the limit of how much indenting we do.
        String indentSpacesTemplate = "| | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | ";

        String indentSpaces = null;

        StringBuilder outputHere = new StringBuilder();

        for (Moment m: momentsList) {

            title = m.titleGet();

            // These are the two types of taskBegin() moments
            if (title.startsWith("Creation:") ||
                title.startsWith("TaskBegin:")) {

                // Post-increment the indent count
                //  Unless we've hit the limit
                if (2*indentCount < indentSpacesTemplate.length()) {
                    indentSpaces = indentSpacesTemplate.substring(0, (2*indentCount));
                    indentCount++;
                }
            }
            else if (title.startsWith("TaskEnd:")) {

                // Pre-decrement the indent count
                //  Unless we've hit the limit
                if (indentCount > 0) {
                    indentCount--;
                    indentSpaces = indentSpacesTemplate.substring(0, (2*indentCount));
                }
            }
            else {
                // this moment does not change the indentation count
                indentSpaces = indentSpacesTemplate.substring(0, (2*indentCount));
            }


            outputHere.append(String.format("%012d\t%s%s\n", 
                                             m.whenGet(), 
                                             indentSpaces, 
                                             title));
        }

        return outputHere.toString();
    }

    /**
     * Produce a nicely-formated text representation of the Stopwatch
     * and its recorded moments, using the creationMomentRelative format.
     *
     * <p>If the caller has not provided balanced calls to taskBegin() and
     * taskEnd(), or the indentation limit has been exceeded,
     * the output will be complete but will not be pretty.
     *
     * @return a {@link java.lang.String} object.
     */
    public String prettyPrint() {
        return prettyPrint(Stopwatch.IntervalReportType.creationMomentRelative);
    }
}

/**
 * This class implements the Stopwatch interface but does nothing, 
 * so as to minimize the overhead of embedded calls on a Stopwatch.
 * <p>Return values are 0 for <code>long</code> values,
 * the empty string <code>("")</code> for String values, 
 * and a non-null, empty <code>List&lt;Moment&gt;</code> for list values.
 * @see <a href="Stopwatch.html">Stopwatch</a>
 *
 */
@Immutable
final class StopwatchNoOpImpl implements Stopwatch {

    /**
     * Construct a StopwatchNoOpImpl object.
     * 
     * @param title String value for the title of this Stopwatch
     */
    public  StopwatchNoOpImpl(String title) {
        super();
        //  title is ignored
    }

    /**
     * Add a moment to the chronological set of moments that this Stopwatch records.
     * Maintains the cache of creationMoment, firstMoment, and previousMoment.
     * @param title String title of this Stopwatch
     * @return 0L
     *
     */
    private final long momentAdd(String title) {
        return 0;
    }

    /**
     *
     * Record a named moment.
     */
    public long momentRecord(String title) {
        return momentAdd(null);
    }

    /**
     *
     * Record the beginning of a named task.
     */
    public long taskBegin(String title) {
        return momentAdd(null);
    }

    /**
     *
     * Record the end of a named task.
     */
    public long taskEnd(String title) {
        return momentAdd(null);
    }

    /**
     * Return an empty List of Moment objects.
     * <p>The <code>IntervalReportType</code>parameter is ignored.
     * @param intervalType a IntervalReportType object.
     * @return an empty List of Moment objects
     */
    public List<Moment> momentsRetrieve(IntervalReportType intervalType) {
        return new ArrayList<Moment>();
    }

    /**
     * Return the empty String ("").
     * @return the empty String ("")
     */
    public String titleGet() {
        return "";
    }

    /**
     * toString() returns the empty String ("").
     * @return the empty String ("")
     */
    public String toString() {
        return "";
    }

    /**
     * toStringAsIntervals() returns the empty String ("").
     * <p>The <code>IntervalReportType</code>parameter is ignored.
     * @param thisReportType an IntervalReportType enum value
     * @return the empty String ("")
     */
    public String toStringAsIntervals(Stopwatch.IntervalReportType thisReportType) {
        return "";
    }

    /**
     * prettyPrint() returns the empty String ("").
     * <p>The <code>IntervalReportType</code>parameter is ignored.
     * @param thisReportType an IntervalReportType enum value
     * @return the empty String ("")
     */
    public String prettyPrint(Stopwatch.IntervalReportType thisReportType) {
        return "";
    }

    /**
     * prettyPrint() returns the empty String ("").
     *
     * @return the empty String ("")
     */
    public String prettyPrint() {
        return "";
    }
}
