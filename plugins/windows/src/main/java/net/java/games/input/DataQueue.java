/*
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * @author elias
 * @version 1.0
 */
final class DataQueue<T> {
	private final T[] elements;
	private int position;
	private int limit;

    @SuppressWarnings("unchecked")
	public DataQueue(int size, Class<T> element_type) {
		this.elements= (T[])Array.newInstance(element_type, size);
		for (int i = 0; i < elements.length; i++) {
			try {
				elements[i] = element_type.getDeclaredConstructor().newInstance();
			} catch (InstantiationException|IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		clear();
	}

	public void clear() {
		position = 0;
		limit = elements.length;
	}

	public int position() {
		return position;
	}

	public int limit() {
		return limit;
	}

	public T get(int index) {
		assert index < limit;
		return elements[index];
	}

	public T get() {
		if (!hasRemaining())
			return null;
		return get(position++);
	}

	public void compact() {
		int index = 0;
		while (hasRemaining()) {
			swap(position, index);
			position++;
			index++;
		}
		position = index;
		limit = elements.length;
	}

	private void swap(int index1, int index2) {
		T temp = elements[index1];
		elements[index1] = elements[index2];
		elements[index2] = temp;
	}

	public void flip() {
		limit = position;
		position = 0;
	}

	public boolean hasRemaining() {
		return remaining() > 0;
	}

	public int remaining() {
		return limit - position;
	}

	public void position(int position) {
		this.position = position;
	}

	public T[] getElements() {
		return elements;
	}
}
