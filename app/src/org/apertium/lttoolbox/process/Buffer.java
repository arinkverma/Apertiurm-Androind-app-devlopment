package org.apertium.lttoolbox.process;

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */

/**
 * circular character buffer class
 * @author Raah
 */
public class Buffer {

    /**
     * Buffer size.
     */
    int size;
    
    /**
     * Buffer array.
     */
    char[] buf;
    
    /**
     * Buffer current position.
     */
    int currentpos;
    
    /**
     * Last position.
     */
    int lastpos;

    /**
     * Copy the buffer
     * @param b the buffer to copy
     */
    void copy(Buffer b) {
        currentpos = b.currentpos;
        lastpos = b.lastpos;
        size = b.size;
        buf = new char[size];
        System.arraycopy(buf, 0, b.buf, 0, size);
    }

    /**
     * Constructor
     * @param buf_size buffer size
     */
    Buffer(int buf_size) {
        if (buf_size == 0) {
            throw new RuntimeException("Error: Cannot create empty buffer.");

        }
        buf = new char[buf_size];
        size = buf_size;
        currentpos = 0;
        lastpos = 0;
    }

    /**
     * Copy constructor.
     */
    Buffer(Buffer b) {
        copy(b);
    }

    /**
     * Add an element to the buffer.
     * @param value the value.
     */
    void add(char value) {
        if (lastpos == size) {
            lastpos = 0;
        }
        buf[lastpos++] = value;
        currentpos = lastpos;
    //return buf[lastpos - 1];
    }

    /**
     * Consume the buffer's current value.
     * @return the current value.
     */
    char next() {
        if (currentpos != lastpos) {
            if (currentpos == size) {
                currentpos = 0;
            }
            return buf[currentpos++];
        } else {
            return last();
        }
    }

    /**
     * Get the last element of the buffer.
     * @return last element.
     */
    char last() {
        if (lastpos != 0) {
            return buf[lastpos - 1];
        } else {
            return buf[size - 1];
        }
    }

    /**
     * Get the current buffer position.
     * @return the position.
     */
    int getPos() {
        return currentpos;
    }

    /**
     * Set the buffer to a new position.
     * @param newpos the new position.
     */
    void setPos(int newpos) {
        currentpos = newpos;
    }

    /**
     * Return the range size between the buffer current position and a
     * outside stored given position that is previous to the current.
     * @param prevpos the given position.
     * @return the range size.
     */
    int diffPrevPos(int prevpos) {
        if (prevpos <= currentpos) {
            return currentpos - prevpos;
        } else {
            return currentpos + size - prevpos;
        }
    }

    /**
     * Return the range size between the buffer current position and a
     * outside stored given position that is following to the current
     * @param postpos the given position.
     * @return the range size.
     */
    int diffPostPos(int postpos) {
        if (postpos >= currentpos) {
            return postpos - currentpos;
        } else {
            return postpos + size - currentpos;
        }
    }

    /**
     * Checks the buffer for emptyness.
     * @return true if the buffer is empty.
     */
    boolean isEmpty() {
        return currentpos == lastpos;
    }

    /**
     * Gets back 'posback' positions in the buffer.
     * @param posback the amount of position to get back.
     */
    void back(int posback) {
        if (currentpos > posback) {
            currentpos -= posback;
        } else {
            currentpos = size - (posback - currentpos);
        }
    }

    /**
     * Computes a string representation of the buffer
     * @return the string representation of the buffer
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
    	sb.append("content : ");
        for (int i = 0; i < buf.length; i++) {
            sb.append(buf[i]);
        }
        sb.append(", lastpos : " + lastpos + ", currentpos : " + currentpos);
        return sb.toString();
    }
}
