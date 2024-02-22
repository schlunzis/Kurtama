package org.schlunzis.kurtama.common.game.model;

public interface IDirection {

    IDirection[] directions();

    /**
     * Returns the number of directions/sides/neighbours of the implementation.
     *
     * @return the number
     */
    default int size() {
        return directions().length;
    }

    int getIndex();

}
