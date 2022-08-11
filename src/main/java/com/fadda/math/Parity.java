package com.fadda.math;

/*
 * Permite clasificar un entero en {@code PAIR} o {@code UNPAIR}.
 */
public enum Parity {
	PAIR, UNPAIR;

	/**
	 * Calcula la paridad de un entero.
	 *
	 * @param n el entero del que queremos saber su paridad.
	 * @return {@code PAIR} si es par o {@code UNPAIR} si es impar.
	 */
	public static Parity get(Integer n) {
		return n % 2 == 0 ? PAIR: UNPAIR;
	}
}
