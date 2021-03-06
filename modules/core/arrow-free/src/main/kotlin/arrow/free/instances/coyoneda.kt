package arrow.free.instances

import arrow.*
import arrow.free.*
import arrow.typeclasses.Functor

@instance(Coyoneda::class)
interface CoyonedaFunctorInstance<F, G> : Functor<CoyonedaPartialOf<F, G>> {
  override fun <A, B> Kind<CoyonedaPartialOf<F, G>, A>.map(f: (A) -> B): Coyoneda<F, G, B> = fix().map(f)
}

class CoyonedaContext<F, G> : CoyonedaFunctorInstance<F, G>

class CoyonedaContextPartiallyApplied<F, G> {
  infix fun <A> extensions(f: CoyonedaContext<F, G>.() -> A): A =
    f(CoyonedaContext())
}

fun <F, G> ForCoyoneda(): CoyonedaContextPartiallyApplied<F, G> =
  CoyonedaContextPartiallyApplied()