package arrow.typeclasses

/**
 * The [Semiring] type class for a given type `A` combines both a commutative additive [Monoid] and a multiplicative [Monoid].
 *  It requires the multiplicative [Monoid] to distribute over the additive one. The operations of the multiplicative [Monoid] have been renamed to
 *  [one] and [combineMultiplicate] for easier use.
 *
 * ```kotlin
 * (a.combineMultiplicate(b)).combineMultiplicate(c) == a.combineMultiplicate(b.combineMultiplicate(c))
 * ```
 *
 * The [one] function serves exactly like the [empty] function for an additive [Monoid], just adapted for the multiplicative
 * version. This forms the following law:
 *
 * ```kotlin
 * a.combineMultiplicate(one()) == one().combineMultiplicate(a) == a
 * ```
 *
 * Please note that the empty function has been renamed to [zero] to get a consistent naming style inside the semiring.
 *
 * Currently, [Semiring] instances are defined for all available number types.
 *
 * ### Examples
 *
 * Here a some examples:
 *
 * ```kotlin:ank:playground
 * import arrow.typeclasses.Semiring
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   Semiring.int().run { 1.combine(2) }
 *   //sampleEnd
 *   println(result)
 * }
 * ```
 *
 * ```kotlin:ank:playground
 * import arrow.typeclasses.Semiring
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   Semiring.int().run { 2.combineMultiplicate(3) }
 *   //sampleEnd
 *   println(result)
 * }
 * ```
 *
 * The type class `Semiring` also has support for the `+` `*` syntax:
 *
 * ```kotlin:ank:playground
 * import arrow.typeclasses.Semiring
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   Semiring.int().run {
 *      1 + 2
 *   }
 *   //sampleEnd
 *   println(result)
 * }
 * ```
 *
 * ```kotlin:ank:playground
 * import arrow.typeclasses.Semiring
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   Semiring.int().run {
 *      2 * 3
 *   }
 *   //sampleEnd
 *   println(result)
 * }
 * ```
 */
interface Semiring<A> {

  /**
   * A zero value for this A
   */
  fun zero(): A

  /**
   * A one value for this A
   */
  fun one(): A

  fun A.combine(b: A): A

  operator fun A.plus(b: A): A =
    combine(b)

  /**
   * Multiplicatively combine two [A] values.
   */
  fun A.combineMultiplicate(b: A): A

  operator fun A.times(b: A): A =
    this.combineMultiplicate(b)

  /**
   * Maybe additively combine two [A] values.
   */
  fun A?.maybeCombineAddition(b: A?): A =
    if (this == null) zero()
    else b?.let { combine(it) } ?: this

  /**
   * Maybe multiplicatively combine two [A] values.
   */
  fun A?.maybeCombineMultiplicate(b: A?): A =
    if (this == null) one()
    else b?.let { combineMultiplicate(it) } ?: this

  companion object {
    @JvmStatic
    @JvmName("Float")
    fun float(): Semiring<Float> = FloatSemiring

    @JvmStatic
    @JvmName("Short")
    fun short(): Semiring<Short> = ShortSemiring

    @JvmStatic
    @JvmName("Long")
    fun long(): Semiring<Long> = LongSemiring

    @JvmStatic
    @JvmName("Integer")
    fun int(): Semiring<Int> = IntSemiring

    @JvmStatic
    @JvmName("Double")
    fun double(): Semiring<Double> = DoubleSemiring

    @JvmStatic
    @JvmName("Byte")
    fun byte(): Semiring<Byte> = ByteSemiring

    private object ByteSemiring : Semiring<Byte> {
      override fun one(): Byte = 1
      override fun zero(): Byte = 0

      override fun Byte.combine(b: Byte): Byte = (this + b).toByte()
      override fun Byte.combineMultiplicate(b: Byte): Byte = (this * b).toByte()
    }

    private object DoubleSemiring : Semiring<Double> {
      override fun one(): Double = 1.0
      override fun zero(): Double = 0.0

      override fun Double.combine(b: Double): Double = this + b
      override fun Double.combineMultiplicate(b: Double): Double = this * b
    }

    private object IntSemiring : Semiring<Int> {
      override fun one(): Int = 1
      override fun zero(): Int = 0

      override fun Int.combine(b: Int): Int = this + b
      override fun Int.combineMultiplicate(b: Int): Int = this * b
    }

    private object LongSemiring : Semiring<Long> {
      override fun one(): Long = 1
      override fun zero(): Long = 0

      override fun Long.combine(b: Long): Long = this + b
      override fun Long.combineMultiplicate(b: Long): Long = this * b
    }

    private object ShortSemiring : Semiring<Short> {
      override fun one(): Short = 1
      override fun zero(): Short = 0

      override fun Short.combine(b: Short): Short = (this + b).toShort()
      override fun Short.combineMultiplicate(b: Short): Short = (this * b).toShort()
    }

    private object FloatSemiring : Semiring<Float> {
      override fun one(): Float = 1f
      override fun zero(): Float = 0f

      override fun Float.combine(b: Float): Float = this + b
      override fun Float.combineMultiplicate(b: Float): Float = this * b
    }
  }
}
