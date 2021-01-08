package com.example.colornoteplus;

import java.util.Stack;

/**
 * Handle Undo and Redo action in the text note activity.
 * @see TextNoteActivity
 */
public class TextUndoRedo {

    /**
     * Undo actions stack
     */
    private final Stack<String> undoStack ;

    /**
     * Redo actions stack
     */
    private final Stack<String> redoStack ;

    /**
     * public constructor
     */
    public TextUndoRedo(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * getter for undo stack
     * @return undo stack
     */
    public Stack<String> getUndoStack(){ return undoStack; }

    /**
     * getter for redo stack
     * @return redo stack
     */
    public Stack<String> getRedoStack(){ return redoStack; }

    /**
     * push a new value into undo stack
     * @param string new value
     */
    public void pushUndo(String string){ undoStack.push(string); }

    /**
     * push a new value into redo stack
     * @param string new value
     */
    public void pushRedo(String string){ redoStack.push(string); }

    /**
     * pop the first value from the undo stack
     * @return undo stack first value
     */
    public String popUndo(){ return undoStack.pop(); }

    /**
     * pop the first value from the redo stack
     * @return redo stack first value
     */
    public String popRedo(){ return redoStack.pop(); }

    /**
     * @deprecated
     * clear the undo stack
     */
    public void resetUndo(){ undoStack.clear(); }

    /**
     * clear the redo stack
     */
    public void resetRedo(){ redoStack.clear(); }

}
