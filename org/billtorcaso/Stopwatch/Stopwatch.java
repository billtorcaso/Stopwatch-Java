package org.billtorcaso.Stopwatch;

import java.util.List;

/**
 * The <code>Stopwatch</code> interface records an sequence of named moments, with 
 * nanosecond intervals and minimal run-time overhead. 
 * <p>The original purpose was to provide hierarchical measures
 * of time spent doing I/O, but <code>Stopwatch</code> is not limited to that.
 * The typical use-case is to record many moments of interest and analyze them afterwards.
 *
 * <p>This Interface is (intended to be) thread-safe. Implementation
 * classes must be <code>final</code> in order to preserve
 * thread-safety.
 *
 * <p>A Stopwatch object provides only relative nanosecond measures.
 * As such it records <code>moments</code> and
 * can provide the elapsed time between two <code>moments</code>.
 * <p>A moment has a title, which is a String that identifies it.
 * It does not have to be unique among the titles of a Stopwatch.
 *
 * <p>A Stopwatch cannot be started, reset, or stopped.
 * Create another Stopwatch object if you get tired of the one you have.
 *
 * <p>A moment is represented in an immutable inner-class object named <code>Moment</code>.
 * A Moment object contains two accessors, one for the result of
 * System.timeNanos() and one for the title of the Moment. A Moment object can
 * only be instantiated by the Stopwatch method <code>momentsRetrieve</code>.
 *
 * <p>A <code>Moment</code> can be identified as the beginning of a <code>task</code>,
 * the end of a <code>task</code>
 * or just a plain moment.
 * The distinction between a moment,
 * a beginning, and an ending is only relevant to the prettyPrint()
 * method.
 * <p>A Stopwatch object does not enforce
 * any ordering among beginnings and endings.  If they are unbalanced, <code>prettyPrint</code>
 * will display complete information but lose the pretty indenting.
 *
 * <p>A Stopwatch object is a mutable object and can grow without
 * bounds.  It grows by calls to any of these methods:
 * <p><code>
 *     momentRecord()
 *     taskBegin()
 *     taskEnd()
 * </code>
 * <p>These methods reach return a <code>long</code> value that
 * is the elapsed time since the time of the previous recorded moment.
 * See {@link prettyPrint()} and {@link toString()} 
 * for other measures of the time between moments.
 *
 * <p>A Stopwatch object always records the moment of its creation, and the return
 * from the first moment-recording call will be the elapsed time
 * since the creation of the Stopwatch object.
 *
 * <p>Note that there is no service that returns the time elapsed between any two particular moments.  
 * You must either track those two moments yourself, or post-process the result of <code>momentsRetrieve</code>.
 * <p>By convention, implementation classes are forbidden
 * from providing constructors.  All concrete object implementations
 * of this Interface must be obtained from class-static factory methods.
 *
 * @see <a href="StopwatchFactory.html">StopwatchFactory</a>
 * @see <a href="StopwatchInstance.html">StopwatchInstance</a>
 * @author Bill Torcaso
 */
@ThreadSafe
public interface Stopwatch {

    /**
     * The <code>Moment</code> interface represents an immutable "moment in time", 
     * consisting of a long integer value and a String title.  
     * The long value is a relative number
     * of nanoseconds from an arbitrary reference point in the past.
     * In this it is "similar to" the return value from <code>System.nanoTime()</code>,
     * but there is no promise that the similarity will continue
     * across implementations or releases.
     * <p>A Moment is immutable.
     *
     * <p>A Moment object has accessors whenGet() and titleGet().
     *
     * <p>Moment has its own toString() method, which return the two values in (something like)
     * this format: <code>"%012d\t%s"</code>, where the first value is for <code>when</code> and the 
     * second value is for <code>title</code>.
     *
     * <p>A Moment object becomes visible to a caller <u>only</u> as an element in the List returned
     * from <code>Stopwatch.momentsRetrieve()</code>.
     */
    @Immutable
    public interface Moment {
        public long whenGet();
        public String titleGet();
        public String toString();
    }

    /**
     * This enum represents the various interpretations of <code>when</code>.  The value 
     * of <code>when</code> that is displayed in a Moment object is one of these:
     * <pre>
     *     nanoTimeValue:
     *    
     *         The raw value returned from System.nanoTime()
     *     
     *     creationMomentRelative:
     *    
     *         The number of nanoseconds since the creation of the associated Stopwatch object.
     *     
     *     previousMomentRelative:
     *    
     *         The number of nanoseconds since the previous Moment that was recorded by this Stopwatch object.
     *    
     *     taskRelative:
     *    
     *         The number of nanoseconds relative to its "balanced beginning Moment".  This balances
     *         corresponding taskBegin() and taskEnd() moments.
     *    
     * </pre>
     */
    public enum IntervalReportType {
        /**
         * The raw value returned from System.nanoTime()
         */
        nanoTimeValue,
        /**
         * The number of nanoseconds since the creation of the associated Stopwatch object.
         */
        creationMomentRelative,
        /**
         * The number of nanoseconds since the previous Moment that was recorded by this Stopwatch object.
         */
        previousMomentRelative,
        /**
         * The number of nanoseconds relative to the 
         * "beginning Moment" of a task.  This balances
         * nested taskBegin() and taskEnd() moments.
         */
        taskRelative
    };

    /**
     * Record a named moment.
     *
     * @param title the text to use as the identifier of this moment.
     * @return a long that is the elapsed time since the previous
     * moment was recorded. If no previous moment has been recorded
     * using <code>momentRecord()</code>, <code>taskBegin()</code>,
     * or <code>taskEnd()</code>, the elapsed
     * time interval measures the time between the creation of the Stopwatch
     * object and this call.
     */
    public long momentRecord(String title);

