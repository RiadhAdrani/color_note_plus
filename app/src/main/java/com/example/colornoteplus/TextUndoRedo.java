package com.example.colornoteplus;

import java.util.Stack;

public class TextUndoRedo {

    private final Stack<String> undoStack ;
    private final Stack<String> redoStack ;

    public TextUndoRedo(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public Stack<String> getUndoStack(){ return undoStack; }

    public Stack<String> getRedoStack(){ return redoStack; }

    public void pushUndo(String string){ undoStack.push(string); }

    public void pushRedo(String string){ redoStack.push(string); }

    public String popUndo(){ return undoStack.pop(); }

    public String popRedo(){ return redoStack.pop(); }

    public void resetUndo(){ undoStack.clear(); }

    public void resetRedo(){ redoStack.clear(); }

}
