package br.com.jiratorio.extension

import rx.Single
import rx.functions.FuncN

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> Single<*>.zip(
    s1: T1,
    s2: T2,
    s3: T3,
    s4: T4,
    s5: T5,
    s6: T6,
    s7: T7,
    s8: T8,
    s9: T9,
    s10: T10,
    s11: T11,
    s12: T12,
    s13: T13,
    s14: T14
): Single<R> {
    Single.zip(listOf(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14)) {

    }
}
