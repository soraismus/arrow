package kategory

typealias Algebra <F, A> = (HK<F, A>) -> A

typealias AlgebraM <M, F, A> = (HK<F, A>) -> HK<M, A>

typealias Coalgebra <F, A> = (A) -> HK<F, A>

typealias CoalgebraM <M, F, A> = (A) -> HK<M, HK<F, A>>

fun <F, A, B> hylo(a: A, alg: Algebra<F, B>, coalg: Coalgebra<F, A>, FF: Functor<F>): B =
        alg(FF.map(coalg(a), { hylo(it, alg, coalg, FF) }))

fun <M, F, A, B> hyloM(a: A, algM: AlgebraM<M, F, B>, coalgM: CoalgebraM<M, F, A>, TF: Traverse<F>, MM: Monad<M>): HK<M, B> =
        hylo(
                a,
                { MM.flatMap(it.lower(), { MM.flatMap(TF.sequence(MM, it), algM) }) },
                { aa: A -> coalgM(aa).lift() },
                ComposedFunctor(MM, TF)
        )

fun <F, G> algebraIso(alg: Algebra<ComposedType<F, G>, HK<F, G>>, coalg: Coalgebra<ComposedType<F, G>, HK<F, G>>, FG: Functor<G>): Birecursive<F, G> =
        object : Birecursive<F, G> {
            override fun FG(): Functor<G> = FG

            override fun projectT(fg: HK<F, G>): HK<ComposedType<F, G>, HK<F, G>> =
                    coalg(fg)

            override fun embedT(compFG: HK<ComposedType<ComposedType<F, G>, F>, G>): HK<F, G> =
                    alg(compFG.lower())
        }