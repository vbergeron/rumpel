package io.funktional.rumpel

/**
 * A unique name generator.
 *
 * @param config the configuration for the Rumpel generator
 */
final case class Rumpel(config: RumpelConfig, private var random: Random):
    /**
     * Generates a string based on the dictionaries and style defined in the configuration.
     *
     * @return the generated string
     */
    def generate: String =
        val (rng1, dictionaries) = random.pick(config.dictionaries, config.length)
        val (rng2, words)        = dictionaries.foldLeft((rng1, List.empty[String])):
            case ((rng, acc), dictionary) =>
                val (nextRng, word) = dictionary.pickOne(rng)
                (nextRng, acc :+ word)
        this.random = rng2
        words.map(config.style.format).mkString(" ").replace(" ", config.separator)
    end generate
end Rumpel

object Rumpel:
    def apply(config: RumpelConfig): Rumpel =
        Rumpel(config, config.seed.fold(Random.default)(Random.withSeed))