    /**
     * Record the beginning of a named task.
     *
     * @param title the text to use as the identifier of this moment.
     * The title is prefixed with the text <code>"TaskBegin:"</code>.
     * @return a long that is the elapsed time since the previous
     * moment was recorded. If no moments have been recorded using momentRecord(), taskBegin(), or taskEnd(), the elapsed
     * time interval measures the time between the creation of the Stopwatch
     * object and this call.
     */
    public long taskBegin(String title);

    /**
     * Record the end of a named task.
     *
     * @param title the text to use as the identifier of this moment.
     * The title is prefixed with the text <code>"TaskEnd:"</code>.
     * @return a long that is the elapsed time since the previous
     * moment was recorded. If no moments have been recorded using
     * momentRecord(), taskBegin(), or taskEnd(), the elapsed
     * time interval measures the time between the creation of the Stopwatch
     * object and this call.
     */
    public long taskEnd(String title);

    /**
     * Return the title string that was used when the Stopwatch was created.
     * This string will never be null.  
     * It may be an empty ("") string.
     *
     * @return a String that is the title of this Stopwatch object.
     */
    public String titleGet();

    /**
     * Return a List of Moment objects that represents the state of the Stopwatch
     * at the time of the call.  This method is thread-safe and returns a
     * copy of the Stopwatch state.  Changes in the list are not reflected in the
     * Stopwatch itself.
     * <p>The return value may be an empty list, but it will never be null.
     * <p>Note that if multiple threads use the same Stopwatch object, the List
     * may contain Moments that <code>the current thread</code> did not create.
     *
     * @return a List containing Moment objects, in the order in which they
     * were recorded by the Stopwatch.
     * @param intervalType a {@link org.billtorcaso.Stopwatch.Stopwatch.IntervalReportType} object.
     */
    public List<Moment> momentsRetrieve(IntervalReportType intervalType);

    /**
     * This enum represents the various measures of elapsed time.  They are:
     * <pre>
     *
     *     timeToNow:  
     *
     *         The relative time from creation of the Stopwatch object to <code>"now"</code>.
     *
     *     timeCreationToLastMoment:  
     *
     *         The relative time from creation of the Stopwatch object to 
     *         the last Moment recorded by the Stopwatch.
     *
     *     timeFirstToLastMoment:  
     *
     *         The relative time from the first Moment recorded by the Stopwatch
     *         to the last Moment recorded by the Stopwatch.
     *
     * </pre>
     *
     * The rationale for <code>timeFirstToLastMoment</code>  method 
     * is that a Stopwatch object may be created at a higher level 
     * of code than the Moments that are worth recording.
     */
    public enum TimeElapsedType {
        /**
         * The relative time from creation of the Stopwatch object to <code>now</code>".
         */
        timeToNow,
        /**
         * The relative time from creation of the Stopwatch object to 
         * the last Moment recorded by the Stopwatch.
         */
        timeCreationToLastMoment,
        /**
         * The relative time from the first Moment recorded by the Stopwatch
         * to the last Moment recorded by the Stopwatch.
         */
        timeFirstToLastMoment
    }

    /**
     * Return a String representation of the Moments in this Stopwatch, in which
     * the representation of the time-interval of the Moment is selected from one of the
     * <code>Stopwatch.IntervalReportType</code> values.
     *
     * @see <a href="Stopwatch.html#IntervalReportType">IntervalReportType</a>
     * @return a String representation of the Moments in this Stopwatch
     * @param thisReportType a {@link org.billtorcaso.Stopwatch.Stopwatch.IntervalReportType} object.
     */
    public String toStringAsIntervals(Stopwatch.IntervalReportType thisReportType);

    /**
     * {@inheritDoc}
     *
     * Represent the Stopwatch's internal state in a String.
     *
     * The returned String presents each Moment in the Stopwatch,
     * in order, with each Moment separated by an embedded "\n"
     * newline character.  Moment.toString() is called to create
     * the String representation of each Moment object.
     */
    @Override
    public String toString();

    /**
     * Produce a nicely-formated text representation of the Stopwatch
     * and its recorded moments, displaying intervals between Moments in
     * the given IntervalReportType.
     * This differs from <code>toString()</code> in that the text lines are
     * indented to show the nesting of TaskBegin and TaskEnd moments.
     * There is an implementation-defined limit to depth of indentation.
     * <p>If the caller has not provided balanced calls to taskBegin() and
     * taskEnd(), or the indentation limit has been excceded,
     * the output will be complete but will not be pretty.
     *
     * @param thisType a value of the Enum type Stopwatch.IntervalReportType
     * that controls the representation of time-intervals in a Moment.
     * @return a String representing the recordedMoments of this Stopwatch,
     * with TaskBegin / TaskEnd pairs having equal indentation.
     */
    public String prettyPrint(Stopwatch.IntervalReportType thisType);

    /**
     * Produce a nicely-formated text representation of the Stopwatch
     * and its recorded moments, using the creationMomentRelative format.
     * <p>This convenience method is equivalent to
     * <code>prettyPrint(Stopwatch.IntervalReportType.previousMomentRelative)</code>.
     *
     * @see <a href="Stopwatch.html#prettyPrint">
     * prettyPrint(Stopwatch.IntervalReportType thisType)</a>
     * @return a String representing the recordedMoments of this Stopwatch,
     * with TaskBegin / TaskEnd pairs having equal indentation.
     */
    public String prettyPrint();

}
