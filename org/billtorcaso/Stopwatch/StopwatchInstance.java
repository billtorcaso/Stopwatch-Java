package org.billtorcaso.Stopwatch;

//import java.lang.Long;
import java.lang.System;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentNavigableMap;
//import java.util.concurrent.ConcurrentSkipListMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.TimeUnit;

/**
 * This class provides class-static services for a shareable singleton Stopwatch object.
 * <p>The shared stopwatch object allows legacy libraries to use this Stopwatch for timing, without adding another
 * parameter to the legacy API.
 * <p>There is one accessor method that returns a reference to the shared Stopwatch object, 
 * and three convenience methods for recording moments
 * and tasks.  A stopwatch operation that is not listed below must
 * use the shared Stopwatch instance returned from <code>get()</code> to call other Stopwatch methods.
 * <pre>
 *
 * <code>StopwatchInstance.get()</code>
 * <code>StopwatchInstance.taskBegin()</code>
 * <code>StopwatchInstance.momentAdd()</code>
 * <code>StopwatchInstance.taskEnd()</code>
 * </pre>
 *
 * @see <a href="Stopwatch.html">Stopwatch</a>
 * @see <a href="StopwatchFactory.html">StopwatchFactory</a>
 * @author Bill Torcaso
 */
@ThreadSafe
public final class StopwatchInstance {

    /**
     * This object is used only to guard synchronization
     * of the shared instance.
     */
    private static Object sharedInstanceGuard = new Object();

    /**
     * This is the singleton instance
     */
    @GuardedBy("sharedInstanceGuard")
    private static Stopwatch sharedInstance;

    // Note: static initializer.  So there is a shared stopwatch
    // as soon as this class gets loaded.
    {
        sharedInstance = get();
    }

    /**
     * The constructor of <code>StopwatchFactory</code> is private,
     * in order to make the class a singleton with services provided
     * only through class-static methods.
     */
    private StopwatchInstance() {
        super();
    }

    /**
     * This class-static method is the only way to get access to the shared Stopwatch object.
     *
     * @return a reference to the shared Stopwatch object
     */
    @GuardedBy("sharedInstanceGuard")
    public static Stopwatch get() {
        synchronized (sharedInstanceGuard) {
            if (sharedInstance == null) {
                sharedInstance = StopwatchFactory.stopwatchCreate("Shared");
                //sharedInstance.momentRecord("SharedStopwatch:created");
            }
            return sharedInstance;
        }
    }

    /**
     * This class-static method is the only way to discard the existing
     * shared Stopwatch instance and create a new one.
     * <p>Note that any existing reference to the previously-shared
     * object instance will remain
     * valid. Libraries or modules with a reference to
     * that object will continue to use it.
     *
     * @return a reference to the new shared Stopwatch object instance
     */
    @GuardedBy("sharedInstanceGuard")
    public static Stopwatch release() {
        synchronized (sharedInstanceGuard) {
            if (sharedInstance != null) {
                sharedInstance.taskEnd("SharedStopwatch:release");
                sharedInstance = null;
            }
            // This is guaranteed to create a new shared object instance
            return get();
        }
    }

    /**
     * This class-static convenience method invokes Stopwatch.taskBegin(title) on the shared Stopwatch instance.
     *
     * @param title a {@link java.lang.String} object.
     * @see <a href=Stopwatch.html#taskBegin>taskBegin</a>
     * @return the <code>long</code> value returned from Stopwatch.taskBegin(title)
     */
    public static long taskBegin(String title) {
            return get().taskBegin(title);
    }

    /**
     * This class-static convenience method invokes Stopwatch.momentRecord(title) on the shared Stopwatch instance.
     *
     * @see <a href=Stopwatch.html#momentRecord>momentRecord</a>
     * @param title a {@link java.lang.String} object.
     * @return the <code>long</code> value returned from Stopwatch.momentRecord(title)
     */
    public static long momentRecord(String title) {
        return get().momentRecord(title);
    }

    /**
     * This class-static convenience method invokes Stopwatch.taskEnd(title) on the shared Stopwatch instance.
     *
     * @see <a href=Stopwatch.html#taskEnd>taskEnd</a>
     * @param title a {@link java.lang.String} object.
     * @return the <code>long</code> value returned from Stopwatch.taskEnd(title)
     */
    public static long taskEnd(String title) {
        return get().taskEnd(title);
    }


    /**
     * <code>main()</code> runs a small exercise of StopwatchInstance methods.
     * @param args an array of {@link java.lang.String} objects.
	 * <p>Run it as
	 * <pre>
	 *     $ java -jar Stopwatch.jar   org.billtorcaso.Stopwatch.StopwatchFactory;
	 *
	 *     or
	 *
	 *     $ java -cp .   org.billtorcaso.Stopwatch.StopwatchFactory;
	 * </pre>
     */
    public static void  main(String[] args) {

        System.out.println("Hello, world --  from StopwatchInstance.");
        System.out.println("StopwatchInstance does not have any tests yet.");
        System.out.println("But StopwatchFactory.java tests the shared stopwatch.");

    }

}
